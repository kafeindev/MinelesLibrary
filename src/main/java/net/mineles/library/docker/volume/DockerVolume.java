package net.mineles.library.docker.volume;

public record DockerVolume(
        String name,
        String driver,
        String mountpoint
) {
}
