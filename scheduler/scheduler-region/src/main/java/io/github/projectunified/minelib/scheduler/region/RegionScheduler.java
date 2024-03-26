package io.github.projectunified.minelib.scheduler.region;

import io.github.projectunified.minelib.scheduler.common.scheduler.Scheduler;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

/**
 * A {@link Scheduler} that schedules tasks for a region
 */
public interface RegionScheduler extends Scheduler {
    /**
     * Get the {@link RegionScheduler} for the given {@link Plugin}, {@link World}, and chunk coordinates
     *
     * @param plugin the plugin
     * @param world  the world
     * @param chunkX the X coordinate of the chunk
     * @param chunkZ the Z coordinate of the chunk
     * @return the scheduler
     */
    static RegionScheduler get(Plugin plugin, World world, int chunkX, int chunkZ) {
        return Platform.FOLIA.isPlatform()
                ? new FoliaRegionScheduler(plugin, world, chunkX, chunkZ)
                : new BukkitRegionScheduler(plugin);
    }

    /**
     * A key for a {@link RegionScheduler}
     */
    class Key {
        public final Plugin plugin;
        public final World world;
        public final int chunkX;
        public final int chunkZ;

        public Key(Plugin plugin, World world, int chunkX, int chunkZ) {
            this.plugin = plugin;
            this.world = world;
            this.chunkX = chunkX;
            this.chunkZ = chunkZ;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return chunkX == key.chunkX && chunkZ == key.chunkZ && Objects.equals(plugin, key.plugin) && Objects.equals(world, key.world);
        }

        @Override
        public int hashCode() {
            return Objects.hash(plugin, world, chunkX, chunkZ);
        }
    }
}
