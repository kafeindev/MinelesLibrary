package net.mineles.library.teleport;

import java.util.UUID;

public final class Teleport {
    private final UUID player;
    private final UUID target;
    private final TeleportType type;

    public Teleport(UUID player, UUID target, TeleportType type) {
        this.player = player;
        this.target = target;
        this.type = type;
    }

    public static Teleport of(UUID player, UUID target, TeleportType type) {
        return new Teleport(player, target, type);
    }

    public static Teleport of(String player, String target, String type) {
        return new Teleport(UUID.fromString(player), UUID.fromString(target), TeleportType.valueOf(type));
    }

    public static Teleport request(UUID player, UUID target) {
        return new Teleport(player, target, TeleportType.REQUEST);
    }

    public static Teleport accept(UUID player, UUID target) {
        return new Teleport(player, target, TeleportType.ACCEPT);
    }

    public static Teleport deny(UUID player, UUID target) {
        return new Teleport(player, target, TeleportType.DENY);
    }

    public static Teleport cancel(UUID player, UUID target) {
        return new Teleport(player, target, TeleportType.CANCEL);
    }

    public UUID getPlayer() {
        return this.player;
    }

    public UUID getTarget() {
        return this.target;
    }

    public TeleportType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "Teleport{" +
                "player=" + this.player +
                ", target=" + this.target +
                ", type=" + this.type +
                '}';
    }

    public enum TeleportType {
        REQUEST,
        ACCEPT,
        DENY,
        CANCEL
    }
}
