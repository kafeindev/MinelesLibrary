package net.mineles.library.docker.config;

import net.mineles.library.libs.configurate.ConfigurationNode;

import java.time.Duration;

import static net.mineles.library.configuration.ConfigKey.getValueFromEnv;

public record DockerConfig(
        String dockerHost,
        String registryUsername,
        String registryPassword,
        String registryEmail,
        String registryUrl,
        Duration connectionTimeout
) {

    public static DockerConfig fromNode(ConfigurationNode node) {
        return new DockerConfigBuilder()
                .dockerHost(node.node("host").getString())
                .registryUsername(getValueFromEnv(node.node("user-name"), node.node("user-name").getString()))
                .registryPassword(getValueFromEnv(node.node("password"), node.node("password").getString()))
                .registryEmail(node.node("mail").getString())
                .registryUrl(node.node("url").getString())
                .build();
    }

    public static DockerConfigBuilder builder() {
        return new DockerConfigBuilder();
    }
}
