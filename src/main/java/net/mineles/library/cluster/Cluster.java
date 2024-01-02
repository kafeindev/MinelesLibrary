package net.mineles.library.cluster;

import com.google.common.collect.Sets;
import net.mineles.library.cluster.container.DockerContainerProperties;
import net.mineles.library.cluster.loadbalance.BalancingStrategy;

import java.util.Set;
import java.util.UUID;

public abstract class Cluster {
    private final String id;
    private final BalancingStrategy strategy;
    private final Set<UUID> players;

    protected Cluster(String id, BalancingStrategy strategy) {
        this(id, strategy, Sets.newHashSet());
    }

    protected Cluster(String id, BalancingStrategy strategy, Set<UUID> players) {
        this.id = id;
        this.strategy = strategy;
        this.players = players;
    }

    public String getId() {
        return this.id;
    }

    public BalancingStrategy getStrategy() {
        return this.strategy;
    }

    public Set<UUID> getPlayers() {
        return this.players;
    }

    public boolean checkAvailable() { // for balancing
        return getStrategy().check(this);
    }

    public boolean checkAvailableHalfQuarter() { // for balancing
        return getStrategy().checkHalfQuarter(this);
    }
}
