package net.mineles.library.loadbalance;

public final class LoadBalancer {
    private final ClusterMap clusterMap;

    public LoadBalancer(ClusterMap clusterMap) {
        this.clusterMap = clusterMap;
    }
}
