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
    private final @NotNull Path path;

    private ConfigType type = ConfigType.YAML;
    private ConfigurationOptions options = ConfigurationOptions.defaults();
    private InputStream resource;

    public ConfigBuilder(@NotNull Path path) {
        this.path = path;
    }

    public ConfigBuilder(@NotNull String... path) {
        this.path = Path.of(String.join("/", path));
    }

    public ConfigBuilder(@NotNull Path dataFolder, @NotNull String... path) {
        this.path = dataFolder.resolve(String.join("/", path));
    }

    public ConfigBuilder(@NotNull File file) {
        this.path = file.toPath();
    }

    public static @NotNull ConfigBuilder builder(@NotNull Path path) {
        return new ConfigBuilder(path);
    }

    public static @NotNull ConfigBuilder builder(@NotNull String... path) {
        return new ConfigBuilder(path);
    }

    public static @NotNull ConfigBuilder builder(@NotNull Path dataFolder, @NotNull String... path) {
        return new ConfigBuilder(dataFolder, path);
    }

    public static @NotNull ConfigBuilder builder(@NotNull File file) {
        return new ConfigBuilder(file);
    }

    public @NotNull Path path() {
        return this.path;
    }

    public @NotNull ConfigType type() {
        return this.type;
    }

    public @NotNull ConfigBuilder type(@NotNull ConfigType type) {
        this.type = type;
        return this;
    }

    public @NotNull ConfigurationOptions options() {
        return this.options;
    }

    public @NotNull ConfigBuilder options(@NotNull ConfigurationOptions options) {
        this.options = options;
        return this;
    }

    public @NotNull InputStream resource() {
        return this.resource;
    }

    public @NotNull ConfigBuilder resource(@NotNull Class<?> clazz, @NotNull String resource) {
        this.resource = clazz.getResourceAsStream(resource);
        return this;
    }

    public @NotNull ConfigBuilder resource(@NotNull InputStream inputStream) {
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
