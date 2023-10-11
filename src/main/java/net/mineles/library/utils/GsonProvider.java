package net.mineles.library.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

public final class GsonProvider {
    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    private GsonProvider() {
    }

    public static @NotNull Gson getGson() {
        return GSON;
    }
}
