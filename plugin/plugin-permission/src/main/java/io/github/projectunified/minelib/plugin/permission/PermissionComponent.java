package io.github.projectunified.minelib.plugin.permission;

import io.github.projectunified.minelib.plugin.base.BasePlugin;
import io.github.projectunified.minelib.plugin.base.Loadable;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A component that handles the registration and unregistration of {@link Permission}
 */
public class PermissionComponent implements Loadable {
    private final BasePlugin plugin;
    private final Set<Permission> registeredPermissions = new HashSet<>();

    /**
     * Create a new instance
     *
     * @param plugin the plugin
     */
    public PermissionComponent(BasePlugin plugin) {
        this.plugin = plugin;
    }

    private List<Permission> getPermissions() {
        List<Permission> permissions = new ArrayList<>();

        Class<?>[] classes = {
                getClass(),
                plugin.getClass()
        };

        for (Class<?> clazz : classes) {
            for (Field field : clazz.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (!field.getType().equals(Permission.class) || !Modifier.isStatic(modifiers) || !Modifier.isPublic(modifiers))
                    continue;

                try {
                    Permission permission = (Permission) field.get(null);
                    permissions.add(permission);
                } catch (IllegalAccessException e) {
                    plugin.getLogger().warning("Failed to access permission field: " + field.getName());
                }
            }
        }

        return permissions;
    }

    @Override
    public void enable() {
        List<Permission> permissions = getPermissions();
        for (Permission permission : permissions) {
            Bukkit.getPluginManager().addPermission(permission);
            registeredPermissions.add(permission);
        }
    }

    @Override
    public void disable() {
        for (Permission permission : registeredPermissions) {
            Bukkit.getPluginManager().removePermission(permission);
        }
    }
}
