package org.geysermc.hydraulic.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mojang.logging.LogUtils;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Utility class for files.
 */
public class FileUtil {
    protected static Logger LOGGER = LogUtils.getLogger();

    /**
     * Exports the specified object to the given location as JSON.
     *
     * @param mapper the object mapper to use
     * @param location the location to export the object to
     * @param object the object to export
     * @throws IOException if an I/O error occurs
     */
    public static void exportJson(@NotNull ObjectMapper mapper, @NotNull Path location, @NotNull Object object) throws IOException {
        if (Files.notExists(location.getParent())) {
            Files.createDirectories(location.getParent());
        }

        if (Files.notExists(location)) {
            Files.createFile(location);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(location)) {
            mapper.writeValue(writer, object);
        }
    }

    /**
     * Zips the specified directory to the given location.
     *
     * @param srcDirectory the directory to zip
     * @param destZipFile the location to zip the directory to
     * @throws Exception if an error occurs
     */
    public static void zipDirectory(@NotNull File srcDirectory, @NotNull File destZipFile) throws Exception {
        try (FileOutputStream fileWriter = new FileOutputStream(destZipFile);
             ZipOutputStream zip = new ZipOutputStream(fileWriter)) {

            addDirectoryToZip(srcDirectory, srcDirectory, zip);
        }
    }

    private static void addFileToZip(@NotNull File rootPath, @NotNull File srcFile, @NotNull ZipOutputStream zip) throws Exception {
        if (srcFile.isDirectory()) {
            addDirectoryToZip(rootPath, srcFile, zip);
        } else {
            byte[] buf = new byte[1024];
            int len;
            try (FileInputStream in = new FileInputStream(srcFile)) {
                String name = srcFile.getPath();
                name = name.replace(rootPath.getPath(), "");
                if (name.startsWith("\\")) {
                    name = name.substring(1);
                }
                zip.putNextEntry(new ZipEntry(name));
                while ((len = in.read(buf)) > 0) {
                    zip.write(buf, 0, len);
                }
            }
        }
    }

    private static void addDirectoryToZip(@NotNull File rootPath, @NotNull File srcDirectory, ZipOutputStream zip) throws Exception {
        for (File fileName : srcDirectory.listFiles()) {
            addFileToZip(rootPath, fileName, zip);
        }
    }

    /**
     * Copies a file from a mod to the specified output path.
     *
     * @param mod the mod to copy the file from
     * @param filePath the path to the file to copy
     * @param outputPath the path to copy the file to
     */
    public static void copyFileFromMod(ModInfo mod, String filePath, Path outputPath) {
        Path iconPath = mod.modPath().resolve(filePath);
        if (Files.exists(iconPath)) {
            try {
                if (Files.notExists(outputPath.getParent())) {
                    Files.createDirectories(outputPath.getParent());
                }

                Files.copy(iconPath, outputPath, StandardCopyOption.REPLACE_EXISTING);
                LOGGER.debug("Copied file {} for mod {}", iconPath, mod.id());
            } catch (IOException ex) {
                LOGGER.error("Failed to copy file {} for mod {}", iconPath, mod.id(), ex);
            }
        } else {
            LOGGER.warn("File {} not found for mod {}", iconPath, mod.id());
        }
    }
}
