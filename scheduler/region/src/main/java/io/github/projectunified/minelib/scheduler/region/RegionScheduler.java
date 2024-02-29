package io.github.projectunified.minelib.scheduler.region;

import io.github.projectunified.minelib.scheduler.common.provider.ObjectProvider;
import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.time.TaskTime;
import io.github.projectunified.minelib.scheduler.common.time.TimerTaskTime;
import io.github.projectunified.minelib.scheduler.common.util.PlatformChecker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

public interface RegionScheduler {
    ObjectProvider<RegionScheduler> PROVIDER = new ObjectProvider<>(
            ObjectProvider.entry(PlatformChecker::isFolia, FoliaRegionScheduler::new),
            ObjectProvider.entry(BukkitRegionScheduler::new)
    );

    static RegionScheduler get(Plugin plugin) {
        return PROVIDER.get(plugin);
    }

    Task run(World world, int chunkX, int chunkZ, Runnable runnable);

    Task runLater(World world, int chunkX, int chunkZ, Runnable runnable, TaskTime delay);

    Task runTimer(World world, int chunkX, int chunkZ, BooleanSupplier runnable, TimerTaskTime timerTaskTime);

    default Task runTimer(World world, int chunkX, int chunkZ, Runnable runnable, TimerTaskTime timerTaskTime) {
        return runTimer(world, chunkX, chunkZ, () -> {
            runnable.run();
            return true;
        }, timerTaskTime);
    }

    Task run(Location location, Runnable runnable);

    Task runLater(Location location, Runnable runnable, TaskTime delay);

    Task runTimer(Location location, BooleanSupplier runnable, TimerTaskTime timerTaskTime);

    default Task runTimer(Location location, Runnable runnable, TimerTaskTime timerTaskTime) {
        return runTimer(location, () -> {
            runnable.run();
            return true;
        }, timerTaskTime);
    }
}
