package io.github.projectunified.minelib.scheduler.entity;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;

import java.util.function.BooleanSupplier;

import static io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask.normalizedTicks;
import static io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask.wrapRunnable;

class FoliaEntityScheduler implements EntityScheduler {
    private final Key key;

    FoliaEntityScheduler(Key key) {
        this.key = key;
    }

    @Override
    public Task run(Runnable runnable, Runnable retired) {
        ScheduledTask scheduledTask;
        if (key.isEntityValid()) {
            scheduledTask = key.entity.getScheduler().run(key.plugin, wrapRunnable(runnable), retired);
        } else {
            scheduledTask = Bukkit.getGlobalRegionScheduler().run(key.plugin, wrapRunnable(retired));
        }
        return new FoliaTask(scheduledTask);
    }

    @Override
    public Task runLater(Runnable runnable, Runnable retired, long delay) {
        ScheduledTask scheduledTask;
        if (key.isEntityValid()) {
            scheduledTask = key.entity.getScheduler().runDelayed(key.plugin, wrapRunnable(runnable), retired, normalizedTicks(delay));
        } else {
            scheduledTask = Bukkit.getGlobalRegionScheduler().runDelayed(key.plugin, wrapRunnable(retired), normalizedTicks(delay));
        }
        return new FoliaTask(scheduledTask);
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, Runnable retired, long delay, long period) {
        ScheduledTask scheduledTask;
        if (key.isEntityValid()) {
            scheduledTask = key.entity.getScheduler().runAtFixedRate(key.plugin, wrapRunnable(runnable), retired, normalizedTicks(delay), normalizedTicks(period));
        } else {
            scheduledTask = Bukkit.getGlobalRegionScheduler().runDelayed(key.plugin, wrapRunnable(retired), normalizedTicks(delay));
        }
        return new FoliaTask(scheduledTask);
    }
}
