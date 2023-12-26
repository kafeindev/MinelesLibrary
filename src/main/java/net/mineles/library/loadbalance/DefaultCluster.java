package net.mineles.library.loadbalance;

import java.util.Set;
import java.util.UUID;

public final class DefaultCluster extends Cluster {
    public DefaultCluster(ClusterProperties properties) {
        super(properties);
    }

    public DefaultCluster(ClusterProperties properties, Set<UUID> players) {
        super(properties, players);
    }
}
