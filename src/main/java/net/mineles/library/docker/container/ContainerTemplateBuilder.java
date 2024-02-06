package net.mineles.library.docker.container;

import com.google.common.base.Preconditions;
import net.mineles.library.docker.binding.env.EnvironmentVariableBinding;
import net.mineles.library.docker.binding.folder.FolderBinding;
import net.mineles.library.docker.binding.port.PortBinding;
import net.mineles.library.docker.image.RemoteImageTag;
import net.mineles.library.libs.dockerjava.api.model.RestartPolicy;

import java.util.ArrayList;
import java.util.List;

public final class ContainerTemplateBuilder {

    private final RemoteImageTag image;

    private String name;

    private String network;

    private List<String> command = new ArrayList<>();

    private List<EnvironmentVariableBinding> environmentVariables = new ArrayList<>();

    private List<PortBinding> ports = new ArrayList<>();

    private List<FolderBinding> volumes = new ArrayList<>();

    private RestartPolicy restartPolicy = RestartPolicy.noRestart();

    private ContainerResourceLimit resourceLimit;

    public ContainerTemplateBuilder(RemoteImageTag image) {
        this.image = image;
    }

    public ContainerTemplateBuilder name(String name) {
        Preconditions.checkArgument(name == null || name.length() <= 255, "Container name must be less than 255 characters");

        this.name = name;
        return this;
    }

    public ContainerTemplateBuilder network(String network) {
        this.network = network;
        return this;
    }

    public ContainerTemplateBuilder command(List<String> command) {
        this.command = command;
        return this;
    }

    public ContainerTemplateBuilder environmentVariables(List<EnvironmentVariableBinding> environmentVariables) {
        this.environmentVariables = environmentVariables;
        return this;
    }

    public ContainerTemplateBuilder ports(List<PortBinding> ports) {
        this.ports = ports;
        return this;
    }

    public ContainerTemplateBuilder volumes(List<FolderBinding> volumes) {
        this.volumes = volumes;
        return this;
    }

    public ContainerTemplateBuilder restartPolicy(RestartPolicy restartPolicy) {
        this.restartPolicy = restartPolicy;
        return this;
    }

    public ContainerTemplateBuilder resourceLimit(ContainerResourceLimit resourceLimit) {
        this.resourceLimit = resourceLimit;
        return this;
    }

    public ContainerTemplate build() {
        return new ContainerTemplate(name,
                image,
                network,
                command,
                environmentVariables,
                ports,
                volumes,
                restartPolicy,
                resourceLimit);
    }
}
