package io.github.projectunified.minelib.scheduler.canceller;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

class FoliaTaskCanceller implements TaskCanceller {
    private final Plugin plugin;

    FoliaTaskCanceller(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void cancelAll() {
        Bukkit.getGlobalRegionScheduler().cancelTasks(plugin);
        Bukkit.getAsyncScheduler().cancelTasks(plugin);
    }
}
