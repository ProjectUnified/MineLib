package io.github.projectunified.minelib.plugin.postenable;

import io.github.projectunified.minelib.plugin.base.BasePlugin;
import io.github.projectunified.minelib.plugin.base.Loadable;

/**
 * The component for the post-enable phase of the plugin lifecycle
 */
public class PostEnableComponent implements Loadable {
    final BasePlugin plugin;

    /**
     * Creates a new instance
     *
     * @param plugin the plugin
     */
    public PostEnableComponent(BasePlugin plugin) {
        this.plugin = plugin;
    }

    void call() {
        plugin.call(PostEnable.class, PostEnable::postEnable);
        if (plugin instanceof PostEnable) {
            ((PostEnable) plugin).postEnable();
        }
    }

    @Override
    public void enable() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            new FoliaPostEnableListener(this);
        } catch (ClassNotFoundException e) {
            new BukkitPostEnableListener(this);
        }
    }
}
