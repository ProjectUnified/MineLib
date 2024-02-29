package io.github.projectunified.minelib.scheduler.global;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.time.TaskTime;
import io.github.projectunified.minelib.scheduler.common.time.TimerTaskTime;
import io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

class FoliaGlobalScheduler implements GlobalScheduler {
    private final Plugin plugin;

    FoliaGlobalScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Task run(Runnable runnable) {
        return new FoliaTask(
                Bukkit.getGlobalRegionScheduler().run(plugin, FoliaTask.wrapRunnable(runnable))
        );
    }

    @Override
    public Task runLater(Runnable runnable, TaskTime delay) {
        return new FoliaTask(
                Bukkit.getGlobalRegionScheduler().runDelayed(plugin, FoliaTask.wrapRunnable(runnable), delay.getNormalizedTicks())
        );
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, TimerTaskTime timerTaskTime) {
        return new FoliaTask(
                Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, FoliaTask.wrapRunnable(runnable), timerTaskTime.getNormalizedDelayTicks(), timerTaskTime.getNormalizedPeriodTicks())
        );
    }
}
