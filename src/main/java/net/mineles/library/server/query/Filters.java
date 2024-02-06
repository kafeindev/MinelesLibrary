package net.mineles.library.server.query;

import net.mineles.library.server.Server;

public final class Filters {
    private Filters() {
    }

    public static Filter name(String name) {
        return server -> server.getName().equalsIgnoreCase(name);
    }

    public static Filter image(String image) {
        if (image.split(":").length > 1) {
            image = image.split(":")[1];
        }
        String finalImage = image;
        return server -> {
            String serverImage = server.getImage();
            if (serverImage.split(":").length > 1) {
                serverImage = serverImage.split(":")[1];
            }
            return serverImage.equalsIgnoreCase(finalImage);
        };
    }

    public static Filter online() {
        return Server::isStarted;
    }
}
