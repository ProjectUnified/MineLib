package io.github.projectunified.minelib.plugin.base;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Base plugin class that provides a simple way to manage components
 */
public class BasePlugin extends JavaPlugin implements Loadable {
    private final Map<Class<?>, Object> components;

    /**
     * Create a new plugin instance
     */
    public BasePlugin() {
        components = getComponents()
                .stream()
                .collect(Collectors.toMap(Object::getClass, o -> o));
    }

    /**
     * Get the components that should be managed by the plugin.
     * Plugins should override this method to provide their own components.
     *
     * @return a list of components
     */
    protected List<Object> getComponents() {
        return Collections.emptyList();
    }

    /**
     * Get a component by its class.
     *
     * @param type the class of the component
     * @param <T>  the type of the component
     * @return the component
     */
    public final <T> T get(Class<T> type) {
        return type.cast(components.get(type));
    }

    /**
     * Call a consumer for each component of a given type.
     *
     * @param type     the class of the component
     * @param consumer the consumer to call
     * @param <T>      the type of the component
     */
    public final <T> void call(Class<T> type, Consumer<T> consumer) {
        for (Object component : components.values()) {
            if (type.isInstance(component)) {
                consumer.accept(type.cast(component));
            }
        }
    }

    @Override
    public final void onLoad() {
        this.call(Loadable.class, Loadable::load);
        this.load();
    }

    @Override
    public final void onEnable() {
        this.call(Loadable.class, Loadable::enable);
        this.enable();
    }

    @Override
    public final void onDisable() {
        this.call(Loadable.class, Loadable::disable);
        this.disable();
    }
}
