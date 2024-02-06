package net.mineles.library.configuration.serializers;

import net.mineles.library.docker.container.ContainerTemplate;
import net.mineles.library.docker.container.ContainerTemplateCollection;
import net.mineles.library.libs.configurate.ConfigurationNode;
import net.mineles.library.libs.configurate.serialize.SerializationException;
import net.mineles.library.libs.configurate.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class ContainerTemplateCollectionAdapter implements TypeSerializer<ContainerTemplateCollection> {
    public static final ContainerTemplateCollectionAdapter INSTANCE = new ContainerTemplateCollectionAdapter();

    private ContainerTemplateCollectionAdapter() {}

    @Override
    public ContainerTemplateCollection deserialize(Type type, ConfigurationNode node) throws SerializationException {
        List<ContainerTemplate> containerTemplates = new ArrayList<>();
        for (ConfigurationNode containerTemplateNode : node.childrenMap().values()) {
            containerTemplates.add(ContainerTemplateAdapter.INSTANCE.deserialize(type, containerTemplateNode));
        }

        return new ContainerTemplateCollection(containerTemplates);
    }

    @Override
    public void serialize(Type type, @Nullable ContainerTemplateCollection obj, ConfigurationNode node) throws SerializationException {
        throw new SerializationException("Cannot serialize");
    }
}
