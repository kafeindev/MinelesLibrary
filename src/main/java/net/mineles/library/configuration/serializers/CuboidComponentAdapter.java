package net.mineles.library.configuration.serializers;

import net.mineles.library.components.CuboidComponent;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class CuboidComponentAdapter implements TypeSerializer<CuboidComponent> {
    public static final CuboidComponentAdapter INSTANCE = new CuboidComponentAdapter();

    private CuboidComponentAdapter() {}

    @Override
    public CuboidComponent deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String objString = node.getString();
        if (objString == null) {
            return null;
        }

        return CuboidComponent.SERIALIZER.deserialize(objString);
    }

    @Override
    public void serialize(Type type, @Nullable CuboidComponent obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        String objString = obj.toString();
        node.raw(objString);
    }
}
