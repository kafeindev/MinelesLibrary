package net.mineles.library.menu;

import net.mineles.library.libs.xseries.XSound;
import com.google.common.collect.Sets;
import net.mineles.library.components.PlayerComponent;
import net.mineles.library.menu.action.ClickAction;
import net.mineles.library.menu.action.ClickActionCollection;
import net.mineles.library.menu.button.Button;
import net.mineles.library.menu.button.ClickHandler;
import net.mineles.library.menu.misc.ClickResult;
import net.mineles.library.menu.misc.OpenCause;
import net.mineles.library.menu.misc.contexts.ClickContext;
import net.mineles.library.menu.misc.contexts.OpenContext;
import net.mineles.library.menu.view.Viewer;
import net.mineles.library.menu.view.ViewersHolder;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.mineles.library.libs.configurate.ConfigurationNode;

import java.util.*;

abstract class AbstractMenu implements Menu {
    private final MenuProperties properties;
    private final Set<Button> buttons;
    private final ClickActionCollection clickActions;
    private final ViewersHolder viewers;

    protected AbstractMenu(MenuProperties properties) {
        this.properties = properties;
        this.buttons = properties.getNode() == null ? Sets.newHashSet() : loadButtonsFromNode();
        this.clickActions = new ClickActionCollection();
        this.viewers = ViewersHolder.create();
    }

    protected AbstractMenu(MenuProperties properties, Set<Button> buttons) {
        this.properties = properties;
        this.buttons = buttons;
        this.clickActions = new ClickActionCollection();
        this.viewers = ViewersHolder.create();
    }

    @Override
    public Viewer open(@NotNull PlayerComponent player) {
        return open(player, 0);
    }

    @Override
    public Viewer open(@NotNull PlayerComponent player, int page) {
        return open(player, page, OpenCause.OPEN);
    }

    @Override
    public Viewer open(@NotNull PlayerComponent player, int page, @NotNull OpenCause cause) {
        OpenContext context = OpenContext.create(player, this, cause, page);

        Inventory inventory = createInventory(context);
        InventoryView view = player.openInventory(inventory);

        player.playSound(getOpenSound());

        Viewer viewer = Viewer.create(player, view, getName(), page);
        return this.viewers.addViewer(viewer);
    }

    @NotNull Inventory createInventory(@NotNull OpenContext context) {
        Map<String, String> placeholders = createTitlePlaceholders(context.getPlayer(), context.getPage());

        Inventory inventory = getInventoryProperties().createInventory(context.getPlayer().getHandle(), placeholders);
        getButtons().forEach(button -> {
            Map<Integer, ItemStack> itemStackMap = button.createItemStacks(context);
            itemStackMap.forEach(inventory::setItem);
        });

        return inventory;
    }

    @NotNull Map<String, String> createTitlePlaceholders(@NotNull PlayerComponent player, int page) {
        return Map.of("%player%", player.getName(), "%page%", String.valueOf(page + 1));
    }

    @Override
    public @Nullable Viewer nextPage(@NotNull UUID uniqueId) {
        Viewer viewer = this.viewers.getViewer(uniqueId);
        if (viewer == null) {
            return null;
        }

        return nextPage(viewer);
    }

    @Override
    public Viewer nextPage(@NotNull PlayerComponent player) {
        Viewer viewer = this.viewers.getViewer(player.getUniqueId());
        if (viewer == null) {
            return open(player);
        }

        return nextPage(viewer);
    }

    @Override
    public Viewer nextPage(@NotNull Viewer viewer) {
        open(viewer.getPlayer(), viewer.getPage() + 1, OpenCause.CHANGE_PAGE);
        return viewer;
    }

    @Override
    public @Nullable Viewer previousPage(@NotNull UUID uniqueId) {
        Viewer viewer = this.viewers.getViewer(uniqueId);
        if (viewer == null) {
            return null;
        }

        return previousPage(viewer);
    }

    @Override
    public Viewer previousPage(@NotNull PlayerComponent player) {
        Viewer viewer = this.viewers.getViewer(player.getUniqueId());
        if (viewer == null) {
            return open(player);
        }

        return previousPage(viewer);
    }

