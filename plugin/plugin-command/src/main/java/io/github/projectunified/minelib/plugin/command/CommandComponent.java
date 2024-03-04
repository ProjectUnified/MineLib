package io.github.projectunified.minelib.plugin.command;

import io.github.projectunified.minelib.plugin.base.BasePlugin;
import io.github.projectunified.minelib.plugin.base.Loadable;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;

/**
 * A component that manages the commands
 */
public class CommandComponent implements Loadable {
    private static final Supplier<CommandMap> COMMAND_MAP_SUPPLIER;
    private static final Supplier<Map<?, ?>> KNOWN_COMMANDS_SUPPLIER;
    private static final Runnable SYNC_COMMANDS_RUNNABLE;

    static {
        Method commandMapMethod;
        try {
            commandMapMethod = Bukkit.getServer().getClass().getMethod("getCommandMap");
        } catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }

        COMMAND_MAP_SUPPLIER = () -> {
            try {
                return (CommandMap) commandMapMethod.invoke(Bukkit.getServer());
            } catch (ReflectiveOperationException e) {
                throw new ExceptionInInitializerError(e);
            }
        };

        Supplier<Map<?, ?>> knownCommandsSupplier;
        try {
            Method knownCommandsMethod = SimpleCommandMap.class.getDeclaredMethod("getKnownCommands");
            knownCommandsSupplier = () -> {
                try {
                    return (Map<?, ?>) knownCommandsMethod.invoke(COMMAND_MAP_SUPPLIER.get());
                } catch (ReflectiveOperationException e) {
                    throw new ExceptionInInitializerError(e);
                }
            };
        } catch (NoSuchMethodException e) {
            try {
                Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                knownCommandsSupplier = () -> {
                    try {
                        return (Map<?, ?>) knownCommandsField.get(COMMAND_MAP_SUPPLIER.get());
                    } catch (ReflectiveOperationException ex) {
                        throw new ExceptionInInitializerError(ex);
                    }
                };
            } catch (ReflectiveOperationException ex) {
                throw new ExceptionInInitializerError(ex);
            }
        }
        KNOWN_COMMANDS_SUPPLIER = knownCommandsSupplier;

        Runnable syncCommandsRunnable;
        try {
            Class<?> craftServer = Bukkit.getServer().getClass();
            Method syncCommandsMethod = craftServer.getDeclaredMethod("syncCommands");
            syncCommandsMethod.setAccessible(true);
            syncCommandsRunnable = () -> {
                try {
                    syncCommandsMethod.invoke(Bukkit.getServer());
                } catch (ReflectiveOperationException e) {
                    Bukkit.getLogger().log(Level.WARNING, "Error when syncing commands", e);
                }
            };
        } catch (Exception e) {
            syncCommandsRunnable = () -> {
            };
        }
        SYNC_COMMANDS_RUNNABLE = syncCommandsRunnable;
    }

    private final BasePlugin plugin;
    private final Supplier<List<Command>> commandSupplier;
    private final Map<String, Command> registered = new HashMap<>();

    /**
     * Create a new instance
     *
     * @param plugin          the plugin
     * @param commandSupplier the command supplier
     */
    public CommandComponent(BasePlugin plugin, Supplier<List<Command>> commandSupplier) {
        this.plugin = plugin;
        this.commandSupplier = commandSupplier;
    }

    /**
     * Sync the commands to the server. Mainly used to make tab completer work in 1.13+
     */
    public static void syncCommand() {
        SYNC_COMMANDS_RUNNABLE.run();
    }

    /**
     * Unregister a command from the known commands
     *
     * @param command the command
     */
    public static void unregisterFromKnownCommands(Command command) {
        Map<?, ?> knownCommands = KNOWN_COMMANDS_SUPPLIER.get();
        knownCommands.values().removeIf(command::equals);
        command.unregister(COMMAND_MAP_SUPPLIER.get());
    }

    /**
     * Register the command to the command map
     *
     * @param label   the label of the command
     * @param command the command
     */
    public static void registerCommandToCommandMap(String label, Command command) {
        COMMAND_MAP_SUPPLIER.get().register(label, command);
    }

    /**
     * Register the command
     *
     * @param command the command object
     */
    public void register(Command command) {
        String name = command.getLabel();
        if (this.registered.containsKey(name)) {
            this.plugin.getLogger().log(Level.WARNING, "Duplicated \"{0}\" command ! Ignored", name);
            return;
        }

        registerCommandToCommandMap(this.plugin.getName(), command);
        this.registered.put(name, command);
    }

    /**
     * Unregister the command
     *
     * @param command the command object
     */
    public void unregister(Command command) {
        unregisterFromKnownCommands(command);
        this.registered.remove(command.getLabel());
    }

    /**
     * Unregister the command
     *
     * @param command the command label
     */
    public void unregister(String command) {
        if (this.registered.containsKey(command)) {
            unregister(this.registered.remove(command));
        }
    }

    /**
     * Unregister all commands
     */
    public void unregisterAll() {
        this.registered.values().forEach(CommandComponent::unregisterFromKnownCommands);
        this.registered.clear();
    }

    /**
     * Get registered commands
     *
     * @return the map contains the name and the command object
     */
    public Map<String, Command> getRegistered() {
        return Collections.unmodifiableMap(this.registered);
    }

    @Override
    public void enable() {
        this.commandSupplier.get().forEach(this::register);
        syncCommand();
    }

    @Override
    public void disable() {
        this.unregisterAll();
    }
}
