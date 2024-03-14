package io.github.projectunified.minelib.util.listener.swapitem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A listener to catch the event when a player swaps two items in the inventory
 *
 * @param <P> the plugin
 */
public abstract class SwapItemListener<P extends Plugin> implements Listener {
    protected final P plugin;
    private EventPriority priority = EventPriority.NORMAL;

    /**
     * Create a new listener
     *
     * @param plugin the plugin
     */
    protected SwapItemListener(P plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the priority of the event
     *
     * @param priority the priority
     */
    public final void setPriority(EventPriority priority) {
        this.priority = priority;
    }

    public final void register() {
        plugin.getServer().getPluginManager().registerEvent(InventoryClickEvent.class, this, priority, (listener, event) -> {
            if (event instanceof InventoryClickEvent) {
                ((SwapItemListener<?>) listener).onSwap((InventoryClickEvent) event);
            }
        }, plugin, false);
    }

    /**
     * Called when a player swaps two items in the inventory
     *
     * @param player the player
     * @param target the target item
     * @param cursor the cursor item
     * @param action the inventory action
     * @return the result
     */
    @NotNull
    protected abstract SwapItemListener.Result onSwap(
            @NotNull Player player,
            @NotNull ItemStack target,
            @NotNull ItemStack cursor,
            @NotNull InventoryAction action
    );

    private void onSwap(InventoryClickEvent event) {
        InventoryAction action = event.getAction();
        if (!action.equals(InventoryAction.SWAP_WITH_CURSOR) && !action.name().startsWith("PLACE_")) return;
        ItemStack target = event.getCurrentItem();
        ItemStack cursor = event.getCursor();
        if (target == null || cursor == null) return;

        Player player = (Player) event.getWhoClicked();
        Result result = onSwap(player, target, cursor, action);
        if (!result.success) return;
        event.setCancelled(true);
        event.setCurrentItem(result.target);
        player.setItemOnCursor(result.cursor);
    }

    /**
     * The result of the swap event
     */
    public static class Result {
        /**
         * Whether the event is successful.
         * If it is true, the event will be cancelled and the items will be changed.
         */
        public final boolean success;
        /**
         * The new target item.
         * If it is null, the target item will be removed.
         */
        @Nullable
        public final ItemStack target;
        /**
         * The new cursor item.
         * If it is null, the cursor item will be removed.
         */
        @Nullable
        public final ItemStack cursor;

        /**
         * Create a new result
         *
         * @param success whether the event is successful
         * @param target  the new target item
         * @param cursor  the new cursor item
         */
        public Result(boolean success, @Nullable ItemStack target, @Nullable ItemStack cursor) {
            this.success = success;
            this.target = target;
            this.cursor = cursor;
        }
    }
}