    @Override
    public Viewer previousPage(@NotNull Viewer viewer) {
        open(viewer.getPlayer(), viewer.getPage() - 1, OpenCause.CHANGE_PAGE);
        return viewer;
    }

    @Override
    public @Nullable Viewer refresh(@NotNull UUID uniqueId) {
        Viewer viewer = this.viewers.getViewer(uniqueId);
        if (viewer == null) {
            return null;
        }

        return refresh(viewer);
    }

    @Override
    public Viewer refresh(@NotNull PlayerComponent player) {
        Viewer viewer = this.viewers.getViewer(player.getUniqueId());
        if (viewer == null) {
            return open(player);
        }

        return refresh(viewer);
    }

    @Override
    public Viewer refresh(@NotNull Viewer viewer) {
        OpenContext context = OpenContext.create(viewer.getPlayer(), this, OpenCause.REFRESH, viewer.getPage());

        Inventory inventory = viewer.getView().getTopInventory();
        getButtons().forEach(button -> {
            Map<Integer, ItemStack> itemStackMap = button.createItemStacks(context);
            itemStackMap.forEach(inventory::setItem);
        });

        return viewer;
    }

    @Override
    public @Nullable Viewer refreshButton(@NotNull UUID uniqueId, int slot) {
        Viewer viewer = this.viewers.getViewer(uniqueId);
        if (viewer == null) {
            return null;
        }

        return refreshButton(viewer, slot);
    }

    @Override
    public @Nullable Viewer refreshButton(@NotNull UUID uniqueId, @NotNull String buttonName) {
        Viewer viewer = this.viewers.getViewer(uniqueId);
        if (viewer == null) {
            return null;
        }

        return refreshButton(viewer, buttonName);
    }

    @Override
    public Viewer refreshButton(@NotNull PlayerComponent player, int slot) {
        Viewer viewer = this.viewers.getViewer(player.getUniqueId());
        if (viewer == null) {
            return open(player);
        }

        return refreshButton(viewer, slot);
    }

    @Override
    public Viewer refreshButton(@NotNull PlayerComponent player, @NotNull String buttonName) {
        Viewer viewer = this.viewers.getViewer(player.getUniqueId());
        if (viewer == null) {
            return open(player);
        }

        return refreshButton(viewer, buttonName);
    }

    @Override
    public Viewer refreshButton(@NotNull Viewer viewer, int slot) {
        Inventory inventory = viewer.getView().getTopInventory();
        findButton(slot).ifPresent(button -> {
            OpenContext context = OpenContext.create(viewer.getPlayer(), this, OpenCause.REFRESH, viewer.getPage());

            Map<Integer, ItemStack> itemStackMap = button.createItemStacks(context);
            itemStackMap.forEach(inventory::setItem);
        });

        return viewer;
    }

    @Override
    public Viewer refreshButton(@NotNull Viewer viewer, @NotNull String buttonName) {
        Inventory inventory = viewer.getView().getTopInventory();
        findButton(buttonName).ifPresent(button -> {
            OpenContext context = OpenContext.create(viewer.getPlayer(), this, OpenCause.REFRESH, viewer.getPage());

            Map<Integer, ItemStack> itemStackMap = button.createItemStacks(context);
            itemStackMap.forEach(inventory::setItem);
        });

        return viewer;
    }

    @Override
    public @Nullable Viewer close(@NotNull UUID uniqueId) {
        Viewer viewer = this.viewers.getViewer(uniqueId);
        if (viewer == null) {
            return null;
        }

        return close(viewer);
    }

    @Override
    public @Nullable Viewer close(@NotNull PlayerComponent player) {
        return close(player.getUniqueId());
    }

    @Override
    public Viewer close(@NotNull Viewer viewer) {
        if (viewer.isClosed()) {
            return viewer;
        }
        viewer.setClosed(true);

        PlayerComponent player = viewer.getPlayer();
        player.closeInventory();
        player.playSound(getCloseSound());

        return this.viewers.removeViewer(player.getUniqueId());
    }

