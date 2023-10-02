package net.mineles.library.menu;

import net.mineles.library.menu.button.Button;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

final class StaticMenu extends AbstractStaticMenu {
    StaticMenu(@NotNull MenuProperties properties) {
        super(properties);
    }

    StaticMenu(@NotNull MenuProperties properties, @NotNull Set<Button> buttons) {
        super(properties, buttons);
    }
}
