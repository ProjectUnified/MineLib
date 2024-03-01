package io.github.projectunified.minelib.scheduler.entity;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.time.TaskTime;
import io.github.projectunified.minelib.scheduler.common.time.TimerTaskTime;
import io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;

import java.util.function.BooleanSupplier;

class FoliaEntityScheduler implements EntityScheduler {
    private final Key key;

    FoliaEntityScheduler(Key key) {
        this.key = key;
    }

    @Override
    public Task run(Runnable runnable, Runnable retired) {
        ScheduledTask scheduledTask;
        if (key.isEntityValid()) {
            scheduledTask = key.entity.getScheduler().run(key.plugin, FoliaTask.wrapRunnable(runnable), retired);
        } else {
            scheduledTask = Bukkit.getGlobalRegionScheduler().run(key.plugin, FoliaTask.wrapRunnable(retired));
        }
        return new FoliaTask(scheduledTask);
    }

    @Override
    public Task runLater(Runnable runnable, Runnable retired, TaskTime delay) {
        ScheduledTask scheduledTask;
        if (key.isEntityValid()) {
            scheduledTask = key.entity.getScheduler().runDelayed(key.plugin, FoliaTask.wrapRunnable(runnable), retired, delay.getNormalizedTicks());
        } else {
            scheduledTask = Bukkit.getGlobalRegionScheduler().runDelayed(key.plugin, FoliaTask.wrapRunnable(retired), delay.getNormalizedTicks());
        }
        return new FoliaTask(scheduledTask);
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, Runnable retired, TimerTaskTime timerTaskTime) {
        ScheduledTask scheduledTask;
        if (key.isEntityValid()) {
            scheduledTask = key.entity.getScheduler().runAtFixedRate(key.plugin, FoliaTask.wrapRunnable(runnable), retired, timerTaskTime.getNormalizedDelayTicks(), timerTaskTime.getNormalizedPeriodTicks());
        } else {
            scheduledTask = Bukkit.getGlobalRegionScheduler().runDelayed(key.plugin, FoliaTask.wrapRunnable(retired), timerTaskTime.getNormalizedDelayTicks());
        }
        return new FoliaTask(scheduledTask);
    }
}
