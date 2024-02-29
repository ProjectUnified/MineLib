package io.github.projectunified.minelib.scheduler.region;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.time.TaskTime;
import io.github.projectunified.minelib.scheduler.common.time.TimerTaskTime;
import io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

class FoliaRegionScheduler implements RegionScheduler {
    private final Plugin plugin;

    FoliaRegionScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Task run(World world, int chunkX, int chunkZ, Runnable runnable) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().run(plugin, world, chunkX, chunkZ, FoliaTask.wrapRunnable(runnable))
        );
    }

    @Override
    public Task runLater(World world, int chunkX, int chunkZ, Runnable runnable, TaskTime delay) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().runDelayed(plugin, world, chunkX, chunkZ, FoliaTask.wrapRunnable(runnable), delay.getNormalizedTicks())
        );
    }

    @Override
    public Task runTimer(World world, int chunkX, int chunkZ, BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().runAtFixedRate(plugin, world, chunkX, chunkZ, FoliaTask.wrapRunnable(runnable), timerTaskTime.getNormalizedDelayTicks(), timerTaskTime.getNormalizedPeriodTicks())
        );
    }

    @Override
    public Task run(Location location, Runnable runnable) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().run(plugin, location, FoliaTask.wrapRunnable(runnable))
        );
    }

    @Override
    public Task runLater(Location location, Runnable runnable, TaskTime delay) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().runDelayed(plugin, location, FoliaTask.wrapRunnable(runnable), delay.getNormalizedTicks())
        );
    }

    @Override
    public Task runTimer(Location location, BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().runAtFixedRate(plugin, location, FoliaTask.wrapRunnable(runnable), timerTaskTime.getNormalizedDelayTicks(), timerTaskTime.getNormalizedPeriodTicks())
        );
    }
}
