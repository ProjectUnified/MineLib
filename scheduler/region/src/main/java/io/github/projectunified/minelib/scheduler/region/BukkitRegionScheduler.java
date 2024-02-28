package io.github.projectunified.minelib.scheduler.region;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.time.TaskTime;
import io.github.projectunified.minelib.scheduler.common.time.TimerTaskTime;
import io.github.projectunified.minelib.scheduler.common.util.task.BukkitTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

class BukkitRegionScheduler implements RegionScheduler {
    private final Plugin plugin;

    BukkitRegionScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Task run(World world, int chunkX, int chunkZ, Runnable runnable) {
        return new BukkitTask(
                Bukkit.getScheduler().runTask(plugin, runnable),
                false
        );
    }

    @Override
    public Task runLater(World world, int chunkX, int chunkZ, Runnable runnable, TaskTime delay) {
        return new BukkitTask(
                Bukkit.getScheduler().runTaskLater(plugin, runnable, delay.getTicks()),
                false
        );
    }

    @Override
    public Task runTimer(World world, int chunkX, int chunkZ, BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
        return new BukkitTask(
                BukkitTask.wrapRunnable(runnable).runTaskTimer(plugin, timerTaskTime.getDelayTicks(), timerTaskTime.getPeriodTicks()),
                true
        );
    }

    @Override
    public Task run(Location location, Runnable runnable) {
        return new BukkitTask(
                Bukkit.getScheduler().runTask(plugin, runnable),
                false
        );
    }

    @Override
    public Task runLater(Location location, Runnable runnable, TaskTime delay) {
        return new BukkitTask(
                Bukkit.getScheduler().runTaskLater(plugin, runnable, delay.getTicks()),
                false
        );
    }

    @Override
    public Task runTimer(Location location, BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
        return new BukkitTask(
                BukkitTask.wrapRunnable(runnable).runTaskTimer(plugin, timerTaskTime.getDelayTicks(), timerTaskTime.getPeriodTicks()),
                true
        );
    }
}
