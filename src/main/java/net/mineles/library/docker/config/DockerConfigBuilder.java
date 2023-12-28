package net.mineles.library.docker.config;

import java.time.Duration;

public class DockerConfigBuilder {

    private String dockerHost;

    private String registryUsername;

    private String registryPassword;

    private String registryEmail;

    private String registryUrl;

    private Duration connectionTimeout = Duration.ofMinutes(3);

    public DockerConfigBuilder dockerHost(String dockerHost) {
        this.dockerHost = dockerHost;
        return this;
    }

    public DockerConfigBuilder registryUsername(String registryUsername) {
        this.registryUsername = registryUsername;
        return this;
    }

    public DockerConfigBuilder registryPassword(String registryPassword) {
        this.registryPassword = registryPassword;
        return this;
    }

    public DockerConfigBuilder registryEmail(String registryEmail) {
        this.registryEmail = registryEmail;
        return this;
    }

    public DockerConfigBuilder registryUrl(String registryUrl) {
        this.registryUrl = registryUrl;
        return this;
    }

    public DockerConfigBuilder connectionTimeout(Duration connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public DockerConfig build() {
        return new DockerConfig(dockerHost, registryUsername, registryPassword, registryEmail, registryUrl, connectionTimeout);
    }
}
