package io.github.projectunified.minelib.scheduler.entity;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.time.TaskTime;
import io.github.projectunified.minelib.scheduler.common.time.TimerTaskTime;
import io.github.projectunified.minelib.scheduler.common.util.task.BukkitTask;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.BooleanSupplier;

class BukkitEntityScheduler implements EntityScheduler {
    private final Plugin plugin;

    BukkitEntityScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    private BukkitRunnable wrapRunnable(Entity entity, BooleanSupplier runnable, Runnable retired) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (isEntityValid(entity)) {
                    if (!runnable.getAsBoolean()) {
                        cancel();
                    }
                } else {
                    retired.run();
                    cancel();
                }
            }
        };
    }

    private BukkitRunnable wrapRunnable(Entity entity, Runnable runnable, Runnable retired) {
        return wrapRunnable(entity, () -> {
            runnable.run();
            return true;
        }, retired);
    }

    @Override
    public Task run(Entity entity, Runnable runnable, Runnable retired) {
        return new BukkitTask(
                wrapRunnable(entity, runnable, retired).runTask(plugin),
                false
        );
    }

    @Override
    public Task runLater(Entity entity, Runnable runnable, Runnable retired, TaskTime delay) {
        return new BukkitTask(
                wrapRunnable(entity, runnable, retired).runTaskLater(plugin, delay.getTicks()),
                false
        );
    }

    @Override
    public Task runTimer(Entity entity, BooleanSupplier runnable, Runnable retired, TimerTaskTime timerTaskTime) {
        return new BukkitTask(
                wrapRunnable(entity, runnable, retired).runTaskTimer(plugin, timerTaskTime.getDelayTicks(), timerTaskTime.getPeriodTicks()),
                true
        );
    }
}
