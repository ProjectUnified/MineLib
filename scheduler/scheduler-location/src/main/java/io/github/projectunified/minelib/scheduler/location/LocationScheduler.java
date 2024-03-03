package io.github.projectunified.minelib.scheduler.location;

import com.google.common.cache.LoadingCache;
import io.github.projectunified.minelib.scheduler.common.scheduler.Scheduler;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * A {@link Scheduler} that schedules tasks for a {@link Location}
 */
public interface LocationScheduler extends Scheduler {
    LoadingCache<Key, LocationScheduler> PROVIDER = Scheduler.createProvider(
            Platform.FOLIA.isPlatform() ? FoliaLocationScheduler::new : k -> new BukkitLocationScheduler(k.plugin)
    );

    /**
     * Get the {@link LocationScheduler} for the given {@link Plugin} and {@link Location}
     *
     * @param plugin   the plugin
     * @param location the location
     * @return the scheduler
     */
    static LocationScheduler get(Plugin plugin, Location location) {
        try {
            return PROVIDER.get(new Key(plugin, location));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
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
