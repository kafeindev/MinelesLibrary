package net.mineles.library.docker.client;

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
import net.mineles.library.docker.binding.AbstractBinding;
import net.mineles.library.docker.config.DockerConfig;
import net.mineles.library.docker.container.options.ContainerListOptions;
import net.mineles.library.docker.container.options.ContainerRemoveOptions;
import net.mineles.library.docker.container.DockerContainer;
import net.mineles.library.docker.container.request.CreateContainerRequest;
import net.mineles.library.docker.exception.DockerContainerException;
import net.mineles.library.docker.exception.DockerHttpClientException;
import net.mineles.library.docker.image.DockerImage;
import net.mineles.library.docker.image.RemoteImageTag;
import net.mineles.library.docker.image.request.BuildImageRequest;
import net.mineles.library.docker.image.request.BuildImageRequestOptions;
import net.mineles.library.docker.volume.CreateVolumeRequest;
import net.mineles.library.docker.volume.DockerVolume;
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

    public Flux<DockerContainer> listContainers(@NotNull ContainerListOptions... options) {
        ListContainersCmd listContainersCmd = dockerClient.listContainersCmd();

        for (ContainerListOptions option : options) {
            listContainersCmd = option.apply(listContainersCmd);
        }

        return Flux.fromIterable(listContainersCmd.exec()).map(DockerContainer::fromContainer);
    }

    public Flux<DockerContainer> findByImage(@NotNull String image) {
        return listContainers().filter(container -> container.image().equals(image));
    }

    public Mono<DockerContainer> findByContainerId(@NotNull String containerId) {
        return listContainers().filter(container -> container.id().equals(containerId)).next();
    }

    public Mono<DockerContainer> findByContainerName(@NotNull String containerName) {
        return listContainers().filter(container -> container.names().contains(containerName)).next();
    }

    public Mono<Void> startContainer(@NotNull DockerContainer container) {
        return Mono.fromRunnable(() -> dockerClient.startContainerCmd(container.id()).exec());
    }

    public Mono<Void> stopContainer(@NotNull DockerContainer container) {
        return Mono.fromRunnable(() -> dockerClient.stopContainerCmd(container.id()).exec());
    }

    public Mono<Void> restartContainer(@NotNull DockerContainer container) {
        return Mono.fromRunnable(() -> dockerClient.restartContainerCmd(container.id()).exec());
    }

    public Mono<Void> killContainer(@NotNull DockerContainer container) {
        return Mono.fromRunnable(() -> dockerClient.killContainerCmd(container.id()).exec());
    }

    public Mono<Void> pauseContainer(@NotNull DockerContainer container) {
        return Mono.fromRunnable(() -> dockerClient.pauseContainerCmd(container.id()).exec());
    }

    public Mono<Void> unpauseContainer(@NotNull DockerContainer container) {
        return Mono.fromRunnable(() -> dockerClient.unpauseContainerCmd(container.id()).exec());
    }

    public Mono<Void> removeContainer(@NotNull DockerContainer container, @NotNull ContainerRemoveOptions... options) {
        RemoveContainerCmd removeContainerCmd = dockerClient.removeContainerCmd(container.id());

        for (ContainerRemoveOptions option : options) {
            removeContainerCmd = option.apply(removeContainerCmd);
        }

        return Mono.fromRunnable(removeContainerCmd::exec);
    }

    public Mono<Boolean> isHealthy(@NotNull DockerContainer container) {
        return Mono.fromCallable(() -> dockerClient.inspectContainerCmd(container.id())
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
