package io.github.projectunified.minelib.scheduler.canceller;

import io.github.projectunified.minelib.scheduler.common.provider.ObjectProvider;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.plugin.Plugin;

/**
 * An interface for cancelling tasks
 */
public interface TaskCanceller {
    ObjectProvider<Plugin, TaskCanceller> PROVIDER = new ObjectProvider<>(
            ObjectProvider.entry(Platform.FOLIA::isPlatform, FoliaTaskCanceller::new),
            ObjectProvider.entry(BukkitTaskCanceller::new)
    );

    /**
     * Get the {@link TaskCanceller} for the given plugin
     *
     * @param plugin the plugin
     * @return the {@link TaskCanceller}
     */
    static TaskCanceller get(Plugin plugin) {
        return PROVIDER.get(plugin);
    }

    /**
     * Cancel all tasks
     */
    void cancelAll();
}
