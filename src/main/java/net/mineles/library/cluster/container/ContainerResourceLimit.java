package net.mineles.library.cluster.container;

public record ContainerResourceLimit(
        long cpuPercent,
        long cpuCount,
        long memory
) {
}
