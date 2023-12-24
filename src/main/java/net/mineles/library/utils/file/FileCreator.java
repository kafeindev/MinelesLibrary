package net.mineles.library.utils.file;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileCreator {
    private final Path path;

    public FileCreator(Path path) {
        this.path = path;
    }

    public static FileCreator of(Path path) {
        return new FileCreator(path);
    }

    public static FileCreator of(String... path) {
        return new FileCreator(Path.of(String.join("/", path)));
    }

    public static FileCreator of(Path dataFolder, String... path) {
        return new FileCreator(dataFolder.resolve(String.join("/", path)));
    }

    public static FileCreator of(File file) {
        return new FileCreator(file.toPath());
    }

    public boolean create() throws IOException {
        Files.createDirectories(this.path.getParent());

        if (!Files.exists(this.path)) {
            Files.createFile(this.path);
            return true;
        }

        return false;
    }

    public boolean createAndInject(@NotNull Class<?> clazz, @NotNull String resource) throws IOException {
        InputStream inputStream = clazz.getResourceAsStream(resource);
        if (inputStream == null) throw new IOException("Resource not found: " + resource);

        return createAndInject(inputStream);
    }

    public boolean createAndInject(@NotNull InputStream inputStream) throws IOException {
        Files.createDirectories(this.path.getParent());
        if (Files.exists(this.path)) return false;

        Files.copy(inputStream, this.path);
        return true;
    }
}
