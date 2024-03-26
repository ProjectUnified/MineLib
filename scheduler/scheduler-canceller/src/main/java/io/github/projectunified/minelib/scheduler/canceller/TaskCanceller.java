package io.github.projectunified.minelib.scheduler.canceller;

import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.plugin.Plugin;

/**
 * An interface for cancelling tasks
 */
public interface TaskCanceller {
    /**
     * Get the {@link TaskCanceller} for the given plugin
     *
     * @param plugin the plugin
     * @return the {@link TaskCanceller}
     */
    static TaskCanceller get(Plugin plugin) {
        return Platform.FOLIA.isPlatform() ? new FoliaTaskCanceller(plugin) : new BukkitTaskCanceller(plugin);
    }

    /**
     * Cancel all tasks
     */
    void cancelAll();
}
