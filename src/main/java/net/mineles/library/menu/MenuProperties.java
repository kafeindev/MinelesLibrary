package net.mineles.library.menu;

import net.mineles.library.libs.xseries.XSound;
import net.mineles.library.configuration.Config;
import net.mineles.library.configuration.ConfigBuilder;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.mineles.library.libs.configurate.ConfigurationNode;

import static com.google.common.base.Preconditions.checkNotNull;

public final class MenuProperties {
    private final @Nullable ConfigurationNode node;

    private final String name;
    private final InventoryProperties inventoryProperties;

    //private long cooldown;

    private XSound openSound;
    private XSound closeSound;

    public MenuProperties(@Nullable ConfigurationNode node, String name, InventoryProperties inventoryProperties, @Nullable XSound openSound, @Nullable XSound closeSound) {
        this.node = node;
        this.name = name;
        this.inventoryProperties = inventoryProperties;
        this.openSound = openSound;
        this.closeSound = closeSound;
    }

    public static MenuProperties fromConfig(String name, Config config) {
        return fromNode(name, config.getNode().node("menu"));
    }

    public static MenuProperties fromConfig(String name, Class<?> clazz, String resource, String... path) {
        Config config = ConfigBuilder.builder(path)
                .resource(clazz, resource)
                .build();
        return fromNode(name, config.getNode().node("menu"));
    }

    public static MenuProperties fromNode(String name, ConfigurationNode node) {
        return new Builder()
                .node(node)
                .name(name)
                .title(node.node("title").getString("NONE"))
                .size(node.node("size").getInt(9))
                .type(node.node("type").getString())
                .openSound(node.node("open-sound").getString())
                .closeSound(node.node("close-sound").getString())
                .build();
    }

    public @Nullable ConfigurationNode getNode() {
        return this.node;
    }

    public String getName() {
        return this.name;
    }

    public InventoryProperties getInventoryProperties() {
        return this.inventoryProperties;
    }

    public String getTitle() {
        return this.inventoryProperties.getTitle();
    }

    public int getSize() {
        return this.inventoryProperties.getSize();
    }

    public InventoryType getInventoryType() {
        return this.inventoryProperties.getType();
    }

    public @Nullable XSound getOpenSound() {
        return this.openSound;
    }

    public void setOpenSound(@Nullable XSound openSound) {
        this.openSound = openSound;
    }

    public @Nullable XSound getCloseSound() {
        return this.closeSound;
    }

    public void setCloseSound(@Nullable XSound closeSound) {
        this.closeSound = closeSound;
    }

    @NotNull Builder toBuilder() {
        return new Builder()
                .node(this.node)
                .name(this.name)
                .title(this.inventoryProperties.getTitle())
                .size(this.inventoryProperties.getSize())
                .type(this.inventoryProperties.getType())
                .openSound(this.openSound)
                .closeSound(this.closeSound);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private ConfigurationNode node;

        private String name;
        private InventoryProperties.Builder inventoryPropertiesBuilder;

        private XSound openSound;
        private XSound closeSound;

        private Builder() {
            this.inventoryPropertiesBuilder = InventoryProperties.newBuilder();
        }

        public ConfigurationNode node() {
            return checkNotNull(this.node, "node");
        }

        public Builder node(@Nullable ConfigurationNode node) {
            this.node = node;
            return this;
        }

        public @Nullable String name() {
            return this.name;
        }

        public Builder name(@NotNull String name) {
            this.name = name;
            return this;
        }

        public @Nullable String title() {
            return this.inventoryPropertiesBuilder.title();
        }

        public Builder title(@NotNull String title) {
            this.inventoryPropertiesBuilder.title(title);
            return this;
        }

        public int size() {
            return this.inventoryPropertiesBuilder.size();
        }

        public Builder size(int size) {
            this.inventoryPropertiesBuilder.size(size);
            return this;
        }

        public @Nullable InventoryType type() {
            return this.inventoryPropertiesBuilder.type();
        }

        public Builder type(@NotNull InventoryType type) {
            this.inventoryPropertiesBuilder.type(type);
            return this;
        }

        public Builder type(@Nullable String type) {
            this.inventoryPropertiesBuilder.type(type);
            return this;
        }

        public @Nullable XSound openSound() {
            return this.openSound;
        }

        public Builder openSound(@Nullable XSound openSound) {
            this.openSound = openSound;
            return this;
        }

        public Builder openSound(@Nullable String openSound) {
            if (openSound != null) {
                this.openSound = XSound.matchXSound(openSound).orElse(null);
            }
            return this;
        }

        public @Nullable XSound closeSound() {
            return this.closeSound;
        }

        public Builder closeSound(@Nullable XSound closeSound) {
            this.closeSound = closeSound;
            return this;
        }

        public Builder closeSound(@Nullable String closeSound) {
            if (closeSound != null) {
                this.closeSound = XSound.matchXSound(closeSound).orElse(null);
            }
            return this;
        }

        public MenuProperties build() {
            return new MenuProperties(this.node, this.name, this.inventoryPropertiesBuilder.build(), this.openSound, this.closeSound);
        }
    }
}
