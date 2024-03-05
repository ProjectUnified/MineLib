package io.github.projectunified.minelib.plugin.base;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Consumer;

/**
 * Base plugin class that provides a simple way to manage components
 */
public class BasePlugin extends JavaPlugin implements Loadable {
    private final Map<Class<?>, Object> components;

    /**
     * Create a new plugin instance
     */
    public BasePlugin() {
        components = getComponentMap(getComponents());
    }

    /**
     * Get a map of components by their class and interfaces
     *
     * @param components the components to map
     * @return the map of components
     */
    private static Map<Class<?>, Object> getComponentMap(List<Object> components) {
        Map<Class<?>, Object> componentMap = new HashMap<>();
        for (Object component : components) {
            Set<Class<?>> addedClasses = new HashSet<>();
            Queue<Class<?>> classQueue = new LinkedList<>();
            classQueue.add(component.getClass());
            while (true) {
                Class<?> clazz = classQueue.poll();
                if (clazz == null) break;
                if (!addedClasses.add(clazz)) continue;

                componentMap.put(clazz, component);

                Class<?> superClass = clazz.getSuperclass();
                if (superClass != null) {
                    classQueue.add(superClass);
                }
                classQueue.addAll(Arrays.asList(clazz.getInterfaces()));
            }
        }
        return componentMap;
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
