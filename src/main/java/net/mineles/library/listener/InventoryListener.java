package net.mineles.library.listener;

import net.mineles.library.components.PlayerComponent;
import net.mineles.library.libs.nbtapi.nbtapi.NBTItem;
import net.mineles.library.menu.Menu;
import net.mineles.library.menu.misc.ClickResult;
import net.mineles.library.menu.misc.contexts.ClickContext;
import net.mineles.library.plugin.BukkitPlugin;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public final class InventoryListener implements Listener {
    private final BukkitPlugin plugin;

    public InventoryListener(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent event) {
        HumanEntity humanEntity = event.getPlayer();
        if (!(humanEntity instanceof Player player)) {
            return;
        }

        ItemStack itemStack = player.getItemOnCursor();
        if (isButton(itemStack)) {
            player.setItemOnCursor(null);
        }

        this.plugin.getMenuManager().stopViewing(humanEntity.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemOnCursor();
        if (isButton(itemStack)) {
            player.setItemOnCursor(null);
        }

        this.plugin.getMenuManager().stopViewing(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (!(humanEntity instanceof Player)) {
            return;
        }

        Menu menu =this.plugin.getMenuManager().findByViewer(humanEntity.getUniqueId()).orElse(null);
        if (menu != null) {
            PlayerComponent player = PlayerComponent.from(this.plugin, (Player) humanEntity);

            ClickContext clickContext = ClickContext.from(this.plugin, menu, player, event);
            ClickResult result;
            try {
                result = menu.click(clickContext);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                result = ClickResult.CANCELLED;
            }

            switch (result) {
                case CANCELLED -> {
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                }
                case CURSORED -> {
                    event.setCancelled(false);
                    event.setResult(Event.Result.ALLOW);
                }
                case REFRESH -> {
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                    menu.refresh(humanEntity.getUniqueId());
                }
                case NEXT_PAGE -> {
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                    menu.nextPage(humanEntity.getUniqueId());
                }
                case PREVIOUS_PAGE -> {
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                    menu.previousPage(humanEntity.getUniqueId());
                }
                case CLOSE -> {
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                    menu.close(humanEntity.getUniqueId());
                }
            }
        } else {
            ItemStack itemStack = event.getCurrentItem();
            if (isButton(itemStack)) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrag(InventoryDragEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (!(humanEntity instanceof Player)) {
            return;
        }

        Menu menu = this.plugin.getMenuManager().findByViewer(humanEntity.getUniqueId()).orElse(null);
        if (menu != null) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        } else {
            ItemStack itemStack = event.getOldCursor();
            if (isButton(itemStack)) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
            } else if (event.getNewItems().values().stream().anyMatch(this::isButton)) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();

        ItemStack itemStack = item.getItemStack();
        if (!isButton(itemStack)) {
            return;
        }

        item.remove();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickup(PlayerPickupItemEvent event) {
        Item item = event.getItem();

        ItemStack itemStack = item.getItemStack();
        if (!isButton(itemStack)) {
            return;
        }

        item.remove();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSwapItems(PlayerSwapHandItemsEvent event) {
        ItemStack itemStack = event.getOffHandItem();
        if (!isButton(itemStack)) {
            return;
        }

        event.setCancelled(true);
        event.setOffHandItem(null);
    }

    private boolean isButton(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir() || itemStack.getAmount() <= 0 || !itemStack.hasItemMeta()) {
            return false;
        }

        NBTItem nbtItem = new NBTItem(itemStack);
        return nbtItem.hasKey("mineles-button");
    }
}
