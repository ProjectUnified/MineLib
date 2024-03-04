package io.github.projectunified.minelib.plugin.postenable;

/**
 * An interface for components that need to run code after the plugin has been enabled
 */
public interface PostEnable {
    /**
     * Called after the plugin has been enabled
     */
    void postEnable();
}
