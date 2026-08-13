package org.geysermc.hydraulic.pack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        shortenLongPaths(path);
        clampGeometryBounds(path, logger);

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

    // Bedrock's custom block geometry error bounds are (-0.875, -0.875, -0.875) to (1.875, 1.875, 1.875).
    // Cubes that poke outside these bounds make the client log "contains N boxes outside the error bounds"
    // and every custom block in the same chunk disappears (MCPE-152191). Clip cubes back into bounds.
    private static void clampGeometryBounds(@NotNull Path root, @NotNull LogListener logger) throws IOException {
        final double MIN = -0.875;
        final double MAX = 1.875;
        int clampedFiles = 0;

        try (Stream<Path> walker = Files.walk(root)) {
            final List<Path> geometries = walker.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".json")).toList();
            for (final Path file : geometries) {
                final String raw = Files.readString(file, StandardCharsets.UTF_8);
                final JsonObject json;
                try {
                    json = JsonParser.parseString(raw).getAsJsonObject();
                } catch (Exception ignored) {
                    continue; // not a valid JSON geometry, leave it alone
                }
                final JsonArray geometryList = json.has("minecraft:geometry")
                        ? json.getAsJsonArray("minecraft:geometry") : null;
                if (geometryList == null) {
                    continue;
                }

                boolean modified = false;
                for (JsonElement geometryElement : geometryList) {
                    if (!geometryElement.isJsonObject()) continue;
                    final JsonArray bones = geometryElement.getAsJsonObject().getAsJsonArray("bones");
                    if (bones == null) continue;
                    for (JsonElement boneElement : bones) {
                        if (!boneElement.isJsonObject()) continue;
                        final JsonObject bone = boneElement.getAsJsonObject();
                        final JsonArray cubes = bone.getAsJsonArray("cubes");
                        if (cubes == null) continue;

                        final List<JsonElement> toRemove = new ArrayList<>();
                        for (JsonElement cubeElement : cubes) {
                            if (!cubeElement.isJsonObject()) continue;
                            final JsonObject cube = cubeElement.getAsJsonObject();
                            final JsonArray origin = cube.getAsJsonArray("origin");
                            final JsonArray size = cube.getAsJsonArray("size");
                            if (origin == null || size == null || origin.size() < 3 || size.size() < 3) continue;

                            boolean remove = false;
                            boolean cubeModified = false;
                            for (int axis = 0; axis < 3; axis++) {
                                double min = origin.get(axis).getAsDouble();
                                double max = min + size.get(axis).getAsDouble();
                                if (max <= MIN || min >= MAX) {
                                    remove = true; // fully outside the error bounds, drop the cube
                                    break;
                                }
                                final double newMin = Math.max(min, MIN);
                                final double newMax = Math.min(max, MAX);
                                if (newMin != min) {
                                    size.set(axis, new JsonPrimitive(Math.max(0, newMax - newMin)));
                                    origin.set(axis, new JsonPrimitive(newMin));
                                    cubeModified = true;
                                } else if (newMax != max) {
                                    size.set(axis, new JsonPrimitive(Math.max(0, newMax - min)));
                                    cubeModified = true;
                                }
                            }
                            if (remove) {
                                toRemove.add(cubeElement);
                                modified = true;
                            } else if (cubeModified) {
                                modified = true;
                            }
                        }
                        for (JsonElement element : toRemove) {
                            cubes.remove(element);
                        }
                    }
                }

                if (modified) {
                    Files.writeString(file, json.toString(), StandardCharsets.UTF_8);
                    clampedFiles++;
                }
            }
        }

        if (clampedFiles > 0) {
            logger.info("Clamped out-of-bounds geometry in " + clampedFiles + " geometry file(s)");
        }
    }
}
