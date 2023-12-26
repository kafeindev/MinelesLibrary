package net.mineles.library.loadbalance;

import com.google.common.collect.Sets;

import java.util.Set;
import java.util.UUID;

public abstract class Cluster {
    private final ClusterProperties properties;
    private final Set<UUID> players;

    private long lastHealthCheck;

    protected Cluster(ClusterProperties properties) {
        this(properties, Sets.newHashSet());
    }

    protected Cluster(ClusterProperties properties, Set<UUID> players) {
        this.properties = properties;
        this.players = players;
        this.lastHealthCheck = System.currentTimeMillis();
    }

    public ClusterProperties getProperties() {
        return this.properties;
    }

    public Set<UUID> getPlayers() {
        return this.players;
    }

    public long getLastHealthCheck() {
        return this.lastHealthCheck;
    }

    public void setLastHealthCheck(long lastHealthCheck) {
        this.lastHealthCheck = lastHealthCheck;
    }
}
