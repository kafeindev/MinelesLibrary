package net.mineles.library.compatibility;

import com.google.common.collect.Maps;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.FontImages.FontImageWrapper;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class ItemsAdderCompatibility {
    private static final Map<String, Integer> MODEL_DATA_MAP = Maps.newHashMap();

    public static int getItemModel(@NotNull String name) {
        if (!Compatibility.ITEMS_ADDER.isCompatible()) {
            throw new IllegalStateException("ItemsAdder is not compatible!");
        }

        if (MODEL_DATA_MAP.containsKey(name)) {
            return MODEL_DATA_MAP.get(name);
        }

        ItemStack itemStack = CustomStack.getInstance(name).getItemStack();

        int modelData = itemStack.getItemMeta().getCustomModelData();
        MODEL_DATA_MAP.put(name, modelData);

        return modelData;
    }

    @NotNull
    public static String replaceFontImages(@NotNull String key) {
        if (!Compatibility.ITEMS_ADDER.isCompatible()) {
            return key;
        }

        return FontImageWrapper.replaceFontImages(key);
    }

    private ItemsAdderCompatibility() {
    }
}
