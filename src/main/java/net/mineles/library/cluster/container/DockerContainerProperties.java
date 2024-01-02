package net.mineles.library.cluster.container;

import com.github.dockerjava.api.model.Container;
import net.mineles.library.cluster.binding.port.PortBinding;

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
                Arrays.stream(container.getPorts()).map(PortBinding::fromContainerPort).toList(),
                ContainerStatus.fromStatus(container.getStatus()),
                container.getCommand(),
                container.getImage(),
                container.getCreated()
        );
    }
}
