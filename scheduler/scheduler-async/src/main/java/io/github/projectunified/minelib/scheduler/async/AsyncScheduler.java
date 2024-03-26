package io.github.projectunified.minelib.scheduler.async;

import io.github.projectunified.minelib.scheduler.common.scheduler.Scheduler;
import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

/**
 * A {@link Scheduler} that can run tasks asynchronously
 */
public interface AsyncScheduler extends Scheduler {
    /**
     * Get the {@link AsyncScheduler} for the given plugin
     *
     * @param plugin the plugin
     * @return the scheduler
     */
    static AsyncScheduler get(Plugin plugin) {
        return Platform.FOLIA.isPlatform() ? new FoliaAsyncScheduler(plugin) : new BukkitAsyncScheduler(plugin);
    }

    /**
     * Run a task later
     *
     * @param runnable the runnable
     * @param delay    the delay
     * @param unit     the unit of the delay
     * @return the task
     */
    Task runLater(Runnable runnable, long delay, TimeUnit unit);

    /**
     * Run a task repeatedly
     *
     * @param runnable the runnable, returning {@code true} to continue or {@code false} to stop
     * @param delay    the delay
     * @param period   the period
     * @param unit     the unit of the delay and period
     * @return the task
     */
    Task runTimer(BooleanSupplier runnable, long delay, long period, TimeUnit unit);

    /**
     * Run a task repeatedly
     *
     * @param runnable the runnable
     * @param delay    the delay
     * @param period   the period
     * @param unit     the unit of the delay and period
     * @return the task
     */
    default Task runTimer(Runnable runnable, long delay, long period, TimeUnit unit) {
        return runTimer(() -> {
            runnable.run();
            return true;
        }, delay, period, unit);
    }
}
