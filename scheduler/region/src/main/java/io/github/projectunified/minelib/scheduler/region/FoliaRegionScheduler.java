package io.github.projectunified.minelib.scheduler.region;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask;
import org.bukkit.Bukkit;

import java.util.function.BooleanSupplier;

import static io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask.normalizedTicks;
import static io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask.wrapRunnable;

class FoliaRegionScheduler implements RegionScheduler {
    private final Key key;

    FoliaRegionScheduler(Key key) {
        this.key = key;
    }

    @Override
    public Task run(Runnable runnable) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().run(key.plugin, key.world, key.chunkX, key.chunkZ, wrapRunnable(runnable))
        );
    }

    @Override
    public Task runLater(Runnable runnable, long delay) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().runDelayed(key.plugin, key.world, key.chunkX, key.chunkZ, wrapRunnable(runnable), normalizedTicks(delay))
        );
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, long delay, long period) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().runAtFixedRate(key.plugin, key.world, key.chunkX, key.chunkZ, wrapRunnable(runnable), normalizedTicks(delay), normalizedTicks(period))
        );
    }
}
