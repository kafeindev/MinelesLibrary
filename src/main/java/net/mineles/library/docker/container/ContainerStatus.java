package net.mineles.library.docker.container;

import java.util.Locale;

public enum ContainerStatus {
    CREATED(false),
    RESTARTING(true),
    RUNNING(true),
    PAUSED(true),
    EXITED(false),
    DEAD(false);

    private final boolean running;

    ContainerStatus(boolean running) {
        this.running = running;
    }

    public static ContainerStatus fromStatus(String status) {
        return switch (status.toLowerCase(Locale.ENGLISH)) {
            case "created" -> CREATED;
            case "restarting" -> RESTARTING;
            case "running" -> RUNNING;
            case "paused" -> PAUSED;
            case "exited" -> EXITED;
            case "dead" -> DEAD;
            default -> throw new IllegalArgumentException("Unknown status: " + status);
        };
    }

    public boolean isRunning() {
        return running;
    }
}
