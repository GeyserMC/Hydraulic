package org.geysermc.hydraulic.pack;

import org.geysermc.pack.converter.PackConverter;
import org.geysermc.pack.converter.PackageHandler;
import org.geysermc.pack.converter.util.LogListener;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Packs the pack into a zip file unless its empty.
 * <p>
 * Passes over to {@link PackageHandler#ZIP} after its finished its checks.
 */
public class PackPackager implements PackageHandler {
    @Override
    public void pack(@NotNull PackConverter converter, @NotNull Path path, @NotNull Path outputPath, @NotNull LogListener logger) throws IOException {
        boolean notEmptyPack = true;
        try (Stream<Path> walker = Files.walk(path)) {
            // Check if there is a file other than manifest.json and pack_icon.png
            notEmptyPack = walker.filter(Files::isRegularFile).anyMatch(filePath -> !(filePath.getFileName().toString().equals("manifest.json") || filePath.getFileName().toString().equals("pack_icon.png")));
        } catch (IOException ignored) {
        }

        // Ignore empty packs
        if (!notEmptyPack) {
            return;
        }

        PackageHandler.ZIP.pack(converter, path, outputPath, logger);
    }
}
