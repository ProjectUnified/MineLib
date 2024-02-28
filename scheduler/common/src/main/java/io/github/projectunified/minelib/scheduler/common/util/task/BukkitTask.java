package io.github.projectunified.minelib.scheduler.common.util.task;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.BooleanSupplier;

public class BukkitTask implements Task {
    private final org.bukkit.scheduler.BukkitTask bukkitTask;
    private final boolean repeating;

    public BukkitTask(org.bukkit.scheduler.BukkitTask bukkitTask, boolean repeating) {
        this.bukkitTask = bukkitTask;
        this.repeating = repeating;
    }

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

    public org.bukkit.scheduler.BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    @Override
    public boolean isCancelled() {
        try {
            return bukkitTask.isCancelled();
        } catch (Throwable throwable) {
            int taskId = bukkitTask.getTaskId();
            return !(Bukkit.getScheduler().isQueued(taskId) || Bukkit.getScheduler().isCurrentlyRunning(taskId));
        }
    }

    @Override
    public void cancel() {
        bukkitTask.cancel();
    }

    @Override
    public boolean isAsync() {
        return !bukkitTask.isSync();
    }

    @Override
    public boolean isRepeating() {
        return repeating;
    }

    @Override
    public Plugin getPlugin() {
        return bukkitTask.getOwner();
    }
}
