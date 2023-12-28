package net.mineles.library.loadbalance;

import net.mineles.library.loadbalance.strategy.BalancingStrategy;

import java.util.Set;
import java.util.UUID;

public final class DefaultCluster extends Cluster {
    public DefaultCluster(ClusterProperties properties, BalancingStrategy strategy) {
        super(properties, strategy);
    }

    public DefaultCluster(ClusterProperties properties, BalancingStrategy strategy, Set<UUID> players) {
        super(properties, strategy, players);
    }
}
