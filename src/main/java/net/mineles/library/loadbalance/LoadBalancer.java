package net.mineles.library.loadbalance;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public final class LoadBalancer {
    private final ClusterMap clusterMap;
    private final ScheduledExecutorService executor;

    public LoadBalancer() {
        this(new ClusterMap());
    }

    public LoadBalancer(ClusterMap clusterMap) {
        this.clusterMap = clusterMap;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void scheduleHealthChecker() {
        this.executor.schedule(() -> {
            Multimap<ClusterKey, Cluster> remove = ArrayListMultimap.create();
            this.clusterMap.getClusters().forEach((key, cluster) -> {
                cluster.setLastHealthCheck(System.currentTimeMillis());

                if (HealthChecker.check(cluster)) {
                    cluster.setRetryCount(0);
                    return;
                }

                int retryCount = cluster.getRetryCount() + 1;
                if (retryCount == 3) {
                    remove.put(key, cluster);
                } else {
                    cluster.setRetryCount(retryCount);
                }
            });
        }, 1L, TimeUnit.SECONDS);
    }

    public ClusterMap getClusterMap() {
        return this.clusterMap;
    }

    public List<Cluster> getAvailableClusters(@NotNull ClusterKey key) {
        return this.clusterMap.getClusters(key).stream()
                .filter(Cluster::checkAvailable)
                .collect(Collectors.toList());
    }
}
