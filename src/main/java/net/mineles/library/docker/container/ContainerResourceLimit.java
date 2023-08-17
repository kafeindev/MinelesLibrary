package net.mineles.library.docker.container;

public record ContainerResourceLimit(
        long cpuPercent,
        long cpuCount,
        long memory
) {
}
