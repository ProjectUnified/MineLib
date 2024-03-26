package io.github.projectunified.minelib.scheduler.common.scheduler;

import io.github.projectunified.minelib.scheduler.common.task.Task;

import java.util.function.BooleanSupplier;

/**
 * A scheduler that provides a way to run tasks
 */
public interface Scheduler {
    /**
     * Run a task
     *
     * @param runnable the runnable
     * @return the task
     */
    Task run(Runnable runnable);

    /**
     * Run a task later
     *
     * @param runnable the runnable
     * @param delay    the delay in ticks
     * @return the task
     */
    Task runLater(Runnable runnable, long delay);

    /**
     * Run a task timer
     *
     * @param runnable the runnable, returning {@code true} to continue or {@code false} to stop
     * @param delay    the delay in ticks
     * @param period   the period in ticks
     * @return the task
     */
    Task runTimer(BooleanSupplier runnable, long delay, long period);

    /**
     * Run a task repeatedly
     *
     * @param runnable the runnable
     * @param delay    the delay in ticks
     * @param period   the period in ticks
     * @return the task
     */
    default Task runTimer(Runnable runnable, long delay, long period) {
        return runTimer(() -> {
            runnable.run();
            return true;
        }, delay, period);
    }
}
