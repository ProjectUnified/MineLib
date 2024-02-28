package io.github.projectunified.scheduler.common.util.supplier;

import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class ObjectSupplier<S> implements Function<Plugin, Optional<S>> {
    private final BooleanSupplier predicate;
    private final Function<Plugin, S> supplier;

    private ObjectSupplier(BooleanSupplier predicate, Function<Plugin, S> supplier) {
        this.predicate = predicate;
        this.supplier = supplier;
    }

    public static <S> ObjectSupplier<S> of(BooleanSupplier predicate, Function<Plugin, S> supplier) {
        return new ObjectSupplier<>(predicate, supplier);
    }

    public static <S> ObjectSupplier<S> of(Function<Plugin, S> supplier) {
        return new ObjectSupplier<>(() -> true, supplier);
    }

    @Override
    public Optional<S> apply(Plugin plugin) {
        return predicate.getAsBoolean() ? Optional.of(supplier.apply(plugin)) : Optional.empty();
    }
}
