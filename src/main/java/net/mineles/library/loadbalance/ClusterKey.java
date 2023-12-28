package net.mineles.library.loadbalance;

public final class ClusterKey {
    private final String name;

    public ClusterKey(String name) {
        this.name = name;
    }

    public static ClusterKey of(String name) {
        return new ClusterKey(name);
    }

    public String getName() {
        return this.name;
    }
}
