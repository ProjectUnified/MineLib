package io.github.projectunified.minelib.scheduler.location;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.time.TaskTime;
import io.github.projectunified.minelib.scheduler.common.time.TimerTaskTime;
import io.github.projectunified.minelib.scheduler.common.util.task.BukkitTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

class BukkitLocationScheduler implements LocationScheduler {
    private final Plugin plugin;

    BukkitLocationScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Task run(Runnable runnable) {
        return new BukkitTask(
                Bukkit.getScheduler().runTask(plugin, runnable)
        );
    }

    @Override
    public Task runLater(Runnable runnable, TaskTime delay) {
        return new BukkitTask(
                Bukkit.getScheduler().runTaskLater(plugin, runnable, delay.getTicks())
        );
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
        return new BukkitTask(
                BukkitTask.wrapRunnable(runnable).runTaskTimer(plugin, timerTaskTime.getDelayTicks(), timerTaskTime.getPeriodTicks())
        );
    }
}
