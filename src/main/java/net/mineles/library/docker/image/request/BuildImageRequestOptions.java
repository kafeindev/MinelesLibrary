package net.mineles.library.docker.image.request;

import com.github.dockerjava.api.command.BuildImageCmd;

import java.util.function.UnaryOperator;

public enum BuildImageRequestOptions {

    PULL(buildImageCmd -> buildImageCmd.withPull(true)),
    NO_CACHE(buildImageCmd -> buildImageCmd.withNoCache(true)), // we should always use this option
    REMOVE_INTERMEDIATE_CONTAINERS(buildImageCmd -> buildImageCmd.withRemove(true));

    private final UnaryOperator<BuildImageCmd> option;

    BuildImageRequestOptions(UnaryOperator<BuildImageCmd> option) {
        this.option = option;
    }

    public BuildImageCmd apply(BuildImageCmd cmd) {
        return option.apply(cmd);
    }
}
