package io.github.projectunified.minelib.scheduler.async;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.time.TaskTime;
import io.github.projectunified.minelib.scheduler.common.time.TimerTaskTime;
import io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

class FoliaAsyncScheduler implements AsyncScheduler {
    private final Plugin plugin;

    FoliaAsyncScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Task run(Runnable runnable) {
        return new FoliaTask(
                Bukkit.getAsyncScheduler().runNow(plugin, FoliaTask.wrapRunnable(runnable)),
                true
        );
    }

    @Override
    public Task runLater(Runnable runnable, TaskTime delay) {
        return new FoliaTask(
                Bukkit.getAsyncScheduler().runDelayed(plugin, FoliaTask.wrapRunnable(runnable), delay.getTime(), delay.getUnit()),
                true
        );
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
        return new FoliaTask(
                Bukkit.getAsyncScheduler().runAtFixedRate(plugin, FoliaTask.wrapRunnable(runnable), timerTaskTime.getNormalizedDelay(), timerTaskTime.getNormalizedPeriod(), timerTaskTime.getUnit()),
                true
        );
    }
}
