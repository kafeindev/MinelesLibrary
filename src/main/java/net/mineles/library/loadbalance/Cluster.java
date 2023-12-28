package net.mineles.library.loadbalance;

import com.google.common.collect.Sets;
import net.mineles.library.loadbalance.strategy.BalancingStrategy;

import java.util.Set;
import java.util.UUID;

public abstract class Cluster {
    private final ClusterProperties properties;
    private final BalancingStrategy strategy;
    private final Set<UUID> players;

    private long lastHealthCheck;
    private int retryCount;

    protected Cluster(ClusterProperties properties, BalancingStrategy strategy) {
        this(properties, strategy, Sets.newHashSet());
    }

    protected Cluster(ClusterProperties properties, BalancingStrategy strategy, Set<UUID> players) {
        this.properties = properties;
        this.strategy = strategy;
        this.players = players;
        this.lastHealthCheck = System.currentTimeMillis();
    }

    public ClusterProperties getProperties() {
        return this.properties;
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

    public boolean checkHealthy() {
        return this.retryCount <= 0;
    }

    public long getLastHealthCheck() {
        return this.lastHealthCheck;
    }

    public void setLastHealthCheck(long lastHealthCheck) {
        this.lastHealthCheck = lastHealthCheck;
    }

    public int getRetryCount() {
        return this.retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
