package net.mineles.library.menu.misc.contexts;

import net.mineles.library.action.context.Context;
import net.mineles.library.components.ItemComponent;
import net.mineles.library.menu.Menu;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public final class ClickContext extends Context {
    public ClickContext(@NotNull Object... args) {
        super(args);
    }

    @NotNull
    public static ClickContext from(@NotNull InventoryClickEvent event,
                                    @NotNull Menu menu) {
        ItemComponent item = event.getCurrentItem() == null ? null
                : ItemComponent.from(event.getCurrentItem());
        ItemComponent cursor = event.getCursor() == null ? null
                : ItemComponent.from(event.getCursor());

        ClickType clickType = event.getClick();

        InventoryType.SlotType slotType = event.getSlotType();
        int slot = event.getSlot();

        return new ClickContext(menu, event, item, cursor, clickType, slotType, slot);
    }

    public InventoryClickEvent getEvent() {
        return get(InventoryClickEvent.class, 1);
    }

    public Menu getMenu() {
        return get(Menu.class, 0);
    }

    public ItemComponent getItem() {
        return get(ItemComponent.class, 0);
    }

    public ItemComponent getCursor() {
        return get(ItemComponent.class, 1);
    }

    public ClickType getClickType() {
        return get(ClickType.class, 0);
    }

    public InventoryType.SlotType getSlotType() {
        return get(InventoryType.SlotType.class, 0);
    }

    public Integer getSlot() {
        return get(Integer.class, 0);
    }

    public boolean isLeftClick() {
        return getEvent().isLeftClick();
    }

    public boolean isRightClick() {
        return getEvent().isRightClick();
    }

    public boolean isShiftClick() {
        return getEvent().isShiftClick();
    }
}
