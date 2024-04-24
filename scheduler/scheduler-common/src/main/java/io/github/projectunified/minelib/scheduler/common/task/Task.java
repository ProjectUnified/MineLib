package io.github.projectunified.minelib.scheduler.common.task;

import org.bukkit.plugin.Plugin;

/**
 * A scheduled task
 */
public interface Task {
    /**
     * Check if the task is cancelled
     *
     * @return true if the task is cancelled
     */
    boolean isCancelled();

    /**
     * Check if the task is done
     *
     * @return true if the task is done
     */
    boolean isDone();

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
