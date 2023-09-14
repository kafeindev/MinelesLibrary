package net.mineles.library.menu;

import net.mineles.library.menu.button.Button;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

final class DynamicMenu extends AbstractDynamicMenu {
    DynamicMenu(@NotNull MenuProperties properties) {
        super(properties);
    }

    DynamicMenu(@NotNull MenuProperties properties, @NotNull Set<Button> buttons) {
        super(properties, buttons);
    }
}
