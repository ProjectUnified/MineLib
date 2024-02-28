package io.github.projectunified.scheduler.common.pool;

import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class ObjectPool {
    private static final Map<Plugin, Map<Class<?>, Object>> objectPool = new ConcurrentHashMap<>();

    public static <T> T get(Plugin plugin, Class<T> clazz, List<Function<Plugin, Optional<T>>> suppliers) {
        Map<Class<?>, Object> pluginPool = objectPool.computeIfAbsent(plugin, k -> new ConcurrentHashMap<>());
        if (pluginPool.containsKey(clazz)) {
            return clazz.cast(pluginPool.get(clazz));
        }

        for (Function<Plugin, Optional<T>> supplier : suppliers) {
            Optional<T> optional = supplier.apply(plugin);
            if (optional.isPresent()) {
                T instance = optional.get();
                pluginPool.put(clazz, instance);
                return instance;
            }
        }

        throw new IllegalStateException("No supplier for " + clazz.getName());
    }
}
