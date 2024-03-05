package io.github.projectunified.minelib.plugin.listener;

import io.github.projectunified.minelib.plugin.base.Loadable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A component that registers and unregisters itself as a {@link Listener}.
 * Plugin listeners should extend this class and register their events.
 */
public interface ListenerComponent extends Loadable, Listener {
    /**
     * Get the plugin that this listener is associated with.
     * You may override this method to return a different plugin, in case that the listener is not in the same plugin.
     *
     * @return the plugin
     */
    default JavaPlugin getPlugin() {
        return JavaPlugin.getProvidingPlugin(getClass());
    }

    @Override
    default void enable() {
        JavaPlugin plugin = getPlugin();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    default void disable() {
        HandlerList.unregisterAll(this);
    }
}
