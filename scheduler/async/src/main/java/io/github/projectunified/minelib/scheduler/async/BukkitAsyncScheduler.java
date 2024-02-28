package io.github.projectunified.minelib.scheduler.async;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.time.TaskTime;
import io.github.projectunified.minelib.scheduler.common.time.TimerTaskTime;
import io.github.projectunified.minelib.scheduler.common.util.task.BukkitTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

class BukkitAsyncScheduler implements AsyncScheduler {
    private final Plugin plugin;

    BukkitAsyncScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Task run(Runnable runnable) {
        return new BukkitTask(
                Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable),
                false
        );
    }

    @Override
    public Task runLater(Runnable runnable, TaskTime delay) {
        return new BukkitTask(
                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay.getTicks()),
                false
        );
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
        return new BukkitTask(
                BukkitTask.wrapRunnable(runnable).runTaskTimerAsynchronously(plugin, timerTaskTime.getDelayTicks(), timerTaskTime.getPeriodTicks()),
                true
        );
    }
}
