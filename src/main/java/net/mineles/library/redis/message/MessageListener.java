package net.mineles.library.redis.message;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MessageListener<T> {
    void onMessage(@NotNull MessageDecoder<T> decoder, @NotNull String channel, @NotNull T message);
}
