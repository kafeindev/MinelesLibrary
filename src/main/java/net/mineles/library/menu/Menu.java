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
    static @NotNull Menu fromConfig(@NotNull String name,
                                    @NotNull Config config) {
        return fromNode(name, config.getNode(), MenuType.DYNAMIC);
    }

    static @NotNull Menu fromConfig(@NotNull String name,
                                    @NotNull Config config,
                                    @NotNull MenuType type) {
        return fromNode(name, config.getNode(), type);
    }

    static @NotNull Menu fromConfig(@NotNull String name,
                                    @NotNull Class<?> clazz,
                                    @NotNull String resource,
                                    @NotNull String... path) {
        Config config = ConfigBuilder.builder(path)
                .initFromResource(clazz, resource)
                .build();
        return fromConfig(name, config);
    }

    static @NotNull Menu fromNode(@NotNull String name,
                                  @NotNull ConfigurationNode node) {
        return fromNode(name, node, MenuType.DYNAMIC);
    }

    static @NotNull Menu fromNode(@NotNull String name,
                                  @NotNull ConfigurationNode node,
                                  @NotNull MenuType type) {
        return new MenuBuilder()
                .name(name)
                .node(node)
                .type(type)
                .propertiesFromNode(node)
                .buttonsFromNode(node)
                .build();
    }

    static @NotNull MenuBuilder newBuilder() {
        return new MenuBuilder();
    }

    @NotNull Viewer open(@NotNull PlayerComponent player);

    @NotNull Viewer open(@NotNull PlayerComponent player, int page);

    @NotNull Viewer open(@NotNull PlayerComponent player, int page, @NotNull OpenCause cause);

    @Nullable Viewer nextPage(@NotNull UUID uniqueId);

    @NotNull Viewer nextPage(@NotNull PlayerComponent player);

    @NotNull Viewer nextPage(@NotNull Viewer viewer);

    @Nullable Viewer previousPage(@NotNull UUID uniqueId);

    @NotNull Viewer previousPage(@NotNull PlayerComponent player);

    @NotNull Viewer previousPage(@NotNull Viewer viewer);

    @Nullable Viewer refresh(@NotNull UUID uniqueId);

    @NotNull Viewer refresh(@NotNull PlayerComponent player);

    @NotNull Viewer refresh(@NotNull Viewer viewer);

    @Nullable Viewer refreshButton(@NotNull UUID uniqueId, int slot);

    @Nullable Viewer refreshButton(@NotNull UUID uniqueId, @NotNull String buttonName);

    @Nullable Viewer refreshButton(@NotNull PlayerComponent player, int slot);

    @Nullable Viewer refreshButton(@NotNull PlayerComponent player, @NotNull String buttonName);

    @NotNull Viewer refreshButton(@NotNull Viewer viewer, int slot);

    @NotNull Viewer refreshButton(@NotNull Viewer viewer, @NotNull String buttonName);

    @Nullable Viewer close(@NotNull UUID uniqueId);

    @Nullable Viewer close(@NotNull PlayerComponent player);

    @NotNull Viewer close(@NotNull Viewer viewer);

    @NotNull ClickResult click(@NotNull ClickContext context);

    @NotNull ClickResult clickEmptySlot(@NotNull ClickContext context);

    @NotNull ClickResult clickBottomInventory(@NotNull ClickContext context);

    @NotNull ClickActionCollection getClickActions();

    void registerClickAction(@NotNull String key, @NotNull ClickAction action);

    void unregisterClickAction(@NotNull String key);

    @NotNull ViewersHolder getViewers();

    boolean isViewing(@NotNull PlayerComponent player);

    boolean isViewing(@NotNull UUID uniqueId);

    void stopViewing();

    @NotNull MenuProperties getProperties();

    @Nullable ConfigurationNode getNode();

    @NotNull String getName();

    @NotNull MenuType getType();

    @NotNull InventoryProperties getInventoryProperties();

    @NotNull String getTitle();

    @NotNull InventoryType getInventoryType();

    int getSize();

    @Nullable Set<Button> loadButtonsFromNode();

    @Nullable Set<Button> loadButtonsFromNode(@NotNull ConfigurationNode node);

    @NotNull Set<Button> getButtons();

    @NotNull Optional<Button> findButton(@NotNull String name);

    @NotNull Optional<Button> findButton(int slot);

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
