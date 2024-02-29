package io.github.projectunified.minelib.scheduler.entity;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.time.TaskTime;
import io.github.projectunified.minelib.scheduler.common.time.TimerTaskTime;
import io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

class FoliaEntityScheduler implements EntityScheduler {
    private final Plugin plugin;

    FoliaEntityScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Task run(Entity entity, Runnable runnable, Runnable retired) {
        ScheduledTask scheduledTask;
        if (isEntityValid(entity)) {
            scheduledTask = entity.getScheduler().run(plugin, FoliaTask.wrapRunnable(runnable), retired);
        } else {
            scheduledTask = Bukkit.getGlobalRegionScheduler().run(plugin, FoliaTask.wrapRunnable(retired));
        }
        return new FoliaTask(scheduledTask);
    }

    @Override
    public Task runLater(Entity entity, Runnable runnable, Runnable retired, TaskTime delay) {
        ScheduledTask scheduledTask;
        if (isEntityValid(entity)) {
            scheduledTask = entity.getScheduler().runDelayed(plugin, FoliaTask.wrapRunnable(runnable), retired, delay.getNormalizedTicks());
        } else {
            scheduledTask = Bukkit.getGlobalRegionScheduler().runDelayed(plugin, FoliaTask.wrapRunnable(retired), delay.getNormalizedTicks());
        }
        return new FoliaTask(scheduledTask);
    }

    @Override
    public Task runTimer(Entity entity, BooleanSupplier runnable, Runnable retired, TimerTaskTime timerTaskTime) {
        ScheduledTask scheduledTask;
        if (isEntityValid(entity)) {
            scheduledTask = entity.getScheduler().runAtFixedRate(plugin, FoliaTask.wrapRunnable(runnable), retired, timerTaskTime.getNormalizedDelayTicks(), timerTaskTime.getNormalizedPeriodTicks());
        } else {
            scheduledTask = Bukkit.getGlobalRegionScheduler().runDelayed(plugin, FoliaTask.wrapRunnable(retired), timerTaskTime.getNormalizedDelayTicks());
        }
        return new FoliaTask(scheduledTask);
    }
}
