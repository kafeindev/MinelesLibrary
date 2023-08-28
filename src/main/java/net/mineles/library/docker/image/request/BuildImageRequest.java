package net.mineles.library.docker.image.request;

import net.mineles.library.docker.image.RemoteImageTag;

import java.io.File;

public record BuildImageRequest(
        File dockerFile,
        RemoteImageTag tag,
        BuildImageRequestOptions... options

) {
}
