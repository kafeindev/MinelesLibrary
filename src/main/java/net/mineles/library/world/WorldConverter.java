package net.mineles.library.world;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class WorldConverter {
    private WorldConverter() {
    }

    public static File convertBytesToWorld(byte[] worldData, String worldName, File parentFolder) {
        try {
            File worldFolder = new File(parentFolder, worldName + "_temp");
            Files.createDirectories(worldFolder.toPath());

            File tempZipFile = new File(parentFolder, worldName + "_temps.zip");

            try (FileOutputStream fos = new FileOutputStream(tempZipFile)) {
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                ByteArrayInputStream bis = new ByteArrayInputStream(worldData);

                IOUtils.copy(bis, bos);
            }

            unzipFolder(tempZipFile, worldFolder);

            return worldFolder;
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert bytes to world", e);
        }
    }

    private static void unzipFolder(File zipFile, File destFolder) throws IOException {
        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipInputStream zis = new ZipInputStream(fis)) {

            byte[] buffer = new byte[1024];
            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                File newFile = new File(destFolder, zipEntry.getName());
                Files.createDirectories(newFile.getParentFile().toPath());
                Files.createFile(newFile.toPath());

                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }

                zis.closeEntry();
                zipEntry = zis.getNextEntry();
            }
        }
    }

    public static byte[] convertWorldToBytes(File worldFolder) {
        try {
            File tempZipFile = new File(worldFolder.getParentFile(), worldFolder.getName() + "_temp.zip");
            zipFolder(worldFolder, tempZipFile);

            byte[] worldData;
            try (FileInputStream fis = new FileInputStream(tempZipFile);
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                int len;
                byte[] buffer = new byte[1024];
                while ((len = fis.read(buffer)) > 0) {
                    baos.write(buffer, 0, len);
                }

                worldData = baos.toByteArray();
            }

            return worldData;
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert world to bytes", e);
        }
    }

    private static void zipFolder(File folder, File zipFile) {
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            zipFolder(folder, folder, zos);
        } catch (IOException e) {
            throw new RuntimeException("Failed to zip folder", e);
        }
    }

    private static void zipFolder(File rootFolder, File sourceFolder, ZipOutputStream zos) {
        for (File file : sourceFolder.listFiles()) {
            if (file.isDirectory()) {
                zipFolder(rootFolder, file, zos);
            } else {
                byte[] buffer = new byte[1024];
                int length;

                try (FileInputStream fis = new FileInputStream(file)) {
                    String name = file.getPath().substring(rootFolder.getPath().length() + 1);
                    zos.putNextEntry(new ZipEntry(name));

                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }

                    zos.closeEntry();
                } catch (IOException e) {
                    throw new RuntimeException("Failed to zip folder", e);
                }
            }
        }
    }
}
