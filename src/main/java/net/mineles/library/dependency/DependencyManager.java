package net.mineles.library.dependency;

import me.lucko.jarrelocator.JarRelocator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class DependencyManager {
    private final Path libsPath;

    public DependencyManager() {
        this(Path.of("libs"));
    }

    public DependencyManager(Path libsPath) {
        this.libsPath = libsPath;
    }

    public CompletableFuture<URL[]> loadAllFromRepository() {
        return loadAllFromRepository(Dependency.values());
    }

    public CompletableFuture<URL[]> loadAllFromRepository(Dependency... dependencies) {
        return loadAllFromRepository(List.of(dependencies));
    }

    public CompletableFuture<URL[]> loadAllFromRepository(List<Dependency> dependencies) {
        return CompletableFuture.supplyAsync(() -> {
            URL[] urls = new URL[dependencies.size()];

            for (Dependency dependency : dependencies) {
                File file = loadFromRepository(dependency).join();
                if (file == null) {
                    throw new RuntimeException("Failed to load dependency: " + dependency.getArtifactId());
                }

                try {
                    urls[dependencies.indexOf(dependency)] = file.toURI().toURL();
                } catch (MalformedURLException e) {
                    throw new RuntimeException("Failed to load dependency: " + dependency.getArtifactId(), e);
                }
            }

            return urls;
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    public CompletableFuture<File[]> loadAllFromRepositoryAsFiles() {
        return loadAllFromRepositoryAsFiles(Dependency.values());
    }

    public CompletableFuture<File[]> loadAllFromRepositoryAsFiles(Dependency... dependencies) {
        return loadAllFromRepositoryAsFiles(List.of(dependencies));
    }

    public CompletableFuture<File[]> loadAllFromRepositoryAsFiles(List<Dependency> dependencies) {
        return CompletableFuture.supplyAsync(() -> {
            File[] files = new File[dependencies.size()];

            for (Dependency dependency : dependencies) {
                File file = loadFromRepository(dependency).join();
                if (file == null) {
                    throw new RuntimeException("Failed to load dependency: " + dependency.getArtifactId());
                }

                files[dependencies.indexOf(dependency)] = file;
            }

            return files;
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    public CompletableFuture<File> loadFromRepository(@NotNull Dependency dependency) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!Files.exists(this.libsPath)) {
                    Files.createDirectory(this.libsPath);
                }

                String urlString = dependency.getRepository().format(dependency);
                URL url = new URL(urlString);

                File file = this.libsPath.resolve(dependency.getArtifactId() + "-" + dependency.getVersion() + ".jar").toFile();
                if (!file.exists()) {
                    Files.copy(url.openStream(), file.toPath());
                }

                File relocatedFile = this.libsPath.resolve(dependency.getArtifactId() + "-" + dependency.getVersion() + "-relocated.jar").toFile();
                if (!relocatedFile.exists()) {
                    JarRelocator jarRelocator = new JarRelocator(file, relocatedFile, dependency.getRelocations());
                    jarRelocator.run();
                }

                return relocatedFile;
            } catch (Exception e) {
                throw new RuntimeException("Failed to load dependency: " + dependency.getArtifactId(), e);
            }
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }
}
