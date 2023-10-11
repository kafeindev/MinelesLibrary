package net.mineles.library.redis.message;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import static com.google.common.base.Preconditions.checkNotNull;

public final class MessageBuilder {
    private String channel;
    private String key;
    private String payload;

    private MessageBuilder() {}

    @NotNull
    public static MessageBuilder newBuilder() {
        return new MessageBuilder();
    }

    @NotNull
    public static MessageBuilder from(@NotNull Message message) {
        return MessageBuilder.newBuilder()
                .channel(message.getChannel())
                .key(message.getKey())
                .payload(message.getPayload());
    }

    public @UnknownNullability String channel() {
        return this.channel;
    }

    public @NotNull MessageBuilder channel(@NotNull String channel) {
        this.channel = channel;
        return this;
    }

    public @UnknownNullability String key() {
        return this.key;
    }

    public @NotNull MessageBuilder key(@NotNull String key) {
        this.key = key;
        return this;
    }

    public @UnknownNullability String payload() {
        return this.payload;
    }

    public @NotNull MessageBuilder payload(@NotNull String payload) {
        this.payload = payload;
        return this;
    }

    public @NotNull Message build() {
        return new DefaultMessage(
                checkNotNull(this.channel, "channel"),
                checkNotNull(this.key, "key"),
                checkNotNull(this.payload, "payload")
        );
    }
}
