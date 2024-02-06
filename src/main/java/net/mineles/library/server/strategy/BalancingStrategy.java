package net.mineles.library.server.strategy;

import net.mineles.library.redis.RedisCache;

public abstract class BalancingStrategy implements Strategy {
    protected final RedisCache cache;
    protected final int max;

    public BalancingStrategy(RedisCache cache, int max) {
        this.cache = cache;
        this.max = max;
    }

    @Override
    public boolean check(String serverName) {
        return getCount(serverName) < this.max;
    }

    @Override
    public boolean checkForNewServer(String serverName) {
        return getCount(serverName) < (this.max / 4) * 3;
    }

    protected abstract int getCount(String serverName);
}
