package net.mineles.library.docker.binding.port;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum Protocol {
    TCP, UDP;

    public static Protocol fromType(@NotNull String protocol) {
        return switch (protocol.toLowerCase(Locale.ROOT)) {
            case "tcp" -> TCP;
            case "udp" -> UDP;
            default -> throw new IllegalArgumentException("Invalid protocol: " + protocol);
        };
    }
}