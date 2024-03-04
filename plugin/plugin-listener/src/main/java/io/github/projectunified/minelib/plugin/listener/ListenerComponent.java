package io.github.projectunified.minelib.plugin.listener;

import io.github.projectunified.minelib.plugin.base.BasePlugin;
import io.github.projectunified.minelib.plugin.base.Loadable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * A component that registers and unregisters itself as a {@link Listener}
 */
public class ListenerComponent implements Loadable, Listener {
    protected final BasePlugin plugin;

    /**
     * Create a new instance
     *
     * @param plugin the plugin
     */
    public ListenerComponent(BasePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enable() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(this);
    }
}
