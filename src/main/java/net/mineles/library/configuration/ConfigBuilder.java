package net.mineles.library.configuration;

import net.mineles.library.node.Node;
import net.mineles.library.node.NodeException;
import net.mineles.library.node.loader.NodeLoader;
import net.mineles.library.node.loader.gson.GsonNodeLoader;
import net.mineles.library.node.loader.yaml.YamlNodeLoader;
import net.mineles.library.utils.file.FileLoader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigBuilder {
    private final @NotNull Path path;

    private ConfigType type = ConfigType.YAML;

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

    public @NotNull Path getPath() {
        return this.path;
    }

    public @NotNull ConfigType getType() {
        return this.type;
    }

    public @NotNull ConfigBuilder type(@NotNull ConfigType type) {
        this.type = type;
        return this;
    }

    public @NotNull ConfigBuilder init() {
        FileLoader fileLoader = FileLoader.of(this.path);
        try {
            fileLoader.load();
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public @NotNull ConfigBuilder initFromResource(@NotNull Class<?> clazz, @NotNull String resource) {
        FileLoader fileLoader = FileLoader.of(this.path);
        try {
            fileLoader.loadAndInitFromResource(clazz, resource);
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public @NotNull ConfigBuilder initViaStream(@NotNull InputStream inputStream) {
        FileLoader fileLoader = FileLoader.of(this.path);
        try {
            fileLoader.loadAndInitViaStream(inputStream);
            return this;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public @UnknownNullability Config build() {
        if (Files.notExists(path)) {
            throw new IllegalArgumentException("File does not exist. Please initialize the config first.");
        }

        try {
            NodeLoader nodeLoader = switch (type) {
                case YAML -> YamlNodeLoader.newBuilder().path(path).build();
                case JSON -> GsonNodeLoader.newBuilder().path(path).build();
            };

            Node node = nodeLoader.load();
            if (node == null) {
                throw new RuntimeException("Empty config");
            }

            return new Config(type, node, this.path);
        } catch (NodeException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }
}
