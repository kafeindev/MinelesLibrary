package net.mineles.library.cluster.loadbalance;

import net.mineles.library.cluster.Cluster;
import org.jetbrains.annotations.NotNull;

public interface BalancingStrategy {
    boolean check(@NotNull Cluster cluster);

    boolean checkHalfQuarter(@NotNull Cluster cluster);

    interface Factory {
        static BalancingStrategy createOnlineBalancingStrategy(int max) {
            return new OnlinePlayerStrategy(max);
        }
    }
}