    @Override
    public ClickResult click(@NotNull ClickContext context) {
        Button button = context.getButton();
        if (button == null) {
            return ClickResult.CANCELLED;
        }

        PlayerComponent player = context.getPlayer();
        player.playSound(button.getClickSound());

        button.getClickActions().forEach(registeredClickAction -> {
            ClickAction clickAction = this.clickActions.findAction(registeredClickAction).orElse(null);
            if (clickAction == null) {
                return;
            }

            clickAction.accept(context, registeredClickAction);
        });

        return button.click(context);
    }

    @Override
    public ClickResult clickEmptySlot(@NotNull ClickContext context) {
        return ClickResult.CANCELLED;
    }

    @Override
    public ClickResult clickBottomInventory(@NotNull ClickContext context) {
        return ClickResult.CANCELLED;
    }

    @Override
    public ClickActionCollection getClickActions() {
        return this.clickActions;
    }

    @Override
    public void registerClickAction(@NotNull String key, @NotNull ClickAction action) {
        this.clickActions.registerAction(key, action);
    }

    @Override
    public void unregisterClickAction(@NotNull String key) {
        this.clickActions.unregisterAction(key);
    }

    @Override
    public ViewersHolder getViewers() {
        return this.viewers;
    }

    @Override
    public boolean isViewing(@NotNull PlayerComponent player) {
        return isViewing(player.getUniqueId());
    }

    @Override
    public boolean isViewing(@NotNull UUID uniqueId) {
        return this.viewers.containsViewer(uniqueId);
    }

    @Override
    public void stopViewing() {
        Map<UUID, Viewer> viewersSafe = this.viewers.getViewersSafe();
        viewersSafe.values().forEach(this::close);
    }

    @Override
    public MenuProperties getProperties() {
        return this.properties;
    }

    @Override
    public @Nullable ConfigurationNode getNode() {
        return this.properties.getNode();
    }

    @Override
    public String getName() {
        return this.properties.getName();
    }

    @Override
    public InventoryProperties getInventoryProperties() {
        return this.properties.getInventoryProperties();
    }

    @Override
    public String getTitle() {
        return this.properties.getTitle();
    }

    @Override
    public InventoryType getInventoryType() {
        return this.properties.getInventoryType();
    }

    @Override
    public int getSize() {
        return this.properties.getSize();
    }

    @Override
    public @Nullable Set<Button> loadButtonsFromNode() {
        if (getNode() == null) {
            return null;
        }

        ConfigurationNode node = getNode().node("buttons");
        return loadButtonsFromNode(node);
    }

    @Override
    public @Nullable Set<Button> loadButtonsFromNode(@NotNull ConfigurationNode node) {
        Set<Button> nodeButtons = new HashSet<>();

        for (ConfigurationNode child : node.childrenMap().values()) {
            Button button = Button.fromNode(child);
            nodeButtons.add(button);
        }

        return nodeButtons;
    }

    @Override
    public Set<Button> getButtons() {
        return this.buttons;
    }

    @Override
    public Optional<Button> findButton(@NotNull String name) {
        return this.buttons.stream()
                .filter(button -> button.getName().equals(name))
                .findFirst();
    }

    @Override
    public Optional<Button> findButton(int slot) {
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
    public @Nullable Button registerClickHandler(@NotNull String buttonName, @NotNull ClickHandler handler) {
        Optional<Button> optionalButton = findButton(buttonName);
        return optionalButton.map(button -> registerClickHandler(button, handler)).orElse(null);
    }

    @Override
    public @Nullable Button registerClickHandler(@NotNull Button button, @NotNull ClickHandler handler) {
        button.setClickHandler(handler);
        return button;
    }

    @Override
    public @Nullable XSound getOpenSound() {
        return this.properties.getOpenSound();
    }

    @Override
    public @Nullable XSound getCloseSound() {
        return this.properties.getCloseSound();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Menu)) return false;
        if (obj == this) return true;

        Menu menu = (Menu) obj;
        return menu.getName().equals(getName());
    }
}
