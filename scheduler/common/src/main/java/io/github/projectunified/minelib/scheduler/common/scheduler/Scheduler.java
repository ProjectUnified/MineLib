package io.github.projectunified.minelib.scheduler.common.scheduler;

import io.github.projectunified.minelib.scheduler.common.task.Task;

import java.util.function.BooleanSupplier;

public interface Scheduler {
    Task run(Runnable runnable);

    Task runLater(Runnable runnable, long delay);

    Task runTimer(BooleanSupplier runnable, long delay, long period);

    default Task runTimer(Runnable runnable, long delay, long period) {
        return runTimer(() -> {
            runnable.run();
            return true;
        }, delay, period);
    }
}
