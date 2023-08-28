package net.mineles.library.docker.container.options;

import com.github.dockerjava.api.command.ListContainersCmd;

import java.util.Collections;
import java.util.function.UnaryOperator;

public enum ContainerListOptions {

    ALL(listContainersCmd -> listContainersCmd.withShowAll(true)),

    EXITED(listContainersCmd -> listContainersCmd.withStatusFilter(Collections.singleton("exited"))),

    RUNNING(listContainersCmd -> listContainersCmd.withStatusFilter(Collections.singleton("running"))),

    SHOW_SIZE(listContainersCmd -> listContainersCmd.withShowSize(true));



    private final UnaryOperator<ListContainersCmd> option;

    ContainerListOptions(UnaryOperator<ListContainersCmd> option) {
        this.option = option;
    }

    public ListContainersCmd apply(ListContainersCmd cmd) {
        return option.apply(cmd);
    }
}
