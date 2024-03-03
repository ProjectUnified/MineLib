package io.github.projectunified.minelib.scheduler.canceller;

import com.google.common.cache.LoadingCache;
import io.github.projectunified.minelib.scheduler.common.scheduler.Scheduler;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.ExecutionException;

/**
 * An interface for cancelling tasks
 */
public interface TaskCanceller {
    LoadingCache<Plugin, TaskCanceller> PROVIDER = Scheduler.createProvider(
            Platform.FOLIA.isPlatform() ? FoliaTaskCanceller::new : BukkitTaskCanceller::new
    );

    /**
     * Get the {@link TaskCanceller} for the given plugin
     *
     * @param plugin the plugin
     * @return the {@link TaskCanceller}
     */
    static TaskCanceller get(Plugin plugin) {
        try {
            return PROVIDER.get(plugin);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Cancel all tasks
     */
    void cancelAll();
}
