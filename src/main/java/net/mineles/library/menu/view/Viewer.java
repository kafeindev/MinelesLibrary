package net.mineles.library.menu.view;

import net.mineles.library.components.PlayerComponent;
import org.bukkit.inventory.InventoryView;

import java.util.UUID;

public final class Viewer {
    private final PlayerComponent player;
    private final InventoryView view;
    private final String target;
    private int page;
    private boolean closed;

    public Viewer(PlayerComponent player, InventoryView view, String target, int page) {
        this.player = player;
        this.view = view;
        this.target = target;
        this.page = page;
    }

    public static Viewer create(PlayerComponent player, InventoryView view, String target) {
        return create(player, view, target, 0);
    }

    public static Viewer create(PlayerComponent player, InventoryView view, String target, int page) {
        return new Viewer(player, view, target, page);
    }

    public PlayerComponent getPlayer() {
        return this.player;
    }

    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    public InventoryView getView() {
        return this.view;
    }

    public String getTarget() {
        return this.target;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
