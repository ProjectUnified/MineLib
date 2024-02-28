package io.github.projectunified.minelib.scheduler.canceller;

import io.github.projectunified.minelib.scheduler.common.util.PlatformChecker;
import io.github.projectunified.minelib.scheduler.common.util.supplier.ObjectSupplier;
import io.github.projectunified.minelib.scheduler.common.util.supplier.ObjectSupplierList;
import org.bukkit.plugin.Plugin;

public interface TaskCanceller {
    ObjectSupplierList<TaskCanceller> SUPPLIERS = new ObjectSupplierList<>(
            TaskCanceller.class,
            ObjectSupplier.of(PlatformChecker::isFolia, FoliaTaskCanceller::new),
            ObjectSupplier.of(BukkitTaskCanceller::new)
    );

    static TaskCanceller get(Plugin plugin) {
        return SUPPLIERS.get(plugin);
    }

    void cancelAll();
}
