package net.mineles.library.configuration.serializers;

import net.mineles.library.components.ItemComponent;
import org.checkerframework.checker.nullness.qual.Nullable;
import net.mineles.library.libs.configurate.ConfigurationNode;
import net.mineles.library.libs.configurate.serialize.SerializationException;
import net.mineles.library.libs.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class ItemComponentAdapter implements TypeSerializer<ItemComponent> {
    public static final ItemComponentAdapter INSTANCE = new ItemComponentAdapter();

    private ItemComponentAdapter() {}

    @Override
    public ItemComponent deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (!node.isMap()) {
            return null;
        }

        return ItemComponent.from(node);
    }

    @Override
    public void serialize(Type type, @Nullable ItemComponent obj, ConfigurationNode node) throws SerializationException {
        throw new UnsupportedOperationException("Not implemented");
    }
}
