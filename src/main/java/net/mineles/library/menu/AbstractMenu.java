package net.mineles.library.menu;

import com.cryptomorin.xseries.XSound;
import net.mineles.library.components.PlayerComponent;
import net.mineles.library.menu.button.Button;
import net.mineles.library.node.Node;
import net.mineles.library.plugin.BukkitPlugin;
import net.mineles.library.property.Attribute;
import net.mineles.library.property.AttributeMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

abstract class AbstractMenu implements Menu {
    protected final @NotNull BukkitPlugin plugin;
    protected final @NotNull AttributeMap attributes;

    protected AbstractMenu(@NotNull BukkitPlugin plugin,
                           @NotNull String name) {
        this.plugin = plugin;
        this.attributes = AttributeMap.create(MenuAttributes.NAME, name);
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
    public @NotNull Node getNode() {
        return this.attributes.getValue(MenuAttributes.NODE);
    }

    @Override
    public @NotNull String getName() {
        return this.attributes.getValue(MenuAttributes.NAME);
    }

    @Override
    public @NotNull MenuType getType() {
        return this.attributes.getValue(MenuAttributes.TYPE);
    }

    @Override
    public @NotNull String getTitle() {
        return this.attributes.getValue(MenuAttributes.TITLE);
    }

    @Override
    public int getSize() {
        return this.attributes.getValue(MenuAttributes.SIZE);
    }

    @Override
    public @Nullable String getParent() {
        return this.attributes.getValue(MenuAttributes.PARENT);
    }

    @Override
    public @NotNull Set<Button> getButtons() {
        return this.attributes.getValue(MenuAttributes.BUTTONS);
    }

    @Override
    public @NotNull Optional<Button> findButton(int slot) {
        Set<Button> buttons = getAttribute(MenuAttributes.BUTTONS, Set.class).getValue();
        return buttons.stream()
                .filter(button -> button.hasSlot(slot))
                .findFirst();
    }

    @Override
    public @NotNull Optional<Button> findButton(@NotNull String name) {
        Set<Button> buttons = getAttribute(MenuAttributes.BUTTONS, Set.class).getValue();
        return buttons.stream()
                .filter(button -> button.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public @Nullable XSound getOpenSound() {
        return this.attributes.getValue(MenuAttributes.OPEN_SOUND);
    }

    @Override
    public @Nullable XSound getCloseSound() {
        return this.attributes.getValue(MenuAttributes.CLOSE_SOUND);
    }
}
