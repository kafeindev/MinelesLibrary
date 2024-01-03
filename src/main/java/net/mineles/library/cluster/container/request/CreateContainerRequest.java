package net.mineles.library.cluster.container.request;

import com.github.dockerjava.api.model.RestartPolicy;
import net.mineles.library.cluster.binding.env.EnvironmentVariableBinding;
import net.mineles.library.cluster.binding.folder.FolderBinding;
import net.mineles.library.cluster.binding.port.PortBinding;
import net.mineles.library.cluster.container.ContainerResourceLimit;
import net.mineles.library.cluster.image.RemoteImageTag;

import java.util.List;

public record CreateContainerRequest(
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
}
