package io.github.projectunified.minelib.scheduler.canceller;

import io.github.projectunified.minelib.scheduler.common.provider.ObjectProvider;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.plugin.Plugin;

public interface TaskCanceller {
    ObjectProvider<Plugin, TaskCanceller> PROVIDER = new ObjectProvider<>(
            ObjectProvider.entry(Platform.FOLIA::isPlatform, FoliaTaskCanceller::new),
            ObjectProvider.entry(BukkitTaskCanceller::new)
    );

    static TaskCanceller get(Plugin plugin) {
        return PROVIDER.get(plugin);
    }

    void cancelAll();
}
