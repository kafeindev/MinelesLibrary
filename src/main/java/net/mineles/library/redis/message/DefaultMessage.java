package net.mineles.library.redis.message;

import org.jetbrains.annotations.NotNull;

final class DefaultMessage implements Message {
    private final @NotNull String channel;
    private final @NotNull String key;
    private final @NotNull String payload;

    DefaultMessage(@NotNull String channel,
                   @NotNull String key,
                   @NotNull String payload) {
        this.channel = channel;
        this.key = key;
        this.payload = payload;
    }

    @Override
    public @NotNull String getChannel() {
        return this.channel;
    }

    @Override
    public @NotNull String getKey() {
        return this.key;
    }

    @Override
    public @NotNull String getPayload() {
        return this.payload;
    }
}
