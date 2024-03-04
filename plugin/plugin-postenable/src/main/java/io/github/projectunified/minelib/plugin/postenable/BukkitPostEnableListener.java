package io.github.projectunified.minelib.plugin.postenable;

import org.bukkit.Bukkit;

class BukkitPostEnableListener {
    BukkitPostEnableListener(PostEnableComponent component) {
        Bukkit.getScheduler().runTask(component.plugin, component::call);
    }
}
