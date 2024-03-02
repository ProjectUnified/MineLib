package io.github.projectunified.minelib.scheduler.entity;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.util.task.BukkitTask;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.BooleanSupplier;

class BukkitEntityScheduler implements EntityScheduler {
    private final Key key;

    BukkitEntityScheduler(Key key) {
        this.key = key;
    }

    private BukkitRunnable wrapRunnable(BooleanSupplier runnable, Runnable retired) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (key.isEntityValid()) {
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
                wrapRunnable(runnable, retired).runTask(key.plugin)
        );
    }

    @Override
    public Task runLater(Runnable runnable, Runnable retired, long delay) {
        return new BukkitTask(
                wrapRunnable(runnable, retired).runTaskLater(key.plugin, delay)
        );
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, Runnable retired, long delay, long period) {
        return new BukkitTask(
                wrapRunnable(runnable, retired).runTaskTimer(key.plugin, delay, period)
        );
    }
}
