package net.mineles.library.docker.image;

import net.mineles.library.libs.dockerjava.api.model.Image;

public record DockerImage(
        String id,
        String name,
        String tag,
        String digest,
        long size
) {

    public static DockerImage fromImage(Image image) {
        return new DockerImage(
                image.getId(),
                image.getRepoTags()[0],
                image.getRepoTags()[1],
                image.getRepoDigests()[0],
                image.getSize()
        );
    }
}
