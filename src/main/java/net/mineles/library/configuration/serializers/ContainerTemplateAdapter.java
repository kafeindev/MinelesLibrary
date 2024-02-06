package net.mineles.library.configuration.serializers;

import com.google.common.collect.Lists;
import net.mineles.library.docker.binding.env.EnvironmentVariableBinding;
import net.mineles.library.docker.binding.folder.FolderBinding;
import net.mineles.library.docker.binding.port.PortBinding;
import net.mineles.library.docker.container.ContainerResourceLimit;
import net.mineles.library.docker.container.ContainerTemplate;
import net.mineles.library.docker.container.ContainerTemplateBuilder;
import net.mineles.library.docker.image.RemoteImageTag;
import net.mineles.library.libs.dockerjava.api.model.RestartPolicy;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;
import net.mineles.library.libs.configurate.ConfigurationNode;
import net.mineles.library.libs.configurate.serialize.SerializationException;
import net.mineles.library.libs.configurate.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public final class ContainerTemplateAdapter implements TypeSerializer<ContainerTemplate> {
    public static final ContainerTemplateAdapter INSTANCE = new ContainerTemplateAdapter();

    private ContainerTemplateAdapter() {}

    @Override
    public ContainerTemplate deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (!node.isMap()) {
            throw new SerializationException("Expected a map");
        }

        RemoteImageTag imageTag = RemoteImageTag.parse(node.node("image").getString());
        String name = node.node("name").getString();
        String network = node.node("network").getString("bridge");

        List<EnvironmentVariableBinding> environmentVariableBindings = node.node("environments").getList(String.class, Lists.newArrayList()).stream()
                .map(EnvironmentVariableBinding::fromBinding)
                .collect(Collectors.toList());
        List<String> command = node.node("command").getList(String.class, Lists.newArrayList());
        List<PortBinding> portBindings = node.node("ports").getList(String.class, Lists.newArrayList()).stream()
                .map(PortBinding::fromBinding)
                .toList();
        List<FolderBinding> folderBindings = node.node("volumes").getList(String.class, Lists.newArrayList()).stream()
                .map(FolderBinding::fromBinding)
                .toList();

        RestartPolicy restartPolicy = RestartPolicy.parse(node.node("restart-policy").getString("no"));
        ContainerResourceLimit resourceLimit = node.node("resource-limit").get(ContainerResourceLimit.class);

        if (!node.node("environments-file").empty()) {
            List<EnvironmentVariableBinding> environmentVariablesFromFile = getEnvironmentVariablesFromSecretFile(node.node("environments-file").getString());
            environmentVariableBindings.addAll(environmentVariablesFromFile);
        }

        return new ContainerTemplateBuilder(imageTag)
                .name(name)
                .command(command)
                .network(network)
                .environmentVariables(environmentVariableBindings)
                .ports(portBindings)
                .volumes(folderBindings)
                .restartPolicy(restartPolicy)
                .resourceLimit(resourceLimit)
                .build();
    }

    private List<EnvironmentVariableBinding> getEnvironmentVariablesFromSecretFile(@NotNull String path) {
        Path environmentVariablesPath = Paths.get(path);
        if (!Files.exists(environmentVariablesPath)) {
            return Lists.newArrayList();
        }

        List<EnvironmentVariableBinding> environmentVariableBindings = Lists.newArrayList();
        try {
            Files.lines(environmentVariablesPath).forEach(line -> {
                String[] split = line.split("=");
                if (split.length != 2) {
                    throw new IllegalArgumentException("Invalid environment variable: " + line);
                }
                environmentVariableBindings.add(new EnvironmentVariableBinding(split[0], split[1]));
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to read environment variables", e);
        }

        return environmentVariableBindings;
    }

    @Override
    public void serialize(Type type, @Nullable ContainerTemplate obj, ConfigurationNode node) throws SerializationException {
        throw new SerializationException("Cannot serialize");
    }
}
