package net.mineles.library.utils.file;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileLoader {
    private final @NotNull Path path;

    public FileLoader(@NotNull Path path) {
        this.path = path;
    }

    @NotNull
    public static FileLoader of(@NotNull Path path) {
        return new FileLoader(path);
    }

    @NotNull
    public static FileLoader of(@NotNull String... path) {
        return new FileLoader(Path.of(String.join("/", path)));
    }

    @NotNull
    public static FileLoader of(@NotNull Path dataFolder, @NotNull String... path) {
        return new FileLoader(dataFolder.resolve(String.join("/", path)));
    }

    @NotNull
    public static FileLoader of(@NotNull File file) {
        return new FileLoader(file.toPath());
    }

    public boolean load() throws IOException {
        Files.createDirectories(this.path.getParent());

        if (!Files.exists(this.path)) {
            Files.createFile(this.path);
            return true;
        }

        return false;
    }

    public boolean loadAndInitFromResource(@NotNull Class<?> clazz, @NotNull String resource) throws IOException {
        InputStream inputStream = clazz.getResourceAsStream(resource);
        if (inputStream == null) throw new IOException("Resource not found: " + resource);

        return loadAndInitViaStream(inputStream);
    }

    public boolean loadAndInitViaStream(@NotNull InputStream inputStream) throws IOException {
        Files.createDirectories(this.path.getParent());
        if (Files.exists(this.path)) return false;

        Files.copy(inputStream, this.path);
        return true;
    }
}
