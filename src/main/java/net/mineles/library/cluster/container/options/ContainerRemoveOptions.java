package net.mineles.library.cluster.container.options;

import com.github.dockerjava.api.command.RemoveContainerCmd;

import java.util.function.UnaryOperator;

public enum ContainerRemoveOptions {

    FORCE(removeContainerCmd -> removeContainerCmd.withForce(true)),
    VOLUME(removeContainerCmd -> removeContainerCmd.withRemoveVolumes(true));

    private final UnaryOperator<RemoveContainerCmd> option;

    ContainerRemoveOptions(UnaryOperator<RemoveContainerCmd> option) {
        this.option = option;
    }

    public RemoveContainerCmd apply(RemoveContainerCmd cmd) {
        return option.apply(cmd);
    }
}
