package io.github.projectunified.minelib.plugin.base;

/**
 * Interface for loadable components
 */
public interface Loadable {
    /**
     * Called when the component is loaded
     */
    default void load() {
        // EMPTY
    }

    /**
     * Called when the component is enabled
     */
    default void enable() {
        // EMPTY
    }

    /**
     * Called when the component is disabled
     */
    default void disable() {
        // EMPTY
    }
}
