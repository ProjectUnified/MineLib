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

/**
 * A {@link Function} factory that provides a way to select a provider based on a predicate
 *
 * @param <K> the type of the key
 * @param <T> the type of the object
 */
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

    /**
     * Create a new instance of {@link ObjectProvider} with the given entries
     *
     * @param entries the entries
     */
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

    /**
     * Create an entry with the given predicate and function
     *
     * @param predicate the predicate
     * @param function  the function
     * @param <K>       the type of the key
     * @param <T>       the type of the object
     * @return the entry
     */
    public static <K, T> Map.Entry<BooleanSupplier, Function<K, T>> entry(BooleanSupplier predicate, Function<K, T> function) {
        return new AbstractMap.SimpleEntry<>(predicate, function);
    }

    /**
     * Create an entry with the given function. The predicate will always return true.
     *
     * @param function the function
     * @param <K>      the type of the key
     * @param <T>      the type of the object
     * @return the entry
     */
    public static <K, T> Map.Entry<BooleanSupplier, Function<K, T>> entry(Function<K, T> function) {
        return entry(() -> true, function);
    }

    /**
     * Get the object for the given key
     *
     * @param key the key
     * @return the object
     */
    public T get(K key) {
        try {
            return cache.get(key);
        } catch (ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }
}
