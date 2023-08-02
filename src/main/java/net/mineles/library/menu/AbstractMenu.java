package net.mineles.library.menu;

import net.mineles.library.component.PlayerComponent;
import net.mineles.library.menu.button.Button;
import net.mineles.library.plugin.BukkitPlugin;
import net.mineles.library.property.Attribute;
import net.mineles.library.property.AttributeMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

public abstract class AbstractMenu implements Menu {
    protected final @NotNull BukkitPlugin plugin;
    protected final @NotNull AttributeMap attributes;

    protected AbstractMenu(@NotNull BukkitPlugin plugin,
                           @NotNull String name) {
        this.plugin = plugin;
        this.attributes = AttributeMap.create(MenuAttributes.NAME, name);
    }

    @Override
    public void initialize() {

    }

    @Override
    public boolean open(@NotNull PlayerComponent player) {
        return open(player, 0);
    }

    @Override
    public boolean open(@NotNull PlayerComponent player, int page) {
        return false;
    }

    @Override
    public boolean close(@NotNull PlayerComponent player) {
        return false;
    }

    @Override
    public boolean refresh(@NotNull PlayerComponent player) {
        return false;
    }

    @Override
    public boolean refreshButton(@NotNull PlayerComponent player, int slot) {
        return false;
    }

    @Override
    public boolean refreshButton(@NotNull PlayerComponent player, @NotNull String buttonName) {
        return false;
    }

    @Override
    public @NotNull AttributeMap getAttributes() {
        return this.attributes;
    }

    @Override
    public @Nullable <T> Attribute<T> getAttribute(@NotNull MenuAttributes attribute) {
        return this.attributes.get(attribute);
    }

    @Override
    public @Nullable <T> Attribute<T> getAttribute(@NotNull MenuAttributes attribute, @NotNull Class<T> type) {
        return this.attributes.get(attribute, type);
    }

    @Override
    public @Nullable <T> Attribute<T> getAttribute(@NotNull String name) {
        return this.attributes.get(name);
    }

    @Override
    public @Nullable <T> Attribute<T> getAttribute(@NotNull String name, @NotNull Class<T> type) {
        return this.attributes.get(name, type);
    }

    @Override
    public @NotNull Optional<Button> findButton(int slot) {
        Set<Button> buttons = getAttribute(MenuAttributes.BUTTONS, Set.class).getValue();
        return buttons.stream().filter(button -> {
            AttributeMap attributes = button.getAttributes();
            return
        }
    }

    @Override
    public @NotNull Optional<Button> findButton(@NotNull String name) {
        Set<Button> buttons = getAttribute(MenuAttributes.BUTTONS, Set.class).getValue();
        return buttons.stream().filter(button -> button.getName().equals(name)).findFirst();
    }
}
