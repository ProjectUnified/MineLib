package io.github.projectunified.minelib.scheduler.global;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

import static io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask.normalizedTicks;
import static io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask.wrapRunnable;

class FoliaGlobalScheduler implements GlobalScheduler {
    private final Plugin plugin;

    FoliaGlobalScheduler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Task run(Runnable runnable) {
        return new FoliaTask(
                Bukkit.getGlobalRegionScheduler().run(plugin, wrapRunnable(runnable))
        );
    }

    @Override
    public Task runLater(Runnable runnable, long delay) {
        return new FoliaTask(
                Bukkit.getGlobalRegionScheduler().runDelayed(plugin, wrapRunnable(runnable), normalizedTicks(delay))
        );
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, long delay, long period) {
        return new FoliaTask(
                Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, wrapRunnable(runnable), normalizedTicks(delay), normalizedTicks(period))
        );
    }
}
