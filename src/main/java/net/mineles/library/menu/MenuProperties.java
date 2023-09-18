package net.mineles.library.menu;

import com.cryptomorin.xseries.XSound;
import net.mineles.library.node.Node;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public final class MenuProperties {
    private final @Nullable Node node;

    private final @NotNull String name;
    private final @NotNull InventoryProperties inventoryProperties;

    private XSound openSound;
    private XSound closeSound;

    MenuProperties(@Nullable Node node,
                   @NotNull String name,
                   @NotNull InventoryProperties inventoryProperties,
                   @Nullable XSound openSound,
                   @Nullable XSound closeSound) {
        this.node = node;
        this.name = name;
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

    void setOpenSound(@Nullable XSound openSound) {
        this.openSound = openSound;
    }

    @Nullable XSound getCloseSound() {
        return this.closeSound;
    }

    void setCloseSound(@Nullable XSound closeSound) {
        this.closeSound = closeSound;
    }

    static @NotNull Builder newBuilder() {
        return new Builder();
    }

    static final class Builder {
        private Node node;

        private String name;
        private InventoryProperties.Builder inventoryPropertiesBuilder;

        private XSound openSound;
        private XSound closeSound;

        private Builder() {
            this.inventoryPropertiesBuilder = InventoryProperties.newBuilder();
        }

        @NotNull Node node() {
            return checkNotNull(this.node, "node");
        }

        @NotNull Builder node(@Nullable Node node) {
            this.node = node;
            return this;
        }

        @Nullable String name() {
            return this.name;
        }

        @NotNull Builder name(@NotNull String name) {
            this.name = name;
            return this;
        }

        @Nullable String title() {
            return this.inventoryPropertiesBuilder.title();
        }

        @NotNull Builder title(@NotNull String title) {
            this.inventoryPropertiesBuilder.title(title);
            return this;
        }

        int size() {
            return this.inventoryPropertiesBuilder.size();
        }

        @NotNull Builder size(int size) {
            this.inventoryPropertiesBuilder.size(size);
            return this;
        }

        @Nullable InventoryType type() {
            return this.inventoryPropertiesBuilder.type();
        }

        @NotNull Builder type(@NotNull InventoryType type) {
            this.inventoryPropertiesBuilder.type(type);
            return this;
        }

        @Nullable XSound openSound() {
            return this.openSound;
        }

        @NotNull Builder openSound(@Nullable XSound openSound) {
            this.openSound = openSound;
            return this;
        }

        @Nullable XSound closeSound() {
            return this.closeSound;
        }

        @NotNull Builder closeSound(@Nullable XSound closeSound) {
            this.closeSound = closeSound;
            return this;
        }

        @NotNull MenuProperties build() {
            return new MenuProperties(this.node, this.name, this.inventoryPropertiesBuilder.build(), this.openSound, this.closeSound);
        }
    }
}
