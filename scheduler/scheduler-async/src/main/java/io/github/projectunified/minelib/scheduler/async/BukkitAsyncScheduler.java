package io.github.projectunified.minelib.scheduler.async;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.util.task.BukkitTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

class BukkitAsyncScheduler implements AsyncScheduler {
    private final Plugin plugin;

    BukkitAsyncScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    private static long toTicks(long time, TimeUnit unit) {
        return unit.toMillis(time) / 50;
    }

    @Override
    public Task run(Runnable runnable) {
        return new BukkitTask(
                Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable)
        );
    }

    @Override
    public Task runLater(Runnable runnable, long delay) {
        return new BukkitTask(
                Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay)
        );
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, long delay, long period) {
        return new BukkitTask(
                BukkitTask.wrapRunnable(runnable).runTaskTimerAsynchronously(plugin, delay, period)
        );
    }

    @Override
    public Task runLater(Runnable runnable, long delay, TimeUnit unit) {
        return runLater(runnable, toTicks(delay, unit));
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, long delay, long period, TimeUnit unit) {
        return runTimer(runnable, toTicks(delay, unit), toTicks(period, unit));
    }
}
