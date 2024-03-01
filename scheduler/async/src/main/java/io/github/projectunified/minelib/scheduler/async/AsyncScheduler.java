package io.github.projectunified.minelib.scheduler.async;

import io.github.projectunified.minelib.scheduler.common.provider.ObjectProvider;
import io.github.projectunified.minelib.scheduler.common.scheduler.Scheduler;
import io.github.projectunified.minelib.scheduler.common.util.Platform;
import org.bukkit.plugin.Plugin;

public interface AsyncScheduler extends Scheduler {
    ObjectProvider<Plugin, AsyncScheduler> PROVIDER = new ObjectProvider<>(
            ObjectProvider.entry(Platform.FOLIA::isPlatform, FoliaAsyncScheduler::new),
            ObjectProvider.entry(BukkitAsyncScheduler::new)
    );

    static AsyncScheduler get(Plugin plugin) {
        return PROVIDER.get(plugin);
    }
}
