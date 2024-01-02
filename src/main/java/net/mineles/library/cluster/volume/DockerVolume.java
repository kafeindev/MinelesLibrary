package net.mineles.library.cluster.volume;

public record DockerVolume(
        String name,
        String driver,
        String mountpoint
) {
}
