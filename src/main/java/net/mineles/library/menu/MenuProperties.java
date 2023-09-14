package net.mineles.library.menu;

import com.cryptomorin.xseries.XSound;
import net.mineles.library.node.Node;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MenuProperties {
    private final @Nullable Node node;

    private final @NotNull String name;
    private final @Nullable String parent;
    private final @NotNull InventoryProperties inventoryProperties;

    private final @Nullable XSound openSound;
    private final @Nullable XSound closeSound;

    MenuProperties(@Nullable Node node,
                   @NotNull String name,
                   @Nullable String parent,
                   @NotNull InventoryProperties inventoryProperties,
                   @Nullable XSound openSound,
                   @Nullable XSound closeSound) {
        this.node = node;
        this.name = name;
        this.parent = parent;
        this.inventoryProperties = inventoryProperties;
        this.openSound = openSound;
        this.closeSound = closeSound;
    }

    @Nullable Node getNode() {
        return this.node;
    }

    @NotNull String getName() {
        return this.name;
    }

    @Nullable String getParent() {
        return this.parent;
    }

    @NotNull InventoryProperties getInventoryProperties() {
        return this.inventoryProperties;
    }

    @NotNull String getTitle() {
        return this.inventoryProperties.getTitle();
    }

    int getSize() {
        return this.inventoryProperties.getSize();
    }

    @NotNull InventoryType getInventoryType() {
        return this.inventoryProperties.getType();
    }

    @Nullable XSound getOpenSound() {
        return this.openSound;
    }

    @Nullable XSound getCloseSound() {
        return this.closeSound;
    }

    static @NotNull Builder newBuilder() {
        return new Builder();
    }

    static final class Builder {
        private Node node;

        private String name;
        private String parent;
        private InventoryProperties.Builder inventoryPropertiesBuilder;

        private XSound openSound;
        private XSound closeSound;

        private Builder() {
            this.inventoryPropertiesBuilder = InventoryProperties.newBuilder();
        }

        @NotNull Builder node(@Nullable Node node) {
            this.node = node;
            return this;
        }

        @NotNull Builder name(@NotNull String name) {
            this.name = name;
            return this;
        }

        @NotNull Builder parent(@Nullable String parent) {
            this.parent = parent;
            return this;
        }

        @NotNull Builder title(@NotNull String title) {
            this.inventoryPropertiesBuilder.title(title);
            return this;
        }

        @NotNull Builder size(int size) {
            this.inventoryPropertiesBuilder.size(size);
            return this;
        }

        @NotNull Builder type(@NotNull InventoryType type) {
            this.inventoryPropertiesBuilder.type(type);
            return this;
        }

        @NotNull Builder openSound(@Nullable XSound openSound) {
            this.openSound = openSound;
            return this;
        }

        @NotNull Builder closeSound(@Nullable XSound closeSound) {
            this.closeSound = closeSound;
            return this;
        }

        @NotNull MenuProperties build() {
            return new MenuProperties(this.node, this.name, this.parent, this.inventoryPropertiesBuilder.build(), this.openSound, this.closeSound);
        }
    }
}
