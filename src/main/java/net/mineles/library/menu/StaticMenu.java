package net.mineles.library.menu;

import net.mineles.library.menu.button.Button;

import java.util.Set;

final class StaticMenu extends AbstractStaticMenu {
    StaticMenu(MenuProperties properties) {
        super(properties);
    }

    StaticMenu(MenuProperties properties, Set<Button> buttons) {
        super(properties, buttons);
    }
}
