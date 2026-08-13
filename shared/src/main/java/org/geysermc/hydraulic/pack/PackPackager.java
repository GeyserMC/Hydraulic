package org.geysermc.hydraulic.pack;

import org.geysermc.hydraulic.util.PackUtil;
import org.geysermc.pack.converter.PackConverter;
import org.geysermc.pack.converter.PackageHandler;
import org.geysermc.pack.converter.util.LogListener;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

// TODO Probably just do an empty pack check in PackConverter?
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

        shortenLongPaths(path);

        PackageHandler.ZIP.pack(converter, path, outputPath, logger);
    }

    /**
     * Bedrock clients on some platforms fail to load files whose pack path is 80 characters or longer.
     * <p>
     * The pack converter generates texture paths from the mod's asset layout, which can exceed the limit
     * for mods with deeply nested assets (e.g. Waystones). This walks the generated pack, moves any
     * over-long files to shortened hashed paths and updates all JSON references accordingly.
     *
     * @param root the unpacked pack directory
     * @throws IOException if an I/O error occurs
     */
    private static void shortenLongPaths(@NotNull Path root) throws IOException {
        final Map<String, String> renames = new HashMap<>();

        try (Stream<Path> walker = Files.walk(root)) {
            final List<Path> files = walker.filter(Files::isRegularFile).toList();
            for (final Path file : files) {
                final String rel = root.relativize(file).toString().replace('\\', '/');
                if (rel.length() < 80) {
                    continue;
                }

                String base = rel;
                String extension = "";
                int dot = rel.lastIndexOf('.');
                if (dot > rel.lastIndexOf('/')) {
                    extension = rel.substring(dot);
                    base = rel.substring(0, dot);
                }

                final String shortBase = PackUtil.limitPathLength(base, 75);
                final Path target = root.resolve((shortBase + extension).replace('/', java.io.File.separatorChar));
                Files.createDirectories(target.getParent());
                Files.move(file, target, StandardCopyOption.REPLACE_EXISTING);
                renames.put(base, shortBase);
            }
        }

        if (renames.isEmpty()) {
            return;
        }

        try (Stream<Path> walker = Files.walk(root)) {
            final List<Path> jsons = walker.filter(p -> Files.isRegularFile(p) && p.toString().endsWith(".json")).toList();
            for (final Path json : jsons) {
                String content = Files.readString(json, StandardCharsets.UTF_8);
                String updated = content;
                for (final Map.Entry<String, String> rename : renames.entrySet()) {
                    updated = updated.replace(rename.getKey(), rename.getValue());
                }
                if (!updated.equals(content)) {
                    Files.writeString(json, updated, StandardCharsets.UTF_8);
                }
            }
        }
    }
}
