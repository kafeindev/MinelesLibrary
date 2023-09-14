package net.mineles.library.menu;

import com.google.common.collect.Maps;
import net.kyori.adventure.text.Component;
import net.mineles.library.utils.text.PlaceholderParser;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

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

    @NotNull Inventory createInventory() {
        return createInventory(PlaceholderParser.parseComponent(getTitle(), Maps.newHashMap()));
    }

    @NotNull Inventory createInventory(@NotNull Map<String, String> placeholders) {
        return createInventory(PlaceholderParser.parseComponent(getTitle(), placeholders));
    }

    @NotNull Inventory createInventory(@NotNull Component parsedTitle) {
        return this.type == InventoryType.CHEST
                ? Bukkit.createInventory(null, this.size, parsedTitle)
                : Bukkit.createInventory(null, this.type, parsedTitle);
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

        @Nullable String title() {
            return this.title;
        }

        @NotNull Builder title(@NotNull String title) {
            this.title = title;
            return this;
        }

        @Nullable InventoryType type() {
            return this.type;
        }

        @NotNull Builder type(@NotNull InventoryType type) {
            this.type = type;
            return this;
        }

        int size() {
            return this.size;
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
