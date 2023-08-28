package net.mineles.library.docker.volume;

public record CreateVolumeRequest(
        String name,
        String driver
) {
    public CreateVolumeRequest(String name) {
        this(name, "local");
    }
}
