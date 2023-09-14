package net.mineles.library.menu;

import com.cryptomorin.xseries.XSound;
import com.google.common.collect.Sets;
import net.mineles.library.menu.button.Button;
import net.mineles.library.node.Node;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public final class MenuBuilder {
    private MenuType type;
    private MenuProperties.Builder propertiesBuilder;
    private Set<Button> buttons;

    MenuBuilder() {
        this.type = MenuType.DYNAMIC;
        this.propertiesBuilder = MenuProperties.newBuilder();
        this.buttons = Sets.newHashSet();
    }

    public static MenuBuilder builder() {
        return new MenuBuilder();
    }

    public @NotNull MenuType type() {
        return this.type;
    }

    public @NotNull MenuBuilder type(@NotNull MenuType type) {
        this.type = type;
        return this;
    }

    public @NotNull MenuProperties.Builder properties() {
        return this.propertiesBuilder;
    }

    public @NotNull MenuBuilder properties(@NotNull MenuProperties.Builder propertiesBuilder) {
        this.propertiesBuilder = propertiesBuilder;
        return this;
    }

    public @NotNull MenuBuilder node(@NotNull Node node) {
        this.propertiesBuilder.node(node);
        return this;
    }

    public @NotNull MenuBuilder name(@NotNull String name) {
        this.propertiesBuilder.name(name);
        return this;
    }

    public @NotNull MenuBuilder inventoryTitle(@NotNull String title) {
        this.propertiesBuilder.title(title);
        return this;
    }

    public @NotNull MenuBuilder inventorySize(int size) {
        this.propertiesBuilder.size(size);
        return this;
    }

    public @NotNull MenuBuilder inventoryType(@NotNull InventoryType type) {
        this.propertiesBuilder.type(type);
        return this;
    }

    public @NotNull MenuBuilder parent(@NotNull String parent) {
        this.propertiesBuilder.parent(parent);
        return this;
    }

    public @NotNull MenuBuilder openSound(@NotNull XSound openSound) {
        this.propertiesBuilder.openSound(openSound);
        return this;
    }

    public @NotNull MenuBuilder closeSound(@NotNull XSound closeSound) {
        this.propertiesBuilder.closeSound(closeSound);
        return this;
    }

    public @Nullable Set<Button> buttons() {
        return this.buttons;
    }

    public @NotNull MenuBuilder buttons(@NotNull Set<Button> buttons) {
        this.buttons = buttons;
        return this;
    }

    public @NotNull MenuBuilder buttonsFromNode() {
        Node parent = checkNotNull(this.propertiesBuilder.node(), "node");
        return buttonsFromNode(parent.node("menu", "buttons"));
    }

    public @NotNull MenuBuilder buttonsFromNode(@NotNull Node parent) {
        for (Node node : parent.childList()) {
            Button button = Button.fromNode(node);
            this.buttons.add(button);
        }

        return this;
    }

    public @NotNull MenuBuilder button(@NotNull Button button) {
        this.buttons.add(button);
        return this;
    }

    public @NotNull Menu build() {
        MenuProperties properties = this.propertiesBuilder.build();
        return switch (this.type) {
            case STATIC -> new StaticMenu(properties, this.buttons);
            case DYNAMIC -> new DynamicMenu(properties, this.buttons);
        };
    }
}
