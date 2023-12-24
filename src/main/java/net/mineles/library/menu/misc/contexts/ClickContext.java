package net.mineles.library.menu.misc.contexts;

import net.mineles.library.action.Context;
import net.mineles.library.components.ItemComponent;
import net.mineles.library.components.PlayerComponent;
import net.mineles.library.menu.Menu;
import net.mineles.library.menu.button.Button;
import net.mineles.library.plugin.BukkitPlugin;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public final class ClickContext implements Context {
    private final BukkitPlugin plugin;
    private final Menu menu;
    private final PlayerComponent player;
    private final InventoryClickEvent event;

    private final Button button;
    private final ItemComponent item;
    private final ItemComponent cursor;

    private final ClickType clickType;
    private final InventoryType.SlotType slotType;
    private final int slot;

    public ClickContext(BukkitPlugin plugin,
                        Menu menu,
                        PlayerComponent player,
                        InventoryClickEvent event,
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

    public static ClickContext create(BukkitPlugin plugin,
                                      Menu menu,
                                      PlayerComponent player,
                                      InventoryClickEvent event,
                                      Button button,
                                      ItemComponent item,
                                      ItemComponent cursor,
                                      ClickType clickType,
                                      InventoryType.SlotType slotType,
                                      int slot) {
        return new ClickContext(plugin, menu, player, event, button, item, cursor, clickType, slotType, slot);
    }

    public static ClickContext from(BukkitPlugin plugin,
                                    Menu menu,
                                    PlayerComponent player,
                                    InventoryClickEvent event) {
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

    public BukkitPlugin getPlugin() {
        return this.plugin;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public PlayerComponent getPlayer() {
        return this.player;
    }

    public InventoryClickEvent getEvent() {
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
