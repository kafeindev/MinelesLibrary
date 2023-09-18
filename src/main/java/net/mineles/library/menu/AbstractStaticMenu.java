package net.mineles.library.menu;

import com.google.common.collect.Maps;
import net.mineles.library.components.PlayerComponent;
import net.mineles.library.menu.button.Button;
import net.mineles.library.menu.misc.contexts.OpenContext;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public abstract class AbstractStaticMenu extends AbstractMenu {
    private Inventory inventory;

    protected AbstractStaticMenu(@NotNull MenuProperties properties,
                                 @NotNull Set<Button> buttons) {
        super(properties, buttons);
    }

    @Override
    @NotNull Inventory createInventory(@NotNull OpenContext context) {
        if (this.inventory == null) {
            this.inventory = super.createInventory(context);
        }

        return this.inventory;
    }

    @Override
    @NotNull Map<String, String> createTitlePlaceholders(@NotNull PlayerComponent player, int page) {
        return Maps.newHashMap();
    }

    @Override
    public @NotNull MenuType getType() {
        return MenuType.STATIC;
    }
}
