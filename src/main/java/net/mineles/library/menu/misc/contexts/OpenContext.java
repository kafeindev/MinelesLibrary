package net.mineles.library.menu.misc.contexts;

import net.mineles.library.action.Context;
import net.mineles.library.components.PlayerComponent;
import net.mineles.library.menu.Menu;
import net.mineles.library.menu.misc.OpenCause;

public final class OpenContext implements Context {
    private final PlayerComponent player;
    private final Menu menu;
    private final OpenCause cause;
    private final int page;

    public OpenContext(PlayerComponent player, Menu menu, OpenCause cause, int page) {
        this.player = player;
        this.menu = menu;
        this.cause = cause;
        this.page = page;
    }

    public static OpenContext create(PlayerComponent player, Menu menu, OpenCause cause, int page) {
        return new OpenContext(player, menu, cause, page);
    }

    public PlayerComponent getPlayer() {
        return this.player;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public OpenCause getCause() {
        return this.cause;
    }

    public int getPage() {
        return this.page;
    }
}
