package net.mineles.library.docker.container;

import net.mineles.library.docker.binding.env.EnvironmentVariableBinding;
import net.mineles.library.docker.binding.folder.FolderBinding;
import net.mineles.library.docker.binding.port.PortBinding;
import net.mineles.library.docker.image.RemoteImageTag;
import net.mineles.library.libs.dockerjava.api.model.RestartPolicy;

import java.util.List;
import java.util.UUID;

public record ContainerTemplate(
        String name,
        RemoteImageTag image,
        String network,
        List<String> command,
        List<EnvironmentVariableBinding> environmentVariables,
        List<PortBinding> ports,
        List<FolderBinding> volumes,
        RestartPolicy restartPolicy,
        ContainerResourceLimit resourceLimit
) {

    public String generateNameForNewContainer() {
        return this.name + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static ContainerTemplateBuilder builder(RemoteImageTag tag) {
        return new ContainerTemplateBuilder(tag);
    }

    public static ContainerTemplate empty() {
        return new ContainerTemplateBuilder(null).build();
    }
}
