package io.github.projectunified.minelib.scheduler.entity;

import io.github.projectunified.minelib.scheduler.common.scheduler.Scheduler;
import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

/**
 * A {@link Scheduler} that schedules tasks for an {@link Entity}
 */
public interface EntityScheduler extends Scheduler {
    /**
     * Check if the {@link Entity} is valid.
     * This checks if the entity is not null and is still valid.
     * For {@link Player} entities, this also checks if the player is online.
     *
     * @param entity the entity
     * @return {@code true} if the entity is valid
     */
    static boolean isEntityValid(Entity entity) {
        if (entity == null) {
            return false;
        }

        if (entity instanceof Player) {
            return ((Player) entity).isOnline();
        }

        return entity.isValid();
    }

    /**
     * Get the {@link EntityScheduler} for the given {@link Plugin} and {@link Entity}
     *
     * @param plugin the plugin
     * @param entity the entity
     * @return the scheduler
     */
    static EntityScheduler get(Plugin plugin, Entity entity) {
        return Platform.FOLIA.isPlatform()
                ? new FoliaEntityScheduler(plugin, entity)
                : new BukkitEntityScheduler(plugin, entity);
    }

    /**
     * Run a task
     *
     * @param runnable the runnable
     * @param retired  the runnable called when the entity is retired
     * @return the task
     */
    Task run(Runnable runnable, Runnable retired);

    /**
     * Run a task later
     *
     * @param runnable the runnable
     * @param retired  the runnable called when the entity is retired
     * @param delay    the delay in ticks
     * @return the task
     */
    Task runLater(Runnable runnable, Runnable retired, long delay);

    /**
     * Run a task repeatedly
     *
     * @param runnable the runnable, returning {@code true} to continue or {@code false} to stop
     * @param retired  the runnable called when the entity is retired
     * @param delay    the delay in ticks
     * @param period   the period in ticks
     * @return the task
     */
    Task runTimer(BooleanSupplier runnable, Runnable retired, long delay, long period);

    /**
     * Run a task repeatedly
     *
     * @param runnable the runnable
     * @param retired  the runnable called when the entity is retired
     * @param delay    the delay in ticks
     * @param period   the period in ticks
     * @return the task
     */
    default Task runTimer(Runnable runnable, Runnable retired, long delay, long period) {
        return runTimer(() -> {
            runnable.run();
            return true;
        }, retired, delay, period);
    }

    @Override
    default Task run(Runnable runnable) {
        return run(runnable, () -> {
        });
    }

    @Override
    default Task runLater(Runnable runnable, long delay) {
        return runLater(runnable, () -> {
        }, delay);
    }

    @Override
    default Task runTimer(BooleanSupplier runnable, long delay, long period) {
        return runTimer(runnable, () -> {
        }, delay, period);
    }
}
