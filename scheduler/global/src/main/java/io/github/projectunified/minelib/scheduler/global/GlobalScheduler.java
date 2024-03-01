package io.github.projectunified.minelib.scheduler.global;

import io.github.projectunified.minelib.scheduler.common.provider.ObjectProvider;
import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.time.TaskTime;
import io.github.projectunified.minelib.scheduler.common.time.TimerTaskTime;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

public interface GlobalScheduler {
    ObjectProvider<GlobalScheduler> PROVIDER = new ObjectProvider<>(
    ObjectProvider<Plugin, GlobalScheduler> PROVIDER = new ObjectProvider<>(
            ObjectProvider.entry(Platform.FOLIA::isPlatform, FoliaGlobalScheduler::new),
            ObjectProvider.entry(BukkitGlobalScheduler::new)
    );

    static GlobalScheduler get(Plugin plugin) {
        return PROVIDER.get(plugin);
    }

    Task run(Runnable runnable);

    Task runLater(Runnable runnable, TaskTime delay);

    Task runTimer(BooleanSupplier runnable, TimerTaskTime timerTaskTime);

    default Task runTimer(Runnable runnable, TimerTaskTime timerTaskTime) {
        return runTimer(() -> {
            runnable.run();
            return true;
        }, timerTaskTime);
    }
}
