package net.mineles.library.menu;

import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public final class InventoryProperties {
    private final @NotNull String title;
    private final @NotNull InventoryType type;
    private final int size;

    InventoryProperties(@NotNull String title,
                        @NotNull InventoryType type,
                        int size) {
        this.title = title;
        this.type = type;
        this.size = size;
    }

    InventoryProperties(@NotNull String title,
                        @NotNull InventoryType type) {
        this(title, type, type.getDefaultSize());
    }

    InventoryProperties(@NotNull String title,
                        int size) {
        this(title, InventoryType.CHEST, size);
    }

    InventoryProperties(@NotNull String title) {
        this(title, InventoryType.CHEST);
    }

    @NotNull String getTitle() {
        return this.title;
    }

    @NotNull InventoryType getType() {
        return this.type;
    }

    int getSize() {
        return this.size;
    }

    static @NotNull Builder newBuilder() {
        return new Builder();
    }

    static final class Builder {
        private String title;
        private InventoryType type;
        private int size;

        private Builder() {}

        @NotNull Builder title(@NotNull String title) {
            this.title = title;
            return this;
        }

        @NotNull Builder type(@NotNull InventoryType type) {
            this.type = type;
            return this;
        }

        @NotNull Builder size(int size) {
            this.size = size;
            return this;
        }

        @NotNull InventoryProperties build() {
            return new InventoryProperties(this.title, this.type, this.size);
        }
    }
}
