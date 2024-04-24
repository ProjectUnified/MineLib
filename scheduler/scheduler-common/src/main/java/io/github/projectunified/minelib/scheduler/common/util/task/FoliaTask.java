package io.github.projectunified.minelib.scheduler.common.util.task;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * A wrapped {@link Task} for a {@link ScheduledTask}
 */
public class FoliaTask implements Task {
    private final ScheduledTask scheduledTask;

    /**
     * Create a new instance of {@link FoliaTask}
     *
     * @param scheduledTask the {@link ScheduledTask} to wrap
     */
    public FoliaTask(ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    /**
     * Wrap a {@link BooleanSupplier} into a {@link Consumer} of {@link ScheduledTask}
     *
     * @param runnable the runnable to wrap
     * @return the wrapped runnable
     */
    public static Consumer<ScheduledTask> wrapRunnable(BooleanSupplier runnable) {
        return task -> {
            if (!runnable.getAsBoolean()) {
                task.cancel();
            }
        };
    }

    /**
     * Wrap a {@link Runnable} into a {@link Consumer} of {@link ScheduledTask}
     *
     * @param runnable the runnable to wrap
     * @return the wrapped runnable
     */
    public static Consumer<ScheduledTask> wrapRunnable(Runnable runnable) {
        return task -> runnable.run();
    }

    /**
     * Normalize the ticks to be at least 1
     *
     * @param ticks the ticks to normalize
     * @return the normalized ticks
     */
    public static long normalizedTicks(long ticks) {
        return Math.max(1, ticks);
    }

    /**
     * Get the original {@link ScheduledTask}
     *
     * @return the original {@link ScheduledTask}
     */
    public ScheduledTask getScheduledTask() {
        return scheduledTask;
    }

    @Override
    public boolean isCancelled() {
        return scheduledTask.isCancelled();
    }

    @Override
    public boolean isDone() {
        return scheduledTask.getExecutionState() == ScheduledTask.ExecutionState.FINISHED;
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
