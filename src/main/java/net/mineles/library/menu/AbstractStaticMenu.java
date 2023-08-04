package net.mineles.library.menu;

import net.mineles.library.plugin.BukkitPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractStaticMenu extends AbstractMenu implements StaticMenu {
    protected AbstractStaticMenu(@NotNull BukkitPlugin plugin, @NotNull String name) {
        super(plugin, name);
    }
}
