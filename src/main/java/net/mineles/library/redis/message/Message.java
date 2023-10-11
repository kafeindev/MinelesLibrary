package net.mineles.library.redis.message;

import org.jetbrains.annotations.NotNull;

public interface Message {
    static @NotNull Message create(@NotNull String channel,
                                   @NotNull String key,
                                   @NotNull String payload) {
        return new DefaultMessage(channel, key, payload);
    }

    static @NotNull MessageBuilder newBuilder() {
        return MessageBuilder.newBuilder();
    }

    @NotNull String getChannel();

    @NotNull String getKey();

    @NotNull String getPayload();

    default @NotNull MessageBuilder toBuilder() {
        return MessageBuilder.from(this);
    }
}
