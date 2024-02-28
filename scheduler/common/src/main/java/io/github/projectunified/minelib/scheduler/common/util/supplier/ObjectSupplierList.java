package io.github.projectunified.minelib.scheduler.common.util.supplier;

import io.github.projectunified.minelib.scheduler.common.pool.ObjectPool;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ObjectSupplierList<T> {
    private final Class<T> clazz;
    private final List<Function<Plugin, Optional<T>>> suppliers;

    @SafeVarargs
    public ObjectSupplierList(Class<T> clazz, Function<Plugin, Optional<T>>... suppliers) {
        this.clazz = clazz;
        this.suppliers = Arrays.asList(suppliers);
    }

    public T get(Plugin plugin) {
        return ObjectPool.get(plugin, clazz, suppliers);
    }
}
