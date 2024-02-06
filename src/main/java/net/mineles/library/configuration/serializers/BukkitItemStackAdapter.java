package net.mineles.library.configuration.serializers;

import net.mineles.library.components.ItemComponent;
import net.mineles.library.libs.configurate.ConfigurationNode;
import net.mineles.library.libs.configurate.serialize.SerializationException;
import net.mineles.library.libs.configurate.serialize.TypeSerializer;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;

public final class BukkitItemStackAdapter implements TypeSerializer<ItemStack> {
    public static final BukkitItemStackAdapter INSTANCE = new BukkitItemStackAdapter();

    private BukkitItemStackAdapter() {}

    @Override
    public ItemStack deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String objString = node.getString();
        if (objString == null) {
            return null;
        }

        return ItemComponent.SERIALIZER.deserializeItemStack(objString);
    }

    @Override
    public void serialize(Type type, @Nullable ItemStack obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        String objString = ItemComponent.SERIALIZER.serializeItemStack(obj);
        node.raw(objString);
    }
}
