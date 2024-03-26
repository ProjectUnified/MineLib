package io.github.projectunified.minelib.scheduler.entity;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.util.task.BukkitTask;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.BooleanSupplier;

class BukkitEntityScheduler implements EntityScheduler {
    private final Plugin plugin;
    private final Entity entity;

    BukkitEntityScheduler(Plugin plugin, Entity entity) {
        this.plugin = plugin;
        this.entity = entity;
    }

    private BukkitRunnable wrapRunnable(BooleanSupplier runnable, Runnable retired) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (EntityScheduler.isEntityValid(entity)) {
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

    private BukkitRunnable wrapRunnable(Runnable runnable, Runnable retired) {
        return wrapRunnable(() -> {
            runnable.run();
            return true;
        }, retired);
    }

    @Override
    public Task run(Runnable runnable, Runnable retired) {
        return new BukkitTask(
                wrapRunnable(runnable, retired).runTask(plugin)
        );
    }

    @Override
    public Task runLater(Runnable runnable, Runnable retired, long delay) {
        return new BukkitTask(
                wrapRunnable(runnable, retired).runTaskLater(plugin, delay)
        );
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, Runnable retired, long delay, long period) {
        return new BukkitTask(
                wrapRunnable(runnable, retired).runTaskTimer(plugin, delay, period)
        );
    }
}
