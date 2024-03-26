package io.github.projectunified.minelib.scheduler.global;

import io.github.projectunified.minelib.scheduler.common.scheduler.Scheduler;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.plugin.Plugin;

/**
 * A {@link Scheduler} that can run tasks globally
 */
public interface GlobalScheduler extends Scheduler {
    /**
     * Get the {@link GlobalScheduler} for the given plugin
     *
     * @param plugin the plugin
     * @return the scheduler
     */
    static GlobalScheduler get(Plugin plugin) {
        return Platform.FOLIA.isPlatform() ? new FoliaGlobalScheduler(plugin) : new BukkitGlobalScheduler(plugin);
    }
}
