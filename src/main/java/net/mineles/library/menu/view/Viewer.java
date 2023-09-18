package net.mineles.library.menu.view;

import net.mineles.library.components.PlayerComponent;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class Viewer {
    private final @NotNull PlayerComponent player;
    private final @NotNull InventoryView view;
    private final @NotNull String target;
    private int page;
    private boolean closed;

    public Viewer(@NotNull PlayerComponent player,
                  @NotNull InventoryView view,
                  @NotNull String target,
                  int page) {
        this.player = player;
        this.view = view;
        this.target = target;
        this.page = page;
    }

    @NotNull
    public static Viewer create(@NotNull PlayerComponent player,
                                @NotNull InventoryView view,
                                @NotNull String target) {
        return create(player, view, target, 0);
    }

    @NotNull
    public static Viewer create(@NotNull PlayerComponent player,
                                @NotNull InventoryView view,
                                @NotNull String target,
                                int page) {
        return new Viewer(player, view, target, page);
    }

    public @NotNull PlayerComponent getPlayer() {
        return this.player;
    }

    public @NotNull UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    public @NotNull InventoryView getView() {
        return this.view;
    }

    public @NotNull String getTarget() {
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
