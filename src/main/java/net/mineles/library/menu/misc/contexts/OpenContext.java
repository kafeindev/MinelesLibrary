package net.mineles.library.menu.misc.contexts;

import net.mineles.library.action.Context;
import net.mineles.library.components.PlayerComponent;
import net.mineles.library.menu.Menu;
import net.mineles.library.menu.misc.OpenCause;
import org.jetbrains.annotations.NotNull;

public final class OpenContext implements Context {
    private final @NotNull PlayerComponent player;
    private final @NotNull Menu menu;
    private final @NotNull OpenCause cause;
    private final int page;

    public OpenContext(@NotNull PlayerComponent player,
                       @NotNull Menu menu,
                       @NotNull OpenCause cause,
                       int page) {
        this.player = player;
        this.menu = menu;
        this.cause = cause;
        this.page = page;
    }

    @NotNull
    public static OpenContext create(@NotNull PlayerComponent player,
                                     @NotNull Menu menu,
                                     @NotNull OpenCause cause,
                                     int page) {
        return new OpenContext(player, menu, cause, page);
    }

    public @NotNull PlayerComponent getPlayer() {
        return this.player;
    }

    public @NotNull Menu getMenu() {
        return this.menu;
    }

    public @NotNull OpenCause getCause() {
        return this.cause;
    }

    public int getPage() {
        return this.page;
    }
}
