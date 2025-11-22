package io.github.projectunified.minelib.plugin.base;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Base plugin class that provides a simple way to manage components
 */
public class BasePlugin extends JavaPlugin implements Loadable {
    private final List<Object> components;
    private final Map<Class<?>, List<Object>> lookupMap;

    /**
     * Create a new plugin instance
     */
    public BasePlugin() {
        this.components = getComponents();
        this.lookupMap = new ConcurrentHashMap<>();
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

    private List<Object> getComponents(Class<?> type) {
        return lookupMap.computeIfAbsent(
                type,
                t -> components.stream().filter(t::isInstance).map(t::cast).collect(Collectors.toList())
        );
    }

    /**
     * Get all components that are the instanced of the given type
     *
     * @param type the class of the component
     * @param <T>  the type
     * @return the type of the component
     */
    @SuppressWarnings("unchecked")
    public final <T> List<T> getAll(Class<T> type) {
        return (List<T>) getComponents(type);
    }

    /**
     * Get a component by its class.
     *
     * @param type the class of the component
     * @param <T>  the type of the component
     * @return the component
     */
    public final <T> T get(Class<T> type) {
        List<Object> components = getComponents(type);
        if (components.isEmpty()) {
            throw new IllegalArgumentException("No component of type " + type.getName() + " found");
        }
        return type.cast(components.get(0));
    }

    /**
     * Call a consumer for each component of a given type.
     *
     * @param type     the class of the component
     * @param consumer the consumer to call
     * @param reverse  whether to iterate in reverse order
     * @param <T>      the type of the component
     */
    public final <T> void call(Class<T> type, Consumer<T> consumer, boolean reverse) {
        List<T> components = getAll(type);
        if (components.isEmpty()) return;

        int size = components.size();
        int index, step;
        if (reverse) {
            index = size - 1;
            step = -1;
        } else {
            index = 0;
            step = 1;
        }

        while (index >= 0 && index < size) {
            T component = components.get(index);
            consumer.accept(component);
            index += step;
        }
    }

    /**
     * Call a consumer for each component of a given type.
     *
     * @param type     the class of the component
     * @param consumer the consumer to call
     * @param <T>      the type of the component
     */
    public final <T> void call(Class<T> type, Consumer<T> consumer) {
        call(type, consumer, false);
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
        this.call(Loadable.class, Loadable::disable, true);
        this.disable();
        this.lookupMap.clear();
    }
}
