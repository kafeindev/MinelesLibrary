package net.mineles.library.menu.button;

import net.mineles.library.menu.misc.ClickResult;
import net.mineles.library.menu.misc.contexts.ClickContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

final class ClickHandler {
    private final @NotNull Function<ClickContext, ClickResult> handler;

    ClickHandler(@NotNull Function<ClickContext, ClickResult> handler) {
        this.handler = handler;
    }

    @NotNull ClickResult click(@NotNull ClickContext context) {
        return this.handler.apply(context);
    }


}
