package io.github.projectunified.minelib.scheduler.async;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.time.TaskTime;
import io.github.projectunified.minelib.scheduler.common.time.TimerTaskTime;
import io.github.projectunified.minelib.scheduler.common.util.PlatformChecker;
import io.github.projectunified.minelib.scheduler.common.util.supplier.ObjectSupplier;
import io.github.projectunified.minelib.scheduler.common.util.supplier.ObjectSupplierList;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

public interface AsyncScheduler {
    ObjectSupplierList<AsyncScheduler> SUPPLIERS = new ObjectSupplierList<>(
            AsyncScheduler.class,
            ObjectSupplier.of(PlatformChecker::isFolia, FoliaAsyncScheduler::new),
            ObjectSupplier.of(BukkitAsyncScheduler::new)
    );

    static AsyncScheduler get(Plugin plugin) {
        return SUPPLIERS.get(plugin);
    }

    Task run(Runnable runnable);

    Task runLater(Runnable runnable, TaskTime delay);

    Task runTimer(BooleanSupplier runnable, TimerTaskTime timerTaskTime);

    default Task runTimer(Runnable runnable, TimerTaskTime timerTaskTime) {
        return runTimer(() -> {
            runnable.run();
            return true;
        }, timerTaskTime);
    }
}
