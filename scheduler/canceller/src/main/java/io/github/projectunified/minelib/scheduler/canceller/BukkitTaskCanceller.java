package io.github.projectunified.minelib.scheduler.canceller;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

class BukkitTaskCanceller implements TaskCanceller {
    private final Plugin plugin;

    BukkitTaskCanceller(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void cancelAll() {
        Bukkit.getScheduler().cancelTasks(this.plugin);
    }
}
