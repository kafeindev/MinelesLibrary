package net.mineles.library.redis.message;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Message {
    private final @NotNull String channel;
    private final @Nullable String key;
    private final @NotNull String payload;

    public Message(@NotNull String channel,
                   @Nullable String key,
                   @NotNull String payload) {
        this.channel = channel;
        this.key = key;
        this.payload = payload;
    }

    @NotNull
    public static Message create(@NotNull String channel,
                                 @NotNull String payload) {
        return new Message(channel, null, payload);
    }

    @NotNull
    public static Message create(@NotNull String channel,
                                 @Nullable String key,
                                 @NotNull String payload) {
        return new Message(channel, key, payload);
    }

    public @NotNull String getChannel() {
        return this.channel;
    }

    public @Nullable String getKey() {
        return this.key;
    }

    public @NotNull String getPayload() {
        return this.payload;
    }
}
