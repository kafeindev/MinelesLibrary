package net.mineles.library.cluster.image;

import org.jetbrains.annotations.NotNull;

public record RemoteImageTag(@NotNull String image, String tag) {

    public RemoteImageTag(@NotNull String image) {
        this(image, "latest");
    }

    public static @NotNull RemoteImageTag parse(@NotNull String image) {
        String[] parts = image.split(":", 2);
        if (parts.length == 1) {
            return new RemoteImageTag(parts[0]);
        } else if (parts.length == 2) {
            return new RemoteImageTag(parts[0], parts[1]);
        } else {
            throw new IllegalArgumentException("Invalid image name: " + image);
        }
    }

    public @NotNull String getFullName() {
        return image + ":" + tag;
    }
}
