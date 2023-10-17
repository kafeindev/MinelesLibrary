package net.mineles.library.redis.codec;

import com.google.common.collect.Maps;
import net.mineles.library.teleport.Teleport;
import net.mineles.library.teleport.TeleportDecoder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public final class DecoderCollection {
    private static final DecoderCollection DEFAULTS;

    static {
        DEFAULTS = DecoderCollection.newBuilder()
                .registerDecoder(Teleport.class, new TeleportDecoder())
                .build();
    }

    private final @NotNull Map<Class<?>, Decoder<?>> decoders;

    public DecoderCollection() {
        this(DecoderCollection.DEFAULTS);
    }

    public DecoderCollection(@NotNull DecoderCollection collection) {
        this(collection.getDecoders());
    }

    public DecoderCollection(@NotNull Map<Class<?>, Decoder<?>> decoders) {
        this.decoders = decoders;
    }

    public @NotNull Map<Class<?>, Decoder<?>> getDecoders() {
        return this.decoders;
    }

    public @NotNull Set<Class<?>> getKeys() {
        return this.decoders.keySet();
    }

    @SuppressWarnings("unchecked")
    public <T> Decoder<T> getDecoder(@NotNull Class<?> type) {
        return (Decoder<T>) this.decoders.get(type);
    }

    public boolean isRegistered(@NotNull Class<?> type) {
        return this.decoders.containsKey(type);
    }

    public <T> void registerDecoder(@NotNull Class<T> type, @NotNull Decoder<T> decoder) {
        this.decoders.put(type, decoder);
    }

    public void unregisterDecoder(@NotNull Class<?> type) {
        this.decoders.remove(type);
    }

    @NotNull
    public static Builder newBuilder() {
        return new DecoderCollection.Builder();
    }

    public static final class Builder {
        private final @NotNull Map<Class<?>, Decoder<?>> decoders;

        private Builder() {
            this.decoders = Maps.newHashMap();
        }

        public @NotNull Builder registerDecoder(@NotNull Class<?> type, @NotNull Decoder<?> decoder) {
            this.decoders.put(type, decoder);
            return this;
        }

        public @NotNull Builder unregisterDecoder(@NotNull Class<?> type) {
            this.decoders.remove(type);
            return this;
        }

        public @NotNull DecoderCollection build() {
            return new DecoderCollection(this.decoders);
        }
    }
}
