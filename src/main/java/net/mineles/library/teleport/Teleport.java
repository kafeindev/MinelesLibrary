package net.mineles.library.teleport;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class Teleport {
    private final @NotNull UUID player;
    private final @NotNull UUID target;
    private final @NotNull TeleportType type;

    public Teleport(@NotNull UUID player,
                    @NotNull UUID target,
                    @NotNull TeleportType type) {
        this.player = player;
        this.target = target;
        this.type = type;
    }

    @NotNull
    public static Teleport of(@NotNull UUID player,
                              @NotNull UUID target,
                              @NotNull TeleportType type) {
        return new Teleport(player, target, type);
    }

    @NotNull
    public static Teleport of(@NotNull String player,
                              @NotNull String target,
                              @NotNull String type) {
        return new Teleport(UUID.fromString(player), UUID.fromString(target), TeleportType.valueOf(type));
    }

    @NotNull
    public static Teleport request(@NotNull UUID player, @NotNull UUID target) {
        return new Teleport(player, target, TeleportType.REQUEST);
    }

    @NotNull
    public static Teleport accept(@NotNull UUID player, @NotNull UUID target) {
        return new Teleport(player, target, TeleportType.ACCEPT);
    }

    @NotNull
    public static Teleport deny(@NotNull UUID player, @NotNull UUID target) {
        return new Teleport(player, target, TeleportType.DENY);
    }

    @NotNull
    public static Teleport cancel(@NotNull UUID player, @NotNull UUID target) {
        return new Teleport(player, target, TeleportType.CANCEL);
    }

    public @NotNull UUID getPlayer() {
        return this.player;
    }

    public @NotNull UUID getTarget() {
        return this.target;
    }

    public @NotNull TeleportType getType() {
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
