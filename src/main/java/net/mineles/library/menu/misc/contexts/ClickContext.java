package net.mineles.library.menu.misc.contexts;

import net.mineles.library.action.Context;
import net.mineles.library.components.ItemComponent;
import net.mineles.library.menu.Menu;
import net.mineles.library.menu.button.Button;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public final class ClickContext implements Context {
    private final @NotNull InventoryClickEvent event;
    private final @NotNull Menu menu;

    private final Button button;
    private final ItemComponent item;
    private final ItemComponent cursor;

    private final ClickType clickType;
    private final InventoryType.SlotType slotType;
    private final int slot;

    public ClickContext(@NotNull Menu menu,
                        @NotNull InventoryClickEvent event,
                        Button button,
                        ItemComponent item,
                        ItemComponent cursor,
                        ClickType clickType,
                        InventoryType.SlotType slotType,
                        int slot) {
        this.menu = menu;
        this.event = event;
        this.button = button;
        this.item = item;
        this.cursor = cursor;
        this.clickType = clickType;
        this.slotType = slotType;
        this.slot = slot;
    }

    @NotNull
    public static ClickContext create(@NotNull Menu menu,
                                      @NotNull InventoryClickEvent event,
                                      Button button,
                                      ItemComponent item,
                                      ItemComponent cursor,
                                      ClickType clickType,
                                      InventoryType.SlotType slotType,
                                      int slot) {
        return new ClickContext(menu, event, button, item, cursor, clickType, slotType, slot);
    }

    @NotNull
    public static ClickContext from(@NotNull InventoryClickEvent event,
                                    @NotNull Menu menu) {
        Button button = menu.findButton(event.getSlot()).orElse(null);

        ItemComponent item = event.getCurrentItem() == null ? null
                : ItemComponent.from(event.getCurrentItem());
        ItemComponent cursor = event.getCursor() == null ? null
                : ItemComponent.from(event.getCursor());

        ClickType clickType = event.getClick();
        InventoryType.SlotType slotType = event.getSlotType();
        int slot = event.getSlot();

        return create(menu, event, button, item, cursor, clickType, slotType, slot);
    }

    public @NotNull InventoryClickEvent getEvent() {
        return this.event;
    }

    public @NotNull Menu getMenu() {
        return this.menu;
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
