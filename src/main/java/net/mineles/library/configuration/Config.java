package net.mineles.library.configuration;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;

import java.nio.file.Path;
import java.util.Objects;

public final class Config {
    private final ConfigType type;
    private final ConfigurationNode node;
    private final @Nullable Path path;

    public Config(ConfigType type, ConfigurationNode node, @Nullable Path path) {
        this.node = node;
        this.type = type;
        this.path = path;
    }

    public ConfigType getType() {
        return this.type;
    }

    public ConfigurationNode getNode() {
        return this.node;
    }

    public @Nullable Path getPath() {
        return this.path;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Config)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Config config = (Config) obj;
        return Objects.equals(this.type, config.type)
                && Objects.equals(this.node, config.node)
                && Objects.equals(this.path, config.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.node, this.path);
    }

    @Override
    public String toString() {
        return "Config{" +
                "type=" + this.type +
                ", node=" + this.node +
                ", path=" + this.path +
                '}';
    }
}
