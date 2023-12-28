package net.mineles.library.loadbalance;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public final class ClusterMap {
    private final Multimap<ClusterKey, Cluster> clusters;

    public ClusterMap() {
        this(ArrayListMultimap.create());
    }

    public ClusterMap(Multimap<ClusterKey, Cluster> clusters) {
        this.clusters = clusters;
    }

    public Multimap<ClusterKey, Cluster> getClusters() {
        return this.clusters;
    }

    public Collection<Cluster> getClusters(@NotNull ClusterKey key) {
        return this.clusters.get(key);
    }

    public @Nullable Cluster getCluster(@NotNull ClusterKey key, @NotNull Predicate<Cluster> predicate) {
        return this.clusters.get(key).stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    public void addCluster(@NotNull ClusterKey clusterKey, @NotNull Cluster cluster) {
        this.clusters.put(clusterKey, cluster);
    }

    public void removeCluster(@NotNull Cluster cluster) {
        ClusterKey key = cluster.getProperties().getKey();
        this.clusters.remove(key, cluster);
    }
}
