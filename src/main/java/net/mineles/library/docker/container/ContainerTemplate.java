package net.mineles.library.docker.container;

import com.github.dockerjava.api.model.RestartPolicy;
import net.mineles.library.docker.binding.env.EnvironmentVariableBinding;
import net.mineles.library.docker.binding.folder.FolderBinding;
import net.mineles.library.docker.binding.port.PortBinding;
import net.mineles.library.docker.image.RemoteImageTag;

import java.util.List;

public record ContainerTemplate(
        String name,
        //String hostName,
        RemoteImageTag image,
        //List<String> command,
        List<EnvironmentVariableBinding> environmentVariables,
        List<PortBinding> ports,
        List<FolderBinding> volumes,
        RestartPolicy restartPolicy,
        ContainerResourceLimit resourceLimit
) {

    public static ContainerTemplateBuilder builder(RemoteImageTag tag) {
        return new ContainerTemplateBuilder(tag);
    }

    public static ContainerTemplate empty() {
        return new ContainerTemplateBuilder(null).build();
    }
}
