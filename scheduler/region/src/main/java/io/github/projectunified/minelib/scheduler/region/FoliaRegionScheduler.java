package io.github.projectunified.minelib.scheduler.region;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.time.TaskTime;
import io.github.projectunified.minelib.scheduler.common.time.TimerTaskTime;
import io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask;
import org.bukkit.Bukkit;

import java.util.function.BooleanSupplier;

class FoliaRegionScheduler implements RegionScheduler {
    private final Key key;

    FoliaRegionScheduler(Key key) {
        this.key = key;
    }

    @Override
    public Task run(Runnable runnable) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().run(key.plugin, key.world, key.chunkX, key.chunkZ, FoliaTask.wrapRunnable(runnable))
        );
    }

    @Override
    public Task runLater(Runnable runnable, TaskTime delay) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().runDelayed(key.plugin, key.world, key.chunkX, key.chunkZ, FoliaTask.wrapRunnable(runnable), delay.getNormalizedTicks())
        );
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().runAtFixedRate(key.plugin, key.world, key.chunkX, key.chunkZ, FoliaTask.wrapRunnable(runnable), timerTaskTime.getNormalizedDelayTicks(), timerTaskTime.getNormalizedPeriodTicks())
        );
    }
}
