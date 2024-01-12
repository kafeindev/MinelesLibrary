package net.mineles.library.docker.container;

import com.github.dockerjava.api.model.Container;
import net.mineles.library.docker.binding.port.PortBinding;

import java.util.Arrays;
import java.util.List;

public record DockerContainerProperties(
        String id,
        List<String> names,
        List<PortBinding> ports,
        ContainerStatus status,
        String command,
        String image,
        long createdAt
) {

    public static DockerContainerProperties fromContainer(Container container) {
        return new DockerContainerProperties(
                container.getId(),
                Arrays.asList(container.getNames()),
                Arrays.stream(container.getPorts())
                        .filter(containerPort -> containerPort.getPrivatePort() != null && containerPort.getPublicPort() != null)
                        .map(PortBinding::fromContainerPort)
                        .toList(),
                ContainerStatus.fromStatus(container.getState()),
                container.getCommand(),
                container.getImage(),
                container.getCreated()
        );
    }
}
