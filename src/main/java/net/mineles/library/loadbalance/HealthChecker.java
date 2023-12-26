package net.mineles.library.loadbalance;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Socket;

final class HealthChecker {
    private static final int DEFAULT_TIMEOUT = 1000;

    private HealthChecker() {
    }

    static boolean check(@NotNull Cluster cluster) {
        HostAndPort hostAndPort = cluster.getProperties().getHostAndPort();
        return check(hostAndPort);
    }

    static boolean check(@NotNull HostAndPort hostAndPort) {
        try (Socket ignored = new Socket(hostAndPort.asSocketAddress().getAddress(), DEFAULT_TIMEOUT)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
