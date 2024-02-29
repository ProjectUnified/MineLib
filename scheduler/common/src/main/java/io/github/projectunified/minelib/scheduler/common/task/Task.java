package io.github.projectunified.minelib.scheduler.common.task;

import org.bukkit.plugin.Plugin;

public interface Task {
    /**
     * Check if the task is cancelled
     *
     * @return true if the task is cancelled
     */
    boolean isCancelled();

    /**
     * Cancel the task
     */
    void cancel();

    /**
     * Get the plugin that owns the task
     *
     * @return the plugin
     */
    Plugin getPlugin();
}
