package io.github.projectunified.minelib.plugin.postenable;

import io.papermc.paper.threadedregions.RegionizedServerInitEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

class FoliaPostEnableListener implements Listener {
    FoliaPostEnableListener(PostEnableComponent component) {
        Bukkit.getPluginManager().registerEvent(RegionizedServerInitEvent.class, this, EventPriority.NORMAL, (listener, event) -> {
            if (event instanceof RegionizedServerInitEvent) {
                component.call();
            }
        }, component.plugin);
    }
}
