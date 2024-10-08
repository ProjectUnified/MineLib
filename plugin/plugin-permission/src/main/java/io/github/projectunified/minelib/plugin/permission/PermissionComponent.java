package io.github.projectunified.minelib.plugin.permission;

import io.github.projectunified.minelib.plugin.base.BasePlugin;
import io.github.projectunified.minelib.plugin.base.Loadable;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * A component that handles the registration and unregistration of {@link Permission}
 */
public class PermissionComponent implements Loadable {
    private final List<Permission> permissions = new ArrayList<>();
    private boolean enabled = false;

    /**
     * Create a new instance
     *
     * @param permissions the permissions
     */
    public PermissionComponent(List<Permission> permissions) {
        this.permissions.addAll(permissions);
    }

    /**
     * Create a new instance that automatically discovers the permissions
     *
     * @param plugin the plugin
     */
    public PermissionComponent(BasePlugin plugin) {
        List<Permission> permissions = new ArrayList<>();

        Class<?>[] classes = {
                this.getClass(),
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

        this.permissions.addAll(permissions);
    }

    /**
     * Add a permission
     *
     * @param permission the permission
     */
    public void addPermission(Permission permission) {
        if (enabled) {
            Bukkit.getPluginManager().addPermission(permission);
        }
        permissions.add(permission);
    }

    /**
     * Remove a permission
     *
     * @param permission the permission
     */
    public void removePermission(Permission permission) {
        if (enabled) {
            Bukkit.getPluginManager().removePermission(permission);
        }
        permissions.remove(permission);
    }

    @Override
    public void enable() {
        for (Permission permission : permissions) {
            Bukkit.getPluginManager().addPermission(permission);
        }
        enabled = true;
    }

    @Override
    public void disable() {
        for (Permission permission : permissions) {
            Bukkit.getPluginManager().removePermission(permission);
        }
        enabled = false;
    }
}
