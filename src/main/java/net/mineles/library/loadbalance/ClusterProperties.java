package net.mineles.library.loadbalance;

public final class ClusterProperties {
    private final ClusterKey key;
    private final String name;
    private final HostAndPort hostAndPort;

    public ClusterProperties(ClusterKey key, String name, HostAndPort hostAndPort) {
        this.key = key;
        this.name = name;
        this.hostAndPort = hostAndPort;
    }

    public ClusterKey getKey() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }

    public HostAndPort getHostAndPort() {
        return this.hostAndPort;
    }
}
