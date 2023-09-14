package net.mineles.library.menu;

import com.cryptomorin.xseries.XSound;
import com.google.common.collect.Sets;
import net.mineles.library.components.PlayerComponent;
import net.mineles.library.menu.button.Button;
import net.mineles.library.menu.misc.ClickResult;
import net.mineles.library.menu.misc.contexts.ClickContext;
import net.mineles.library.node.Node;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

abstract class AbstractMenu implements Menu {
    private final @NotNull MenuProperties properties;
    private final @NotNull Set<Button> buttons;

    protected AbstractMenu(@NotNull MenuProperties properties) {
        this(properties, Sets.newHashSet());
    }

    protected AbstractMenu(@NotNull MenuProperties properties,
                           @NotNull Set<Button> buttons) {
        this.properties = properties;
        this.buttons = buttons;
    }

    @Override
    public boolean open(@NotNull PlayerComponent player) {
        return open(player, 0);
    }

    @Override
    public boolean open(@NotNull PlayerComponent player, int page) {
        return false;
    }

    @NotNull Inventory createInventory(@NotNull PlayerComponent player, int page) {
        Map<String, String> placeholders = createTitlePlaceholders(player, page);
        return getInventoryProperties().createInventory(placeholders);
    }

    @NotNull Map<String, String> createTitlePlaceholders(@NotNull PlayerComponent player, int page) {
        return Map.of("%player%", player.getName(), "%page%", String.valueOf(page + 1));
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
    public @NotNull ClickResult click(@NotNull ClickContext context) {
        Button button = context.getButton();
        return button.click(context);
    }

    @Override
    public @NotNull MenuProperties getProperties() {
        return this.properties;
    }

    @Override
    public @Nullable Node getNode() {
        return this.properties.getNode();
    }

    @Override
    public @NotNull String getName() {
        return this.properties.getName();
    }

    @Override
    public @Nullable String getParent() {
        return this.properties.getParent();
    }

    @Override
    public @NotNull InventoryProperties getInventoryProperties() {
        return this.properties.getInventoryProperties();
    }

    @Override
    public @NotNull String getTitle() {
        return this.properties.getTitle();
    }

    @Override
    public @NotNull InventoryType getInventoryType() {
        return this.properties.getInventoryType();
    }

    @Override
    public int getSize() {
        return this.properties.getSize();
    }

    @Override
    public @NotNull Set<Button> getButtons() {
        return this.buttons;
    }

    @Override
    public @NotNull Optional<Button> findButton(@NotNull String name) {
        return this.buttons.stream()
                .filter(button -> button.getName().equals(name))
                .findFirst();
    }

    @Override
    public @NotNull Optional<Button> findButton(int slot) {
        return this.buttons.stream()
                .filter(button -> button.hasSlot(slot))
                .findFirst();
    }

    @Override
    public void putButtons(@NotNull Button... buttons) {
        for (Button button : buttons) {
            putButton(button);
        }
    }

    @Override
    public void putButtons(@NotNull Iterable<Button> buttons) {
        for (Button button : buttons) {
            putButton(button);
        }
    }

    @Override
    public void putButton(@NotNull Button button) {
        this.buttons.add(button);
    }

    @Override
    public void removeButtons(@NotNull Button... buttons) {
        for (Button button : buttons) {
            removeButton(button);
        }
    }

    @Override
    public void removeButtons(@NotNull Iterable<Button> buttons) {
        for (Button button : buttons) {
            removeButton(button);
        }
    }

    @Override
    public void removeButton(@NotNull Button button) {
        this.buttons.remove(button);
    }

    @Override
    public void removeButton(@NotNull String name) {
        findButton(name).ifPresent(this::removeButton);
    }

    @Override
    public @Nullable XSound getOpenSound() {
        return this.properties.getOpenSound();
    }

    @Override
    public @Nullable XSound getCloseSound() {
        return this.properties.getCloseSound();
    }
}
