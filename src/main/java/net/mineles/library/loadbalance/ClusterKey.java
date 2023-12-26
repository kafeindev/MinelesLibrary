package net.mineles.library.loadbalance;

import net.mineles.library.loadbalance.strategy.BalancingStrategy;

public final class ClusterKey {
    private final String name;
    private final BalancingStrategy strategy;

    public ClusterKey(String name, BalancingStrategy strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    public String getName() {
        return this.name;
    }

    public BalancingStrategy getStrategy() {
        return this.strategy;
    }
}
