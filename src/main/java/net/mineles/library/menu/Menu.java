package net.mineles.library.menu;

import com.cryptomorin.xseries.XSound;
import net.mineles.library.components.PlayerComponent;
import net.mineles.library.configuration.Config;
import net.mineles.library.configuration.ConfigBuilder;
import net.mineles.library.menu.action.ClickAction;
import net.mineles.library.menu.action.ClickActionCollection;
import net.mineles.library.menu.button.Button;
import net.mineles.library.menu.button.ClickHandler;
import net.mineles.library.menu.misc.ClickResult;
import net.mineles.library.menu.misc.OpenCause;
import net.mineles.library.menu.misc.contexts.ClickContext;
import net.mineles.library.menu.view.Viewer;
import net.mineles.library.menu.view.ViewersHolder;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Menu {
    static Menu fromConfig(String name, Config config) {
        return fromNode(name, config.getNode(), MenuType.DYNAMIC);
    }

    static Menu fromConfig(String name, Config config, MenuType type) {
        return fromNode(name, config.getNode(), type);
    }

    static Menu fromConfig(String name, Class<?> clazz, String resource, String... path) {
        Config config = ConfigBuilder.builder(path)
                .resource(clazz, resource)
                .build();
        return fromConfig(name, config);
    }

    static Menu fromNode(String name, ConfigurationNode node) {
        return fromNode(name, node, MenuType.DYNAMIC);
    }

    static Menu fromNode(String name, ConfigurationNode node, MenuType type) {
        return new MenuBuilder()
                .name(name)
                .node(node)
                .type(type)
                .propertiesFromNode(node)
                .buttonsFromNode(node)
                .build();
    }

    static MenuBuilder newBuilder() {
        return new MenuBuilder();
    }

    Viewer open(@NotNull PlayerComponent player);

    Viewer open(@NotNull PlayerComponent player, int page);

    Viewer open(@NotNull PlayerComponent player, int page, @NotNull OpenCause cause);

    @Nullable Viewer nextPage(@NotNull UUID uniqueId);

    Viewer nextPage(@NotNull PlayerComponent player);

    Viewer nextPage(@NotNull Viewer viewer);

    @Nullable Viewer previousPage(@NotNull UUID uniqueId);

    Viewer previousPage(@NotNull PlayerComponent player);

    Viewer previousPage(@NotNull Viewer viewer);

    @Nullable Viewer refresh(@NotNull UUID uniqueId);

    Viewer refresh(@NotNull PlayerComponent player);

    Viewer refresh(@NotNull Viewer viewer);

    @Nullable Viewer refreshButton(@NotNull UUID uniqueId, int slot);

    @Nullable Viewer refreshButton(@NotNull UUID uniqueId, @NotNull String buttonName);

    @Nullable Viewer refreshButton(@NotNull PlayerComponent player, int slot);

    @Nullable Viewer refreshButton(@NotNull PlayerComponent player, @NotNull String buttonName);

    Viewer refreshButton(@NotNull Viewer viewer, int slot);

    Viewer refreshButton(@NotNull Viewer viewer, @NotNull String buttonName);

    @Nullable Viewer close(@NotNull UUID uniqueId);

    @Nullable Viewer close(@NotNull PlayerComponent player);

    Viewer close(@NotNull Viewer viewer);

    ClickResult click(@NotNull ClickContext context);

    ClickResult clickEmptySlot(@NotNull ClickContext context);

    ClickResult clickBottomInventory(@NotNull ClickContext context);

    ClickActionCollection getClickActions();

    void registerClickAction(@NotNull String key, @NotNull ClickAction action);

    void unregisterClickAction(@NotNull String key);

    ViewersHolder getViewers();

    boolean isViewing(@NotNull PlayerComponent player);

    boolean isViewing(@NotNull UUID uniqueId);

    void stopViewing();

    MenuProperties getProperties();

    @Nullable ConfigurationNode getNode();

    String getName();

    MenuType getType();

    InventoryProperties getInventoryProperties();

    String getTitle();

    InventoryType getInventoryType();

    int getSize();

    @Nullable Set<Button> loadButtonsFromNode();

    @Nullable Set<Button> loadButtonsFromNode(@NotNull ConfigurationNode node);

    Set<Button> getButtons();

    Optional<Button> findButton(@NotNull String name);

    Optional<Button> findButton(int slot);

    void putButtons(@NotNull Button... buttons);

    void putButtons(@NotNull Iterable<Button> buttons);

    void putButton(@NotNull Button button);

    void removeButtons(@NotNull Button... buttons);

    void removeButtons(@NotNull Iterable<Button> buttons);

    void removeButton(@NotNull Button button);

    void removeButton(@NotNull String name);

    @Nullable Button registerClickHandler(@NotNull String buttonName, @NotNull ClickHandler handler);

    @Nullable Button registerClickHandler(@NotNull Button button, @NotNull ClickHandler handler);

    @Nullable XSound getOpenSound();

    @Nullable XSound getCloseSound();
}
