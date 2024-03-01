package io.github.projectunified.minelib.scheduler.region;

import io.github.projectunified.minelib.scheduler.common.provider.ObjectProvider;
import io.github.projectunified.minelib.scheduler.common.scheduler.Scheduler;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public interface RegionScheduler extends Scheduler {
    ObjectProvider<Key, RegionScheduler> PROVIDER = new ObjectProvider<>(
            ObjectProvider.entry(Platform.FOLIA::isPlatform, FoliaRegionScheduler::new),
            ObjectProvider.entry(key -> new BukkitRegionScheduler(key.plugin))
    );

    static RegionScheduler get(Plugin plugin, World world, int chunkX, int chunkZ) {
        return PROVIDER.get(new Key(plugin, world, chunkX, chunkZ));
    }

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
