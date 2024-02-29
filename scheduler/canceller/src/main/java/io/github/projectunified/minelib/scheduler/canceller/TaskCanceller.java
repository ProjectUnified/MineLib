package io.github.projectunified.minelib.scheduler.canceller;

import io.github.projectunified.minelib.scheduler.common.provider.ObjectProvider;
import io.github.projectunified.minelib.scheduler.common.util.PlatformChecker;
import org.bukkit.plugin.Plugin;

public interface TaskCanceller {
    ObjectProvider<TaskCanceller> PROVIDER = new ObjectProvider<>(
            ObjectProvider.entry(PlatformChecker::isFolia, FoliaTaskCanceller::new),
            ObjectProvider.entry(BukkitTaskCanceller::new)
    );

    static TaskCanceller get(Plugin plugin) {
        return PROVIDER.get(plugin);
    }

    void cancelAll();
}
