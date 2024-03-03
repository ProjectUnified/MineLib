package io.github.projectunified.minelib.scheduler.global;

import com.google.common.cache.LoadingCache;
import io.github.projectunified.minelib.scheduler.common.scheduler.Scheduler;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.ExecutionException;

/**
 * A {@link Scheduler} that can run tasks globally
 */
public interface GlobalScheduler extends Scheduler {
    LoadingCache<Plugin, GlobalScheduler> PROVIDER = Scheduler.createProvider(
            Platform.FOLIA.isPlatform() ? FoliaGlobalScheduler::new : BukkitGlobalScheduler::new
    );

    /**
     * Get the {@link GlobalScheduler} for the given plugin
     *
     * @param plugin the plugin
     * @return the scheduler
     */
    static GlobalScheduler get(Plugin plugin) {
        try {
            return PROVIDER.get(plugin);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
