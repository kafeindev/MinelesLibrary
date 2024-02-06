package net.mineles.library.configuration.serializers;

import net.mineles.library.components.LocationComponent;
import org.checkerframework.checker.nullness.qual.Nullable;
import net.mineles.library.libs.configurate.ConfigurationNode;
import net.mineles.library.libs.configurate.serialize.SerializationException;
import net.mineles.library.libs.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class LocationComponentAdapter implements TypeSerializer<LocationComponent> {
    public static final LocationComponentAdapter INSTANCE = new LocationComponentAdapter();

    private LocationComponentAdapter() {}

    @Override
    public LocationComponent deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String objString = node.getString();
        if (objString == null) {
            return null;
        }

        return LocationComponent.SERIALIZER.deserialize(objString);
    }

    @Override
    public void serialize(Type type, @Nullable LocationComponent obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.raw(null);
            return;
        }

        String objString = obj.toString();
        node.raw(objString);
    }
}
