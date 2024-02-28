package io.github.projectunified.minelib.scheduler.entity;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.time.TaskTime;
import io.github.projectunified.minelib.scheduler.common.time.TimerTaskTime;
import io.github.projectunified.minelib.scheduler.common.util.PlatformChecker;
import io.github.projectunified.minelib.scheduler.common.util.supplier.ObjectSupplier;
import io.github.projectunified.minelib.scheduler.common.util.supplier.ObjectSupplierList;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

public interface EntityScheduler {
    ObjectSupplierList<EntityScheduler> SUPPLIERS = new ObjectSupplierList<>(
            EntityScheduler.class,
            ObjectSupplier.of(PlatformChecker::isFolia, FoliaEntityScheduler::new),
            ObjectSupplier.of(BukkitEntityScheduler::new)
    );

    static EntityScheduler get(Plugin plugin) {
        return SUPPLIERS.get(plugin);
    }

    Task run(Entity entity, Runnable runnable, Runnable retired);

    Task runLater(Entity entity, Runnable runnable, Runnable retired, TaskTime delay);

    Task runTimer(Entity entity, BooleanSupplier runnable, Runnable retired, TimerTaskTime timerTaskTime);

    default boolean isEntityValid(Entity entity) {
        if (entity == null) {
            return false;
        }

        if (entity instanceof Player) {
            return ((Player) entity).isOnline();
        }

        return entity.isValid();
    }

    default Task runTimer(Entity entity, Runnable runnable, Runnable retired, TimerTaskTime timerTaskTime) {
        return runTimer(entity, () -> {
            runnable.run();
            return true;
        }, retired, timerTaskTime);
    }

    default Task runLater(Entity entity, Runnable runnable, TaskTime delay) {
        return runLater(entity, runnable, () -> {
        }, delay);
    }

    default Task runTimer(Entity entity, BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
        return runTimer(entity, runnable, () -> {
        }, timerTaskTime);
    }

    default Task runTimer(Entity entity, Runnable runnable, TimerTaskTime timerTaskTime) {
        return runTimer(entity, runnable, () -> {
        }, timerTaskTime);
    }

    default Task runLaterWithFinalizer(Entity entity, Runnable runnable, Runnable finalizer, TaskTime delay) {
        return runLater(entity, () -> {
            try {
                runnable.run();
            } finally {
                finalizer.run();
            }
        }, finalizer, delay);
    }
}
