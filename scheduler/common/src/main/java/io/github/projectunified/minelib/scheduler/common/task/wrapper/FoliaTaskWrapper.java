package io.github.projectunified.minelib.scheduler.common.task.wrapper;

import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;

public interface FoliaTaskWrapper {
    static Task wrap(ScheduledTask scheduledTask, boolean async) {
        return new Task() {
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
        };
    }
}
