package net.mineles.library.configuration.serializers;

import net.mineles.library.docker.config.DockerConfig;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public class DockerConfigAdapter implements TypeSerializer<DockerConfig> {
    public static final DockerConfigAdapter INSTANCE = new DockerConfigAdapter();

    private DockerConfigAdapter() {}

    @Override
    public DockerConfig deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (!node.isMap()) {
            throw new SerializationException("Expected a map");
        }

        return DockerConfig.fromNode(node);
    }

    @Override
    public void serialize(Type type, @Nullable DockerConfig obj, ConfigurationNode node) throws SerializationException {
        throw new SerializationException("Cannot serialize DockerConfig");
    }
}
