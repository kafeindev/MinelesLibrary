package net.mineles.library.docker.container;

import com.github.dockerjava.api.model.Container;
import net.mineles.library.docker.binding.port.PortBinding;

import java.util.Arrays;
import java.util.List;

public record DockerContainer(
        String id,
        List<String> names,
        List<PortBinding> ports,
        ContainerStatus status,
        String command,
        String image,
        long createdAt
) {

    public static DockerContainer fromContainer(Container container) {
        return new DockerContainer(
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
