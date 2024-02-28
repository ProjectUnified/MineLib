package io.github.projectunified.minelib.scheduler.common.util.task;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;

public class FoliaTask implements Task {
    private final ScheduledTask scheduledTask;
    private final boolean async;

    public FoliaTask(ScheduledTask scheduledTask, boolean async) {
        this.scheduledTask = scheduledTask;
        this.async = async;
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
    public boolean isAsync() {
        return async;
    }

    @Override
    public boolean isRepeating() {
        return scheduledTask.isRepeatingTask();
    }

    @Override
    public Plugin getPlugin() {
        return scheduledTask.getOwningPlugin();
    }
}
