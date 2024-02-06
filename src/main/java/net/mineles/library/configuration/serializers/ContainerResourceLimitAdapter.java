package net.mineles.library.configuration.serializers;

import net.mineles.library.docker.container.ContainerResourceLimit;
import net.mineles.library.libs.configurate.ConfigurationNode;
import net.mineles.library.libs.configurate.serialize.SerializationException;
import net.mineles.library.libs.configurate.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;

public final class ContainerResourceLimitAdapter implements TypeSerializer<ContainerResourceLimit> {
    public static final ContainerResourceLimitAdapter INSTANCE = new ContainerResourceLimitAdapter();

    private ContainerResourceLimitAdapter() {}

    @Override
    public ContainerResourceLimit deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (!node.isMap()) {
            throw new SerializationException("Expected a map");
        }

        long cpuPercent = node.node("cpu-percent").getLong();
        long cpuCount = node.node("cpu-count").getLong();
        long memory = node.node("memory").getLong();
        return new ContainerResourceLimit(cpuPercent, cpuCount, memory);
    }

    @Override
    public void serialize(Type type, @Nullable ContainerResourceLimit obj, ConfigurationNode node) throws SerializationException {
        throw new SerializationException("Cannot serialize");
    }
}
