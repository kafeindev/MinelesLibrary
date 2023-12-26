package net.mineles.library.loadbalance;

import java.net.InetSocketAddress;

public final class HostAndPort {
    private final String host;
    private final int port;

    public HostAndPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public InetSocketAddress asSocketAddress() {
        return new InetSocketAddress(this.host, this.port);
    }
}
