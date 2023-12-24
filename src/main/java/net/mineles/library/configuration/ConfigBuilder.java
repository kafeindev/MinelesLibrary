package net.mineles.library.configuration;

import net.mineles.library.utils.file.FileCreator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public final class ConfigBuilder {
    private final Path path;

    private ConfigType type = ConfigType.YAML;
    private ConfigurationOptions options = ConfigurationOptions.defaults();
    private InputStream resource;

    public ConfigBuilder(Path path) {
        this.path = path;
    }

    public ConfigBuilder(String... path) {
        this.path = Path.of(String.join("/", path));
    }

    public ConfigBuilder(Path dataFolder, String... path) {
        this.path = dataFolder.resolve(String.join("/", path));
    }

    public ConfigBuilder(File file) {
        this.path = file.toPath();
    }

    public static ConfigBuilder builder(@NotNull Path path) {
        return new ConfigBuilder(path);
    }

    public static ConfigBuilder builder(@NotNull String... path) {
        return new ConfigBuilder(path);
    }

    public static ConfigBuilder builder(@NotNull Path dataFolder, @NotNull String... path) {
        return new ConfigBuilder(dataFolder, path);
    }

    public static ConfigBuilder builder(@NotNull File file) {
        return new ConfigBuilder(file);
    }

    public Path path() {
        return this.path;
    }

    public ConfigType type() {
        return this.type;
    }

    public ConfigBuilder type(@NotNull ConfigType type) {
        this.type = type;
        return this;
    }

    public ConfigurationOptions options() {
        return this.options;
    }

    public ConfigBuilder options(@NotNull ConfigurationOptions options) {
        this.options = options;
        return this;
    }

    public InputStream resource() {
        return this.resource;
    }

    public ConfigBuilder resource(@NotNull Class<?> clazz, @NotNull String resource) {
        this.resource = clazz.getResourceAsStream(resource);
        return this;
    }

    public ConfigBuilder resource(@NotNull InputStream inputStream) {
        this.resource = inputStream;
        return this;
    }

    public @UnknownNullability Config build() {
        FileCreator fileCreator = FileCreator.of(this.path);
        try {
            if (this.resource != null) {
                fileCreator.createAndInject(this.resource);
            } else {
                fileCreator.create();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create file", e);
        }

        try {
            ConfigurationLoader<?> nodeLoader = switch (this.type) {
                case YAML -> YamlConfigurationLoader.builder()
                        .path(this.path)
                        .defaultOptions(this.options)
                        .build();
                case JSON -> GsonConfigurationLoader.builder()
                        .path(this.path)
                        .defaultOptions(this.options)
                        .build();
            };

            ConfigurationNode node = nodeLoader.load();
            if (node == null) {
                throw new RuntimeException("Empty config");
            }

            return new Config(type, node, this.path);
        } catch (ConfigurateException e) {
            throw new RuntimeException("Failed to create config", e);
        }
    }
}
