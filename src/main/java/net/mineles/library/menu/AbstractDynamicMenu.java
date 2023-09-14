package net.mineles.library.menu;

import net.mineles.library.menu.button.Button;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class AbstractDynamicMenu extends AbstractMenu {
    protected AbstractDynamicMenu(@NotNull MenuProperties properties) {
        super(properties);
    }

    protected AbstractDynamicMenu(@NotNull MenuProperties properties,
                                  @NotNull Set<Button> buttons) {
        super(properties, buttons);
    }

    @Override
    public @NotNull MenuType getType() {
        return MenuType.DYNAMIC;
    }
}
