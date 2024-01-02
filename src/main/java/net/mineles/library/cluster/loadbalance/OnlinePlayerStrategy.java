package net.mineles.library.cluster.loadbalance;

import net.mineles.library.cluster.Cluster;
import org.jetbrains.annotations.NotNull;

final class OnlinePlayerStrategy implements BalancingStrategy {
    private final int max;

    OnlinePlayerStrategy(int max) {
        this.max = max;
    }

    @Override
    public boolean check(@NotNull Cluster cluster) {
        return cluster.getPlayers().size() < this.max;
    }

    @Override
    public boolean checkHalfQuarter(@NotNull Cluster cluster) {
        return cluster.getPlayers().size() <= (this.max / 4) * 3;
    }
}
