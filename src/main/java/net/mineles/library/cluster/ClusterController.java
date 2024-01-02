package net.mineles.library.cluster;

import com.github.dockerjava.transport.DockerHttpClient;
import com.google.common.collect.Maps;
import net.mineles.library.cluster.client.DockerTemplate;
import net.mineles.library.cluster.config.DockerConfig;
import net.mineles.library.cluster.container.request.CreateContainerRequest;
import net.mineles.library.cluster.loadbalance.BalancingStrategy;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public final class ClusterController {
    private final DockerTemplate dockerTemplate;
    private final Map<String, Cluster> clusterMap;

    public ClusterController(DockerConfig dockerConfig) {
        this(dockerConfig, Maps.newHashMap());
    }

    public ClusterController(DockerConfig dockerConfig, Map<String, Cluster> clusterMap) {
        this.dockerTemplate = new DockerTemplate(dockerConfig);
        this.clusterMap = clusterMap;
    }

    public DockerTemplate getDockerTemplate() {
        return this.dockerTemplate;
    }

    public DockerHttpClient.Response pingDocker() {
        return this.dockerTemplate.ping().log().block();
    }

    public Mono<Cluster> createCluster(@NotNull CreateContainerRequest createContainerRequest,
                                       @NotNull BalancingStrategy balancingStrategy) {
        return Mono.fromCallable(() -> {
            String id = this.dockerTemplate.createContainer(createContainerRequest)
                    .block();
            return new DefaultCluster(id, balancingStrategy);
        });
    }

    public Map<String, Cluster> getClusterMap() {
        return this.clusterMap;
    }

    public Flux<Cluster> getAvailableClusters(@NotNull String image) {
        return this.dockerTemplate.findByImage(image)
                .filter(dockerContainerProperties -> this.clusterMap.get(dockerContainerProperties.id()) != null)
                .filter(dockerContainerProperties -> this.getDockerTemplate().isHealthy(dockerContainerProperties).block())
                .map(dockerContainerProperties -> this.clusterMap.get(dockerContainerProperties.id()))
                .filter(Cluster::checkAvailable);
    }

    public Cluster getCluster(@NotNull String id) {
        return this.clusterMap.get(id);
    }

    public void registerCluster(@NotNull Cluster cluster) {
        this.clusterMap.put(cluster.getId(), cluster);
    }

    public void unregisterCluster(@NotNull String id) {
        this.clusterMap.remove(id);
    }
}
