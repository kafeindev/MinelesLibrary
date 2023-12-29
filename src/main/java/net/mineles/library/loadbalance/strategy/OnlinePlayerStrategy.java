package net.mineles.library.loadbalance.strategy;

import net.mineles.library.loadbalance.Cluster;
import org.jetbrains.annotations.NotNull;

final class OnlinePlayerStrategy implements BalancingStrategy {
    private static final int MAX_PLAYER = 30;

    OnlinePlayerStrategy() {
    }

    @Override
    public boolean check(@NotNull Cluster cluster) {
        return cluster.getPlayers().size() < MAX_PLAYER;
    }

    @Override
    public boolean checkHalfQuarter(@NotNull Cluster cluster) {
        return cluster.getPlayers().size() <= (MAX_PLAYER / 4) * 3;
    }
}
