package io.github.projectunified.minelib.scheduler.location;

import io.github.projectunified.minelib.scheduler.common.provider.ObjectProvider;
import io.github.projectunified.minelib.scheduler.common.scheduler.Scheduler;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

/**
 * A {@link Scheduler} that schedules tasks for a {@link Location}
 */
public interface LocationScheduler extends Scheduler {
    ObjectProvider<Key, LocationScheduler> PROVIDER = new ObjectProvider<>(
            ObjectProvider.entry(Platform.FOLIA::isPlatform, FoliaLocationScheduler::new),
            ObjectProvider.entry(key -> new BukkitLocationScheduler(key.plugin))
    );

    /**
     * Get the {@link LocationScheduler} for the given {@link Plugin} and {@link Location}
     *
     * @param plugin   the plugin
     * @param location the location
     * @return the scheduler
     */
    static LocationScheduler get(Plugin plugin, Location location) {
        return PROVIDER.get(new Key(plugin, location));
    }

    /**
     * A key for a {@link LocationScheduler}
     */
    class Key {
        public final Plugin plugin;
        public final Location location;

        public Key(Plugin plugin, Location location) {
            this.plugin = plugin;
            this.location = location;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(plugin, key.plugin) && Objects.equals(location, key.location);
        }

        @Override
        public int hashCode() {
            return Objects.hash(plugin, location);
        }
    }
}
