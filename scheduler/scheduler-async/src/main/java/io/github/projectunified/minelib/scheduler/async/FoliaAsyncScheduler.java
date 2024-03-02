package io.github.projectunified.minelib.scheduler.async;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import static io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask.normalizedTicks;
import static io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask.wrapRunnable;

class FoliaAsyncScheduler implements AsyncScheduler {
    private final Plugin plugin;

    FoliaAsyncScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    private static long toMillis(long ticks) {
        return normalizedTicks(ticks) * 50;
    }

    @Override
    public Task run(Runnable runnable) {
        return new FoliaTask(
                Bukkit.getAsyncScheduler().runNow(plugin, wrapRunnable(runnable))
        );
    }

    @Override
    public Task runLater(Runnable runnable, long delay, TimeUnit unit) {
        return new FoliaTask(
                Bukkit.getAsyncScheduler().runDelayed(plugin, wrapRunnable(runnable), delay, unit)
        );
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, long delay, long period, TimeUnit unit) {
        return new FoliaTask(
                Bukkit.getAsyncScheduler().runAtFixedRate(plugin, wrapRunnable(runnable), delay, period, unit)
        );
    }

    @Override
    public Task runLater(Runnable runnable, long delay) {
        return runLater(runnable, toMillis(delay), TimeUnit.MILLISECONDS);
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, long delay, long period) {
        return runTimer(runnable, toMillis(delay), toMillis(period), TimeUnit.MILLISECONDS);
    }
}
