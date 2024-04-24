package io.github.projectunified.minelib.scheduler.common.util.task;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.BooleanSupplier;

/**
 * A wrapped {@link Task} for a Bukkit task
 */
public class BukkitTask implements Task {
    private final org.bukkit.scheduler.BukkitTask bukkitTask;

    /**
     * Create a new instance of {@link BukkitTask}
     *
     * @param bukkitTask the Bukkit task to wrap
     */
    public BukkitTask(org.bukkit.scheduler.BukkitTask bukkitTask) {
        this.bukkitTask = bukkitTask;
    }

    /**
     * Wrap a {@link Runnable} into a {@link BukkitRunnable}
     *
     * @param runnable the runnable to wrap
     * @return the wrapped runnable
     */
    public static BukkitRunnable wrapRunnable(BooleanSupplier runnable) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (!runnable.getAsBoolean()) {
                    cancel();
                }
            }
        };
    }

    /**
     * Get the original Bukkit task
     *
     * @return The original Bukkit task
     */
    public org.bukkit.scheduler.BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    @Override
    public boolean isCancelled() {
        try {
            return bukkitTask.isCancelled();
        } catch (Throwable throwable) {
            int taskId = bukkitTask.getTaskId();
            return !Bukkit.getScheduler().isQueued(taskId) && !Bukkit.getScheduler().isCurrentlyRunning(taskId);
        }
    }

    @Override
    public boolean isDone() {
        try {
            if (bukkitTask.isCancelled()) {
                return false;
            }
        } catch (Throwable ignored) {
            // IGNORED
        }
        int taskId = bukkitTask.getTaskId();
        return !Bukkit.getScheduler().isQueued(taskId) && !Bukkit.getScheduler().isCurrentlyRunning(taskId);
    }

    @Override
    public void cancel() {
        bukkitTask.cancel();
    }

    @Override
    public Plugin getPlugin() {
        return bukkitTask.getOwner();
    }
}
