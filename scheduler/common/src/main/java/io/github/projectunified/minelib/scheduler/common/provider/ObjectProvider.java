package io.github.projectunified.minelib.scheduler.common.provider;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class ObjectProvider<K, T> {
    private final Function<K, T> function;
    private final LoadingCache<K, T> cache = CacheBuilder.newBuilder()
            .weakKeys()
            .build(new CacheLoader<K, T>() {
                @Override
                public @NotNull T load(@NotNull K key) {
                    return function.apply(key);
                }
            });

    @SafeVarargs
    public ObjectProvider(Map.Entry<BooleanSupplier, Function<K, T>>... entries) {
        Function<K, T> function = null;
        for (Map.Entry<BooleanSupplier, Function<K, T>> entry : entries) {
            if (entry.getKey().getAsBoolean()) {
                function = entry.getValue();
            }
        }
        if (function == null) {
            throw new IllegalStateException("No provider found");
        }
        this.function = function;
    }

    public static <K, T> Map.Entry<BooleanSupplier, Function<K, T>> entry(BooleanSupplier predicate, Function<K, T> function) {
        return new AbstractMap.SimpleEntry<>(predicate, function);
    }

    public static <K, T> Map.Entry<BooleanSupplier, Function<K, T>> entry(Function<K, T> function) {
        return entry(() -> true, function);
    }

    public T get(K plugin) {
        try {
            return cache.get(plugin);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }
}
