package net.mineles.library.cluster.client;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import net.mineles.library.cluster.binding.AbstractBinding;
import net.mineles.library.cluster.config.DockerConfig;
import net.mineles.library.cluster.container.DockerContainerProperties;
import net.mineles.library.cluster.container.options.ContainerListOptions;
import net.mineles.library.cluster.container.options.ContainerRemoveOptions;
import net.mineles.library.cluster.container.request.CreateContainerRequest;
import net.mineles.library.cluster.exception.DockerContainerException;
import net.mineles.library.cluster.exception.DockerHttpClientException;
import net.mineles.library.cluster.image.DockerImage;
import net.mineles.library.cluster.image.RemoteImageTag;
import net.mineles.library.cluster.image.request.BuildImageRequest;
import net.mineles.library.cluster.image.request.BuildImageRequestOptions;
import net.mineles.library.cluster.network.request.CreateNetworkRequest;
import net.mineles.library.cluster.volume.CreateVolumeRequest;
import net.mineles.library.cluster.volume.DockerVolume;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Set;

public class DockerTemplate {

    private final DockerClient dockerClient;

    private final DockerHttpClient httpClient;

    public DockerTemplate(@NotNull DockerConfig config) {
        DockerClientConfig clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(config.dockerHost())
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
    }

    public DockerTemplate(@NotNull DockerClient dockerClient, @NotNull DockerHttpClient httpClient) {
        this.dockerClient = dockerClient;
        this.httpClient = httpClient;
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

    /**
     * Creates a container based on the given request.
     *
     * @param request The request object containing the necessary information to create the container
     * @return A Mono containing the ID of the created container
     */
    public Mono<String> createContainer(@NotNull CreateContainerRequest request) {
        CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(request.image().getFullName())
                .withName(request.name())
                .withEnv(request.environmentVariables().stream().map(AbstractBinding::getFullBinding).toList());

        if (createContainerCmd.getHostConfig() == null) {
            throw new DockerContainerException(request.name(), "Host config is null");
        }

        createContainerCmd.getHostConfig()
                .withBinds(request.volumes().stream()
                        .map(binding -> new Bind(binding.getKey(), new Volume(binding.getValue()))).toList())
                .withPortBindings(request.ports().stream().map(binding -> PortBinding.parse(binding.getFullBinding())).toList())
                .withRestartPolicy(request.restartPolicy())
                .withCpuPercent(request.resourceLimit().cpuPercent())
                .withCpuCount(request.resourceLimit().cpuCount())
                .withMemory(request.resourceLimit().memory());

        return Mono.fromCallable(() -> createContainerCmd.exec().getId());
    }

    public Flux<DockerContainerProperties> listContainers(@NotNull ContainerListOptions... options) {
        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();

        for (ContainerListOptions option : options) {
            listContainersCmd = option.apply(listContainersCmd);
        }

        return Flux.fromIterable(listContainersCmd.exec()).map(DockerContainerProperties::fromContainer);
    }

    public Flux<DockerContainerProperties> findByImage(@NotNull String image) {
        return listContainers().filter(container -> container.image().equals(image));
    }

    public Mono<DockerContainerProperties> findByContainerId(@NotNull String containerId) {
        return listContainers().filter(container -> container.id().equals(containerId)).next();
    }

    public Mono<DockerContainerProperties> findByContainerName(@NotNull String containerName) {
        return listContainers().filter(container -> container.names().contains(containerName)).next();
    }

    public Mono<Void> startContainer(@NotNull DockerContainerProperties container) {
        return Mono.fromRunnable(() -> dockerClient.startContainerCmd(container.id()).exec());
    }

    public Mono<Void> stopContainer(@NotNull DockerContainerProperties container) {
        return Mono.fromRunnable(() -> dockerClient.stopContainerCmd(container.id()).exec());
    }

    public Mono<Void> restartContainer(@NotNull DockerContainerProperties container) {
        return Mono.fromRunnable(() -> dockerClient.restartContainerCmd(container.id()).exec());
    }

    public Mono<Void> killContainer(@NotNull DockerContainerProperties container) {
        return Mono.fromRunnable(() -> dockerClient.killContainerCmd(container.id()).exec());
    }

    public Mono<Void> pauseContainer(@NotNull DockerContainerProperties container) {
        return Mono.fromRunnable(() -> dockerClient.pauseContainerCmd(container.id()).exec());
    }

    public Mono<Void> unpauseContainer(@NotNull DockerContainerProperties container) {
        return Mono.fromRunnable(() -> dockerClient.unpauseContainerCmd(container.id()).exec());
    }

    public Mono<Void> removeContainer(@NotNull DockerContainerProperties container, @NotNull ContainerRemoveOptions... options) {
        RemoveContainerCmd removeContainerCmd = dockerClient.removeContainerCmd(container.id());

        for (ContainerRemoveOptions option : options) {
            removeContainerCmd = option.apply(removeContainerCmd);
        }

        return Mono.fromRunnable(removeContainerCmd::exec);
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
