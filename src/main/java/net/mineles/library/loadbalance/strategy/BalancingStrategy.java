package net.mineles.library.loadbalance.strategy;

import net.mineles.library.loadbalance.Cluster;
import org.jetbrains.annotations.NotNull;

public interface BalancingStrategy {
    boolean check(@NotNull Cluster cluster);

    boolean checkHalfQuarter(@NotNull Cluster cluster);

    interface Factory {
        static BalancingStrategy onlineStrategy() {
            return new OnlinePlayerStrategy();
        }
    }
}
