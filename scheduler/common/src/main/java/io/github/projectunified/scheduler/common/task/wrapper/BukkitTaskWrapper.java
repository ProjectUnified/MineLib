package io.github.projectunified.scheduler.common.task.wrapper;

import io.github.projectunified.scheduler.common.task.Task;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public interface BukkitTaskWrapper {
    static Task wrap(BukkitTask bukkitTask, boolean repeating) {
        return new Task() {
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
        };
    }
}
