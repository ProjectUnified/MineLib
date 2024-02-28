package io.github.projectunified.scheduler.common.task;

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
     * Check if the task is async
     *
     * @return true if the task is async
     */
    boolean isAsync();

    /**
     * Check if the task is repeating
     *
     * @return true if the task is repeating
     */
    boolean isRepeating();

    /**
     * Get the plugin that owns the task
     *
     * @return the plugin
     */
    Plugin getPlugin();
}
