package io.github.projectunified.minelib.scheduler.entity;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

import static io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask.normalizedTicks;
import static io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask.wrapRunnable;

class FoliaEntityScheduler implements EntityScheduler {
    private final Plugin plugin;
    private final Entity entity;

    FoliaEntityScheduler(Plugin plugin, Entity entity) {
        this.plugin = plugin;
        this.entity = entity;
    }

    @Override
    public Task run(Runnable runnable, Runnable retired) {
        ScheduledTask scheduledTask;
        if (EntityScheduler.isEntityValid(entity)) {
            scheduledTask = entity.getScheduler().run(plugin, wrapRunnable(runnable), retired);
        } else {
            scheduledTask = Bukkit.getGlobalRegionScheduler().run(plugin, wrapRunnable(retired));
        }
        return new FoliaTask(scheduledTask);
    }

    @Override
    public Task runLater(Runnable runnable, Runnable retired, long delay) {
        ScheduledTask scheduledTask;
        if (EntityScheduler.isEntityValid(entity)) {
            scheduledTask = entity.getScheduler().runDelayed(plugin, wrapRunnable(runnable), retired, normalizedTicks(delay));
        } else {
            scheduledTask = Bukkit.getGlobalRegionScheduler().runDelayed(plugin, wrapRunnable(retired), normalizedTicks(delay));
        }
        return new FoliaTask(scheduledTask);
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, Runnable retired, long delay, long period) {
        ScheduledTask scheduledTask;
        if (EntityScheduler.isEntityValid(entity)) {
            scheduledTask = entity.getScheduler().runAtFixedRate(plugin, wrapRunnable(runnable), retired, normalizedTicks(delay), normalizedTicks(period));
        } else {
            scheduledTask = Bukkit.getGlobalRegionScheduler().runDelayed(plugin, wrapRunnable(retired), normalizedTicks(delay));
        }
        return new FoliaTask(scheduledTask);
    }
}
