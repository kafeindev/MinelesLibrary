package net.mineles.library.redis.codec;

import com.google.gson.JsonObject;
import net.mineles.library.utils.GsonProvider;
import org.jetbrains.annotations.NotNull;

public interface Decoder<T> {
    default @NotNull T decode(@NotNull String payload) {
        return decode(GsonProvider.getGson().fromJson(payload, JsonObject.class));
    }

    @NotNull T decode(@NotNull JsonObject payload);

    @NotNull String encode(@NotNull T payload);
}
