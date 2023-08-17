package net.mineles.library.docker.config;

import java.time.Duration;

public record DockerConfig(
        String dockerHost,
        String registryUsername,
        String registryPassword,
        String registryEmail,
        String registryUrl,
        Duration connectionTimeout
) {

    public static DockerConfigBuilder builder() {
        return new DockerConfigBuilder();
    }
}
