package io.github.projectunified.minelib.scheduler.async;

import io.github.projectunified.minelib.scheduler.common.provider.ObjectProvider;
import io.github.projectunified.minelib.scheduler.common.scheduler.Scheduler;
import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

public interface AsyncScheduler extends Scheduler {
    ObjectProvider<Plugin, AsyncScheduler> PROVIDER = new ObjectProvider<>(
            ObjectProvider.entry(Platform.FOLIA::isPlatform, FoliaAsyncScheduler::new),
            ObjectProvider.entry(BukkitAsyncScheduler::new)
    );

    static AsyncScheduler get(Plugin plugin) {
        return PROVIDER.get(plugin);
    }

    Task runLater(Runnable runnable, long delay, TimeUnit unit);

    Task runTimer(BooleanSupplier runnable, long delay, long period, TimeUnit unit);

    default Task runTimer(Runnable runnable, long delay, long period, TimeUnit unit) {
        return runTimer(() -> {
            runnable.run();
            return true;
        }, delay, period, unit);
    }
}
