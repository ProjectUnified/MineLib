package io.github.projectunified.minelib.scheduler.region;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

import static io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask.normalizedTicks;
import static io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask.wrapRunnable;

class FoliaRegionScheduler implements RegionScheduler {
    private final Plugin plugin;
    private final World world;
    private final int chunkX;
    private final int chunkZ;

    FoliaRegionScheduler(Plugin plugin, World world, int chunkX, int chunkZ) {
        this.plugin = plugin;
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    @Override
    public Task run(Runnable runnable) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().run(plugin, world, chunkX, chunkZ, wrapRunnable(runnable))
        );
    }

    @Override
    public Task runLater(Runnable runnable, long delay) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().runDelayed(plugin, world, chunkX, chunkZ, wrapRunnable(runnable), normalizedTicks(delay))
        );
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, long delay, long period) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().runAtFixedRate(plugin, world, chunkX, chunkZ, wrapRunnable(runnable), normalizedTicks(delay), normalizedTicks(period))
        );
    }
}
