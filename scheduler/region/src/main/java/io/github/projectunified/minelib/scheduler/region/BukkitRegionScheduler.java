package io.github.projectunified.minelib.scheduler.region;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.util.task.BukkitTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

class BukkitRegionScheduler implements RegionScheduler {
    private final Plugin plugin;

    BukkitRegionScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Task run(Runnable runnable) {
        return new BukkitTask(
                Bukkit.getScheduler().runTask(plugin, runnable)
        );
    }

    @Override
    public Task runLater(Runnable runnable, long delay) {
        return new BukkitTask(
                Bukkit.getScheduler().runTaskLater(plugin, runnable, delay)
        );
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, long delay, long period) {
        return new BukkitTask(
                BukkitTask.wrapRunnable(runnable).runTaskTimer(plugin, delay, period)
        );
    }
}
