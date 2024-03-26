package io.github.projectunified.minelib.scheduler.location;

import io.github.projectunified.minelib.scheduler.common.scheduler.Scheduler;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

/**
 * A {@link Scheduler} that schedules tasks for a {@link Location}
 */
public interface LocationScheduler extends Scheduler {
    /**
     * Get the {@link LocationScheduler} for the given {@link Plugin} and {@link Location}
     *
     * @param plugin   the plugin
     * @param location the location
     * @return the scheduler
     */
    static LocationScheduler get(Plugin plugin, Location location) {
        return Platform.FOLIA.isPlatform()
                ? new FoliaLocationScheduler(plugin, location)
                : new BukkitLocationScheduler(plugin);
    }
}
