package net.mineles.library.cluster.container.request;

import com.github.dockerjava.api.model.RestartPolicy;
import com.google.common.base.Preconditions;
import net.mineles.library.cluster.binding.env.EnvironmentVariableBinding;
import net.mineles.library.cluster.binding.folder.FolderBinding;
import net.mineles.library.cluster.binding.port.PortBinding;
import net.mineles.library.cluster.container.ContainerResourceLimit;
import net.mineles.library.cluster.image.RemoteImageTag;

import java.util.ArrayList;
import java.util.List;

public final class CreateContainerRequestBuilder {

    private final RemoteImageTag image;

    private String name;

    //private String hostName;

//    private List<String> command;

    private List<EnvironmentVariableBinding> environmentVariables = new ArrayList<>();

    private List<PortBinding> ports = new ArrayList<>();

    private List<FolderBinding> volumes = new ArrayList<>();

    private RestartPolicy restartPolicy = RestartPolicy.noRestart();

    private ContainerResourceLimit resourceLimit;

    public CreateContainerRequestBuilder(RemoteImageTag image) {
        this.image = image;
    }

    public CreateContainerRequestBuilder name(String name) {
        Preconditions.checkArgument(name == null || name.length() <= 255, "Container name must be less than 255 characters");

        this.name = name;
        return this;
    }

/*    public CreateContainerRequestBuilder hostName(String hostName) {
        Preconditions.checkArgument(hostName == null || hostName.length() <= 255, "Container host name must be less than 255 characters");

        this.hostName = hostName;
        return this;
    }*/

/*
    public CreateContainerRequestBuilder command(List<String> command) {
        this.command = command;
        return this;
    }
*/

    public CreateContainerRequestBuilder environmentVariables(List<EnvironmentVariableBinding> environmentVariables) {
        this.environmentVariables = environmentVariables;
        return this;
    }

    public CreateContainerRequestBuilder ports(List<PortBinding> ports) {
        this.ports = ports;
        return this;
    }

    public CreateContainerRequestBuilder volumes(List<FolderBinding> volumes) {
        this.volumes = volumes;
        return this;
    }

    public CreateContainerRequestBuilder restartPolicy(RestartPolicy restartPolicy) {
        this.restartPolicy = restartPolicy;
        return this;
    }

    public CreateContainerRequestBuilder resourceLimit(ContainerResourceLimit resourceLimit) {
        this.resourceLimit = resourceLimit;
        return this;
    }

    public CreateContainerRequest build() {
        return new CreateContainerRequest(name,
                //hostName,
                image,
//                command,
                environmentVariables,
                ports,
                volumes,
                restartPolicy,
                resourceLimit);
    }
}
