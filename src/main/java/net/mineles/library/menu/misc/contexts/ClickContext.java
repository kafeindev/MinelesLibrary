package net.mineles.library.menu.misc.contexts;

import net.mineles.library.action.Context;
import net.mineles.library.components.ItemComponent;
import net.mineles.library.components.PlayerComponent;
import net.mineles.library.menu.Menu;
import net.mineles.library.menu.button.Button;
import net.mineles.library.plugin.BukkitPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public final class ClickContext implements Context {
    private final @NotNull BukkitPlugin plugin;
    private final @NotNull Menu menu;
    private final @NotNull PlayerComponent player;
    private final @NotNull InventoryClickEvent event;

    private final Button button;
    private final ItemComponent item;
    private final ItemComponent cursor;

    private final ClickType clickType;
    private final InventoryType.SlotType slotType;
    private final int slot;

    public ClickContext(@NotNull BukkitPlugin plugin,
                        @NotNull Menu menu,
                        @NotNull PlayerComponent player,
                        @NotNull InventoryClickEvent event,
                        Button button,
                        ItemComponent item,
                        ItemComponent cursor,
                        ClickType clickType,
                        InventoryType.SlotType slotType,
                        int slot) {
        this.plugin = plugin;
        this.menu = menu;
        this.player = player;
        this.event = event;
        this.button = button;
        this.item = item;
        this.cursor = cursor;
        this.clickType = clickType;
        this.slotType = slotType;
        this.slot = slot;
    }

    @NotNull
    public static ClickContext create(@NotNull BukkitPlugin plugin,
                                      @NotNull Menu menu,
                                      @NotNull PlayerComponent player,
                                      @NotNull InventoryClickEvent event,
                                      Button button,
                                      ItemComponent item,
                                      ItemComponent cursor,
                                      ClickType clickType,
                                      InventoryType.SlotType slotType,
                                      int slot) {
        return new ClickContext(plugin, menu, player, event, button, item, cursor, clickType, slotType, slot);
    }

    @NotNull
    public static ClickContext from(@NotNull BukkitPlugin plugin,
                                    @NotNull Menu menu,
                                    @NotNull PlayerComponent player,
                                    @NotNull InventoryClickEvent event) {
        Button button = menu.findButton(event.getSlot()).orElse(null);

        ItemComponent item = event.getCurrentItem() == null ? null
                : ItemComponent.from(event.getCurrentItem());
        ItemComponent cursor = event.getCursor() == null ? null
                : ItemComponent.from(event.getCursor());

        ClickType clickType = event.getClick();
        InventoryType.SlotType slotType = event.getSlotType();
        int slot = event.getSlot();

        return create(plugin, menu, player, event, button, item, cursor, clickType, slotType, slot);
    }

    public @NotNull BukkitPlugin getPlugin() {
        return this.plugin;
    }

    public @NotNull Menu getMenu() {
        return this.menu;
    }

    public @NotNull PlayerComponent getPlayer() {
        return this.player;
    }

    public @NotNull InventoryClickEvent getEvent() {
        return this.event;
    }

    public Button getButton() {
        return this.button;
    }

    public ItemComponent getItem() {
        return this.item;
    }

    public ItemComponent getCursor() {
        return this.cursor;
    }

    public ClickType getClickType() {
        return this.clickType;
    }

    public InventoryType.SlotType getSlotType() {
        return this.slotType;
    }

    public int getSlot() {
        return this.slot;
    }

    public boolean isLeftClick() {
        return this.clickType == ClickType.LEFT;
    }

    public boolean isRightClick() {
        return this.clickType == ClickType.RIGHT;
    }

    public boolean isShiftClick() {
        return this.clickType == ClickType.SHIFT_LEFT || this.clickType == ClickType.SHIFT_RIGHT;
    }

    public boolean isMiddleClick() {
        return this.clickType == ClickType.MIDDLE;
    }
}
