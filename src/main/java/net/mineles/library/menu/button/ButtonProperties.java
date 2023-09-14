package net.mineles.library.menu.button;

import com.cryptomorin.xseries.XSound;
import net.mineles.library.node.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ButtonProperties {
    private final @Nullable Node node;

    private final @NotNull String name;
    private final @NotNull String parent;
    private final int[] slots;

    private final @Nullable XSound clickSound;

    ButtonProperties(@Nullable Node node,
                     @NotNull String name,
                     @NotNull String parent,
                     int[] slots,
                     @Nullable XSound clickSound) {
        this.node = node;
        this.name = name;
        this.parent = parent;
        this.slots = slots;
        this.clickSound = clickSound;
    }

    @Nullable Node getNode() {
        return this.node;
    }

    @NotNull String getName() {
        return this.name;
    }

    @NotNull String getParent() {
        return this.parent;
    }

    int[] getSlots() {
        return this.slots;
    }

    @Nullable XSound getClickSound() {
        return this.clickSound;
    }

    static @NotNull Builder newBuilder() {
        return new Builder();
    }

    static final class Builder {
        private Node node;

        private String name;
        private String parent;
        private int[] slots;

        private XSound clickSound;

        Builder() {
        }

        Builder node(Node node) {
            this.node = node;
            return this;
        }

        Builder name(String name) {
            this.name = name;
            return this;
        }

        Builder parent(String parent) {
            this.parent = parent;
            return this;
        }

        Builder slots(int[] slots) {
            this.slots = slots;
            return this;
        }

        Builder clickSound(XSound clickSound) {
            this.clickSound = clickSound;
            return this;
        }

        ButtonProperties build() {
            return new ButtonProperties(node, name, parent, slots, clickSound);
        }
    }
}
