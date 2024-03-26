package io.github.projectunified.minelib.scheduler.location;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

import static io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask.normalizedTicks;
import static io.github.projectunified.minelib.scheduler.common.util.task.FoliaTask.wrapRunnable;

class FoliaLocationScheduler implements LocationScheduler {
    private final Plugin plugin;
    private final Location location;

    FoliaLocationScheduler(Plugin plugin, Location location) {
        this.plugin = plugin;
        this.location = location;
    }

    @Override
    public Task run(Runnable runnable) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().run(plugin, location, wrapRunnable(runnable))
        );
    }

    @Override
    public Task runLater(Runnable runnable, long delay) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().runDelayed(plugin, location, wrapRunnable(runnable), normalizedTicks(delay))
        );
    }

    @Override
    public Task runTimer(BooleanSupplier runnable, long delay, long period) {
        return new FoliaTask(
                Bukkit.getRegionScheduler().runAtFixedRate(plugin, location, wrapRunnable(runnable), normalizedTicks(delay), normalizedTicks(period))
        );
    }
}
