package net.mineles.library.menu.button;

import net.mineles.library.libs.xseries.XSound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.mineles.library.libs.configurate.ConfigurationNode;
import net.mineles.library.libs.configurate.serialize.SerializationException;

public final class ButtonProperties {
    private final @Nullable ConfigurationNode node;

    private final String name;
    private final int[] slots;

    private XSound clickSound;

    ButtonProperties(@Nullable ConfigurationNode node, String name, int[] slots, @Nullable XSound clickSound) {
        this.node = node;
        this.name = name;
        this.slots = slots;
        this.clickSound = clickSound;
    }

    public static ButtonProperties of(String name, int[] slots) {
        return new ButtonProperties(null, name, slots, null);
    }

    public static ButtonProperties of(String name, int[] slots, @Nullable XSound clickSound) {
        return new ButtonProperties(null, name, slots, clickSound);
    }

    public static ButtonProperties fromNode(ConfigurationNode node) {
        return new Builder()
                .node(node)
                .name((String) node.key())
                .clickSound(node.node("click-sound").getString())
                .slots(node.node("slot").empty() ? node.node("slots") : node.node("slot"))
                .build();
    }

    public @Nullable ConfigurationNode getNode() {
        return this.node;
    }

    public String getName() {
        return this.name;
    }

    public int[] getSlots() {
        return this.slots;
    }

    public @Nullable XSound getClickSound() {
        return this.clickSound;
    }

    public void setClickSound(@Nullable XSound clickSound) {
        this.clickSound = clickSound;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private ConfigurationNode node;

        private String name;
        private int[] slots;

        private XSound clickSound;

        Builder() {}

        public @Nullable ConfigurationNode node() {
            return this.node;
        }

        public Builder node(@NotNull ConfigurationNode node) {
            this.node = node;
            return this;
        }

        public Builder name(@NotNull String name) {
            this.name = name;
            return this;
        }

        public Builder slots(int @NotNull [] slots) {
            this.slots = slots;
            return this;
        }

        @NotNull Builder slots(@NotNull ConfigurationNode node) {
            Object slotsObject = node.raw();
            if (slotsObject instanceof Integer) {
                this.slots = new int[]{(int) slotsObject};
            } else {
                try {
                    this.slots = node.getList(Integer.class).stream().mapToInt(Integer::intValue).toArray();
                } catch (SerializationException e) {
                    throw new RuntimeException("Failed to deserialize slots", e);
                }
            }

            return this;
        }

        public Builder clickSound(@NotNull XSound clickSound) {
            this.clickSound = clickSound;
            return this;
        }

        public Builder clickSound(@Nullable String clickSound) {
            if (clickSound != null) {
                this.clickSound = XSound.matchXSound(clickSound).orElse(null);
            }
            return this;
        }

        public Builder copy(@NotNull ButtonProperties properties) {
            this.node = properties.node;
            this.name = properties.name;
            this.slots = properties.slots;
            this.clickSound = properties.clickSound;
            return this;
        }

        public Builder copy(@NotNull Builder builder) {
            this.node = builder.node;
            this.name = builder.name;
            this.slots = builder.slots;
            this.clickSound = builder.clickSound;
            return this;
        }

        public ButtonProperties build() {
            return new ButtonProperties(this.node, this.name, this.slots, this.clickSound);
        }
    }
}
