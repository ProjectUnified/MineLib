package io.github.projectunified.minelib.scheduler.common.provider;

import org.bukkit.plugin.Plugin;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class ObjectProvider<T> {
    private final Function<Plugin, T> function;
    private final Map<Plugin, T> map = new ConcurrentHashMap<>();

    public ObjectProvider(Function<Plugin, T> function) {
        this.function = function;
    }

    @SafeVarargs
    public ObjectProvider(Map.Entry<BooleanSupplier, Function<Plugin, T>>... entries) {
        Function<Plugin, T> function = null;
        for (Map.Entry<BooleanSupplier, Function<Plugin, T>> entry : entries) {
            if (entry.getKey().getAsBoolean()) {
                function = entry.getValue();
            }
        }
        if (function == null) {
            throw new IllegalStateException("No provider found");
        }
        this.function = function;
    }

    public static <T> Map.Entry<BooleanSupplier, Function<Plugin, T>> entry(BooleanSupplier predicate, Function<Plugin, T> function) {
        return new AbstractMap.SimpleEntry<>(predicate, function);
    }

    public static <T> Map.Entry<BooleanSupplier, Function<Plugin, T>> entry(Function<Plugin, T> function) {
        return entry(() -> true, function);
    }

    public T get(Plugin plugin) {
        return map.computeIfAbsent(plugin, function);
    }
}
