package io.github.projectunified.minelib.scheduler.global;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.time.TaskTime;
import io.github.projectunified.minelib.scheduler.common.time.TimerTaskTime;
import io.github.projectunified.minelib.scheduler.common.util.task.BukkitTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

class BukkitGlobalScheduler implements GlobalScheduler {
    private final Plugin plugin;

    BukkitGlobalScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Task run(Runnable runnable) {
        return new BukkitTask(
                Bukkit.getScheduler().runTask(plugin, runnable),
                false
        );
    }

    @Override
    public Task runLater(Runnable runnable, TaskTime delay) {
        return new BukkitTask(
                Bukkit.getScheduler().runTaskLater(plugin, runnable, delay.getTicks()),
                false
        );
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
        return new BukkitTask(
                BukkitTask.wrapRunnable(runnable).runTaskTimer(plugin, timerTaskTime.getDelayTicks(), timerTaskTime.getPeriodTicks()),
                true
        );
    }
}
