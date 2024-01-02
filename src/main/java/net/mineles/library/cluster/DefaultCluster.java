package net.mineles.library.cluster;

import net.mineles.library.cluster.loadbalance.BalancingStrategy;

import java.util.Set;
import java.util.UUID;

public final class DefaultCluster extends Cluster {
    public DefaultCluster(String id, BalancingStrategy strategy) {
        super(id, strategy);
    }

    public DefaultCluster(String id, BalancingStrategy strategy, Set<UUID> players) {
        super(id, strategy, players);
    }
}
