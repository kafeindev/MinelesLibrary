package net.mineles.library.docker.client;

import net.mineles.library.libs.dockerjava.api.DockerClient;
import net.mineles.library.libs.dockerjava.api.async.ResultCallback;
import net.mineles.library.libs.dockerjava.api.command.*;
import net.mineles.library.libs.dockerjava.api.model.*;
import net.mineles.library.libs.dockerjava.api.model.Container;
import net.mineles.library.libs.dockerjava.core.DefaultDockerClientConfig;
import net.mineles.library.libs.dockerjava.core.DockerClientConfig;
import net.mineles.library.libs.dockerjava.core.DockerClientImpl;
import net.mineles.library.libs.dockerjava.core.InvocationBuilder;
import net.mineles.library.libs.dockerjava.httpclient5.ApacheDockerHttpClient;
import net.mineles.library.libs.dockerjava.transport.DockerHttpClient;
import net.mineles.library.docker.binding.AbstractBinding;
import net.mineles.library.docker.config.DockerConfig;
import net.mineles.library.docker.container.ContainerTemplateCollection;
import net.mineles.library.docker.container.DockerContainerProperties;
import net.mineles.library.docker.container.options.ContainerListOptions;
import net.mineles.library.docker.container.options.ContainerRemoveOptions;
import net.mineles.library.docker.container.ContainerTemplate;
import net.mineles.library.docker.exception.DockerContainerException;
import net.mineles.library.docker.exception.DockerHttpClientException;
import net.mineles.library.docker.image.DockerImage;
import net.mineles.library.docker.image.RemoteImageTag;
import net.mineles.library.docker.image.request.BuildImageRequest;
import net.mineles.library.docker.image.request.BuildImageRequestOptions;
import net.mineles.library.docker.network.request.CreateNetworkRequest;
import net.mineles.library.docker.volume.CreateVolumeRequest;
import net.mineles.library.docker.volume.DockerVolume;
import net.mineles.library.libs.reactor.core.publisher.Flux;
import net.mineles.library.libs.reactor.core.publisher.Mono;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class DockerTemplate {
    private final DockerClient dockerClient;
    private final DockerHttpClient httpClient;
    private final ContainerTemplateCollection containerTemplates;

    public DockerTemplate(@NotNull DockerConfig config) {
        this(config, new ContainerTemplateCollection());
    }

    public DockerTemplate(@NotNull DockerConfig config, @NotNull ContainerTemplateCollection containerTemplates) {
        DockerClientConfig clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                //.withDockerHost(config.dockerHost())
                .withRegistryUsername(config.registryUsername())
                .withRegistryPassword(config.registryPassword())
                .withRegistryEmail(config.registryEmail())
                .withRegistryUrl(config.registryUrl())
                .build();

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(clientConfig.getDockerHost())
                .connectionTimeout(config.connectionTimeout())
                .build();

        this.dockerClient = DockerClientImpl.getInstance(clientConfig, httpClient);
        this.httpClient = httpClient;
        this.containerTemplates = containerTemplates;
    }

    public DockerTemplate(@NotNull DockerClient dockerClient, @NotNull DockerHttpClient httpClient) {
        this.dockerClient = dockerClient;
        this.httpClient = httpClient;
        this.containerTemplates = new ContainerTemplateCollection();
    }

    public Mono<DockerHttpClient.Response> ping() {
        DockerHttpClient.Request request = DockerHttpClient.Request.builder()
                .method(DockerHttpClient.Request.Method.GET)
                .path("/_ping")
                .build();

        return Mono.fromCallable(() -> httpClient.execute(request));
    }

    public Mono<Void> pullImage(@NotNull RemoteImageTag request) {
        return Mono.fromRunnable(() -> {
            try {
                dockerClient.pullImageCmd(request.image())
                        .withTag(request.tag())
                        .exec(new PullImageResultCallback())
                        .awaitCompletion();
            } catch (InterruptedException e) {
                throw new DockerHttpClientException(e);
            }
        });
    }

    public Flux<Void> pullImagesFromTemplateCollection() {
        return Flux.fromIterable(this.containerTemplates.getTemplates())
                .map(ContainerTemplate::image)
                .flatMap(this::pullImage);
    }

    public Flux<DockerImage> listImages() {
        return Flux.fromIterable(dockerClient.listImagesCmd().exec()).map(DockerImage::fromImage);
    }

    public Mono<String> buildImage(@NotNull BuildImageRequest request) {
        BuildImageCmd buildImageCmd = dockerClient.buildImageCmd()
                .withDockerfile(request.dockerFile())
                .withTags(Set.of(request.tag().getFullName()));

        for (BuildImageRequestOptions option : request.options()) {
            buildImageCmd = option.apply(buildImageCmd);
        }

        final BuildImageCmd finalBuildImageCmd = buildImageCmd;
        return Mono.fromCallable(() -> finalBuildImageCmd.exec(new BuildImageResultCallback()).awaitImageId());
    }

    public Flux<DockerVolume> listVolumes() {
        return Flux.fromIterable(dockerClient.listVolumesCmd().exec().getVolumes()).map(response -> new DockerVolume(
                response.getName(),
                response.getDriver(),
                response.getMountpoint()
        ));
    }

    public Mono<DockerVolume> findVolumeByName(@NotNull String name) {
        return listVolumes().filter(volume -> volume.name().equals(name)).next();
    }

    public Mono<Void> createVolume(@NotNull CreateVolumeRequest request) {
        return Mono.fromRunnable(() ->
                dockerClient.createVolumeCmd().withName(request.name()).withDriver(request.driver()).exec());
    }

    public Mono<Void> removeVolume(@NotNull DockerVolume volume) {
        return Mono.fromRunnable(() -> dockerClient.removeVolumeCmd(volume.name()).exec());
    }

    public Mono<String> createNetwork(@NotNull CreateNetworkRequest request) {
        return Mono.fromCallable(() -> this.dockerClient.createNetworkCmd()
                .withName(request.name())
                .withDriver(request.driver())
                .withIpam(request.ipam())
                .exec()
                .getId());
    }

    public Mono<Void> removeNetwork(@NotNull String name) {
        return Mono.fromRunnable(() -> this.dockerClient.removeContainerCmd(name).exec());
    }

    public Mono<String> createCommand(@NotNull String containerId, @NotNull String... commands) {
        return Mono.fromRunnable(() -> this.dockerClient.execCreateCmd(containerId).withCmd(commands).exec());
    }

    public Mono<String> createCommand(@NotNull String containerId, @NotNull List<String> commands) {
        return createCommand(containerId, commands.toArray(new String[0]));
    }

    public Mono<String> createCommand(@NotNull String containerId, @NotNull String command) {
        return createCommand(containerId, command.split(" "));
    }

    public Mono<String> startCommand(@NotNull String commandId, ResultCallback<Frame> callback) {
        return Mono.fromRunnable(() -> this.dockerClient.execStartCmd(commandId).exec(callback));
    }

    public Mono<Void> createAndStartCommand(@NotNull String containerId, @NotNull ResultCallback<Frame> callback, @NotNull String... commands) {
        return createCommand(containerId, commands).flatMap(commandId -> startCommand(commandId, callback)).then();
    }

    public Mono<Void> createAndStartCommand(@NotNull String containerId, @NotNull ResultCallback<Frame> callback, @NotNull List<String> commands) {
        return createAndStartCommand(containerId, callback, commands.toArray(new String[0]));
    }

    public Mono<Void> createAndStartCommand(@NotNull String containerId, @NotNull ResultCallback<Frame> callback, @NotNull String command) {
        return createAndStartCommand(containerId, callback, command.split(" "));
    }

    public ContainerTemplateCollection getContainerTemplates() {
        return this.containerTemplates;
    }

    public Mono<String> createContainer(@NotNull String name) {
        ContainerTemplate template = this.containerTemplates.getTemplate(name);
        if (template == null) {
            throw new DockerContainerException(name, "Container template not found");
        }

        return createContainer(template);
    }

    /**
     * Creates a container based on the given request.
     *
     * @param request The request object containing the necessary information to create the container
     * @return A Mono containing the ID of the created container
     */
    public Mono<String> createContainer(@NotNull ContainerTemplate request) {
        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(request.image().getFullName())
                .withName(request.generateNameForNewContainer())
                .withEnv(request.environmentVariables().stream().map(AbstractBinding::getFullBinding).toList())
                .withCmd(request.command());
        //.withExposedPorts(request.ports().stream().map(binding -> ExposedPort.parse(binding.getFullBinding())).toList());

        if (createContainerCmd.getHostConfig() == null) {
            throw new DockerContainerException(request.name(), "Host config is null");
        }

        createContainerCmd.getHostConfig()
                .withNetworkMode(request.network())
                .withBinds(request.volumes().stream()
                        .map(binding -> new Bind(binding.getKey(), new Volume(binding.getValue()))).toList())
                .withPortBindings(request.ports().stream().map(binding -> PortBinding.parse(binding.getFullBinding())).toList())
                .withRestartPolicy(request.restartPolicy())
                .withCpuPercent(request.resourceLimit().cpuPercent())
                .withCpuCount(request.resourceLimit().cpuCount())
                .withMemory(request.resourceLimit().memory());

        return Mono.fromCallable(() -> createContainerCmd.exec().getId());
    }

    public Flux<Container> listContainers(@NotNull ContainerListOptions... options) {
        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();

        for (ContainerListOptions option : options) {
            listContainersCmd = option.apply(listContainersCmd);
        }

        return Flux.fromIterable(listContainersCmd.exec());
    }

    public Flux<DockerContainerProperties> listContainerProperties(@NotNull ContainerListOptions... options) {
        return listContainers(options).map(DockerContainerProperties::fromContainer);
    }

    public Flux<Container> find(@NotNull Predicate<Container> predicate) {
        return listContainers(ContainerListOptions.ALL).filter(predicate);

    }
    public Flux<DockerContainerProperties> findByImage(@NotNull String image) {
        return listContainerProperties(ContainerListOptions.ALL).filter(container -> container.image().equals(image));
    }

    public Mono<DockerContainerProperties> findByContainerId(@NotNull String containerId) {
        return listContainerProperties(ContainerListOptions.ALL).filter(container -> container.id().equals(containerId)).next();
    }

    public Mono<DockerContainerProperties> findByContainerName(@NotNull String containerName) {
        return listContainerProperties(ContainerListOptions.ALL).filter(container -> container.names().contains(containerName)).next();
    }

    public Mono<Container> findByIpAddresses(@NotNull String address) {
        return listContainers(ContainerListOptions.RUNNING).filter(container -> {
            ContainerNetworkSettings networkSettings = container.getNetworkSettings();

            Map<String, ContainerNetwork> containerNetworkMap = networkSettings.getNetworks();
            return !containerNetworkMap.isEmpty() && containerNetworkMap.values().stream()
                    .anyMatch(containerNetwork -> containerNetwork.getIpAddress().equals(address));
        }).next();
    }

    public Mono<Void> startContainer(@NotNull String id) {
        return Mono.fromRunnable(() -> dockerClient.startContainerCmd(id).exec());
    }

    public Mono<Void> stopContainer(@NotNull String id) {
        return Mono.fromRunnable(() -> dockerClient.stopContainerCmd(id).exec());
    }

    public Mono<Void> restartContainer(@NotNull String id) {
        return Mono.fromRunnable(() -> dockerClient.restartContainerCmd(id).exec());
    }

    public Mono<Void> killContainer(@NotNull String id) {
        return Mono.fromRunnable(() -> dockerClient.killContainerCmd(id).exec());
    }

    public Mono<Void> pauseContainer(@NotNull String id) {
        return Mono.fromRunnable(() -> dockerClient.pauseContainerCmd(id).exec());
    }

    public Mono<Void> unpauseContainer(@NotNull String id) {
        return Mono.fromRunnable(() -> dockerClient.unpauseContainerCmd(id).exec());
    }

    public Mono<Void> waitContainer(@NotNull String id, ResultCallback<WaitResponse> callback) {
        return Mono.fromRunnable(() -> dockerClient.waitContainerCmd(id).exec(callback));
    }

    public Mono<Void> removeContainer(@NotNull String id, @NotNull ContainerRemoveOptions... options) {
        RemoveContainerCmd removeContainerCmd = dockerClient.removeContainerCmd(id);

        for (ContainerRemoveOptions option : options) {
            removeContainerCmd = option.apply(removeContainerCmd);
        }

        return Mono.fromRunnable(removeContainerCmd::exec);
    }

    public Mono<Void> removeContainers(@NotNull String image, @NotNull ContainerRemoveOptions... options) {
        Flux<Container> containers = listContainers(ContainerListOptions.ALL).filter(container -> container.getImage().equals(image));
        return removeContainers(containers, options);
    }

    public Mono<Void> removeContainers(@NotNull Flux<Container> containers, @NotNull ContainerRemoveOptions... options) {
        return containers.flatMap(container -> removeContainer(container.getId(), options)).then();
    }

    public Mono<Void> removeContainers(@NotNull ContainerRemoveOptions... options) {
        Flux<Container> containers = listContainers();
        return removeContainers(containers, options);
    }

    public Mono<Void> removeContainers(@NotNull ContainerTemplate template, @NotNull ContainerRemoveOptions... options) {
        Flux<Container> containers = listContainers().filter(container -> container.getImage().equals(template.image().getFullName()));
        return removeContainers(containers, options);
    }

    public Mono<Void> removeContainersFromTemplateCollection(@NotNull ContainerRemoveOptions... options) {
        Flux<Container> containers = listContainers().filter(container -> this.containerTemplates.getTemplateByImage(container.getImage()) != null);
        return removeContainers(containers, options);
    }

    public Mono<Statistics> getStatsFromContainer(@NotNull String id) {
        return getStatsFromContainer(id, new InvocationBuilder.AsyncResultCallback<>());
    }

    public Mono<Statistics> getStatsFromContainer(@NotNull String id, InvocationBuilder.AsyncResultCallback<Statistics> callback) {
        return Mono.fromCallable(() -> {
            dockerClient.statsCmd(id).exec(callback);
            Statistics statistics = callback.awaitResult();
            callback.close();
            return statistics;
        });
    }

    public Mono<Boolean> isHealthy(@NotNull DockerContainerProperties container) {
        return isHealthy(container.id());
    }

    public Mono<Boolean> isHealthy(@NotNull String containerId) {
        return Mono.fromCallable(() -> dockerClient.inspectContainerCmd(containerId)
                .exec()
                .getState()
                .getHealth()
                .getStatus()
                .equals("healthy"));
    }

    public void close() {
        try {
            dockerClient.close();
        } catch (IOException e) {
            throw new DockerHttpClientException(e);
        }
    }
}
