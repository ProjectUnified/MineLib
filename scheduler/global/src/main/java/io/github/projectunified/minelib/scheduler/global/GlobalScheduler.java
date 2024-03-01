package io.github.projectunified.minelib.scheduler.global;

import io.github.projectunified.minelib.scheduler.common.provider.ObjectProvider;
import io.github.projectunified.minelib.scheduler.common.scheduler.Scheduler;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.plugin.Plugin;

public interface GlobalScheduler extends Scheduler {
    ObjectProvider<Plugin, GlobalScheduler> PROVIDER = new ObjectProvider<>(
            ObjectProvider.entry(Platform.FOLIA::isPlatform, FoliaGlobalScheduler::new),
            ObjectProvider.entry(BukkitGlobalScheduler::new)
    );

    static GlobalScheduler get(Plugin plugin) {
        return PROVIDER.get(plugin);
    }
}
