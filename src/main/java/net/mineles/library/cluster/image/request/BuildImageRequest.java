package net.mineles.library.cluster.image.request;

import net.mineles.library.cluster.image.RemoteImageTag;

import java.io.File;

public record BuildImageRequest(
        File dockerFile,
        RemoteImageTag tag,
        BuildImageRequestOptions... options

) {
}
