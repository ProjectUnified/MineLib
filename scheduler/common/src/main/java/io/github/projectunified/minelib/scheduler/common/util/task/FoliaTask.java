package io.github.projectunified.minelib.scheduler.common.util.task;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class FoliaTask implements Task {
    private final ScheduledTask scheduledTask;

    public FoliaTask(ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    public static Consumer<ScheduledTask> wrapRunnable(BooleanSupplier runnable) {
        return task -> {
            if (!runnable.getAsBoolean()) {
                task.cancel();
            }
        };
    }

    public static Consumer<ScheduledTask> wrapRunnable(Runnable runnable) {
        return task -> runnable.run();
    }

    public ScheduledTask getScheduledTask() {
        return scheduledTask;
    }

    @Override
    public boolean isCancelled() {
        return scheduledTask.isCancelled();
    }

    @Override
    public void cancel() {
        scheduledTask.cancel();
    }

    @Override
    public Plugin getPlugin() {
        return scheduledTask.getOwningPlugin();
    }
}
