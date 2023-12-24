package net.mineles.library.menu;

import net.mineles.library.menu.button.Button;

import java.util.Set;

public abstract class AbstractDynamicMenu extends AbstractMenu {
    public AbstractDynamicMenu(MenuProperties properties) {
        super(properties);
    }

    protected AbstractDynamicMenu(MenuProperties properties, Set<Button> buttons) {
        super(properties, buttons);
    }

    @Override
    public MenuType getType() {
        return MenuType.DYNAMIC;
    }
}
