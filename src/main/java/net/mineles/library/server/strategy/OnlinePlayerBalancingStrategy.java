package net.mineles.library.server.strategy;

import net.mineles.library.redis.RedisCache;

public final class OnlinePlayerBalancingStrategy extends BalancingStrategy {
    public OnlinePlayerBalancingStrategy(RedisCache cache, int max) {
        super(cache, max);
    }

    @Override
    protected int getCount(String serverName) {
        String onlinePlayers = this.cache.get("server:" + serverName + ":online_players");
        return Integer.parseInt(onlinePlayers);
    }
}
