package net.mineles.library.listener;

import net.mineles.library.components.PlayerComponent;
import net.mineles.library.menu.misc.ClickResult;
import net.mineles.library.menu.misc.contexts.ClickContext;
import net.mineles.library.plugin.BukkitPlugin;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public final class InventoryListener implements Listener {
    private final BukkitPlugin plugin;

    public InventoryListener(BukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent event) {
        HumanEntity humanEntity = event.getPlayer();
        if (!(humanEntity instanceof Player)) {
            return;
        }

        this.plugin.getMenuManager().stopViewing(humanEntity.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (!(humanEntity instanceof Player)) {
            return;
        }

        this.plugin.getMenuManager().findByViewer(humanEntity.getUniqueId()).ifPresent(menu -> {
            PlayerComponent player = PlayerComponent.from(this.plugin, (Player) humanEntity);

            ClickContext clickContext = ClickContext.from(this.plugin, menu, player, event);
            ClickResult result = menu.click(clickContext);

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
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrag(InventoryDragEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (!(humanEntity instanceof Player)) {
            return;
        }

        this.plugin.getMenuManager().findByViewer(humanEntity.getUniqueId())
                .ifPresent(menu -> event.setCancelled(true));
    }
}
