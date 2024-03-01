package io.github.projectunified.minelib.scheduler.common.provider;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class ObjectProvider<T> {
    private final Function<Plugin, T> function;
    private final LoadingCache<Plugin, T> cache = CacheBuilder.newBuilder()
            .weakKeys()
            .initialCapacity(20)
            .build(new CacheLoader<Plugin, T>() {
                @Override
                public @NotNull T load(@NotNull Plugin key) {
                    return function.apply(key);
                }
            });

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
        try {
            return cache.get(plugin);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }
}
