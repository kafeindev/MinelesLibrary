package net.mineles.library.connection;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public final class HostAndPort {
    private final String host;
    private final int port;

    public HostAndPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static HostAndPort localhost(int port) {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return new HostAndPort(inetAddress.getHostAddress(), port);
        } catch (UnknownHostException e) {
            throw new RuntimeException("Failed to get host address", e);
        }
    }

    public static HostAndPort of(String hostAndPort) {
        String[] split = hostAndPort.split(":");
        if (split.length < 2) {
            throw new RuntimeException();
        }

        return new HostAndPort(split[0], Integer.parseInt(split[1]));
    }

    public static HostAndPort fromSocketAddress(InetSocketAddress socketAddress) {
        return localhost(socketAddress.getPort());
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public boolean isLocal() {
        return this.host.equals("127.0.0.1") || this.host.equals("localhost");
    }

    public InetSocketAddress asSocketAddress() {
        return new InetSocketAddress(this.host, this.port);
    }

    @Override
    public String toString() {
        return this.host + ":" + this.port;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        HostAndPort that = (HostAndPort) obj;
        return this.port == that.port && this.host.equals(that.host);
    }
}
