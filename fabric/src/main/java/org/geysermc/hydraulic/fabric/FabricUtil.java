package org.geysermc.hydraulic.fabric;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.GsonHelper;
import org.geysermc.hydraulic.fabric.platform.HydraulicFabricBootstrap;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Utilities used to implement {@link HydraulicFabricBootstrap}.
 */
public class FabricUtil {
    private static final List<String> USER_BLOCKED_MODS = Arrays.stream(
            System.getProperty("hydraulic.ignored_mods", "").split(",")
    ).toList();

    public static final int ICON_SIZE = 256;

    /**
     * Creates {@link ModInfo} objects for each mod in a jar, including JiJ'd mods, recursively.
     * @see #resolveJiJ(Collection, Consumer)
     *
     * @param roots The root paths of a mod. Usually only one element, but may be more if using classpath groups.
     * @return A stream that returns each created {@link ModInfo} object.
     */
    public static Stream<ModInfo> resolveJiJ(Collection<Path> roots) {
        return Stream.of(roots).mapMulti(FabricUtil::resolveJiJ);
    }

    /**
     * Creates {@link ModInfo} objects for each mod in a jar, including JiJ'd mods, recursively.
     * @see #resolveJiJ(Collection)
     *
     * @param roots The root paths of a mod. Usually only one element, but may be more if using classpath groups.
     * @param output Invoked with each created {@link ModInfo} object.
     */
    // We aren't modifying jars, therefore we don't need to close() them, as they'll simply get GCed.
    @SuppressWarnings("resource")
    public static void resolveJiJ(Collection<Path> roots, Consumer<ModInfo> output) {
        final Path fmjPath = resolveFile(roots, "fabric.mod.json");
        if (fmjPath == null) return;

        final JsonObject fmj;
        try (Reader reader = Files.newBufferedReader(fmjPath)) {
            fmj = GsonHelper.parse(reader);
        } catch (IOException ignored) {
            return;
        }

        final JsonElement schemaVersionJson = fmj.get("schemaVersion");
        if (schemaVersionJson != null && !schemaVersionJson.isJsonPrimitive()) return;
        final int schemaVersion;
        try {
            schemaVersion = schemaVersionJson != null ? schemaVersionJson.getAsInt() : 0;
        } catch (NumberFormatException e) {
            return;
        }
        if (schemaVersion < 0 || schemaVersion > 1) return;

        JsonElement environment = fmj.get("environment");
        if (environment == null) {
            environment = fmj.get("side"); // v0
        }
        if (environment instanceof JsonPrimitive && environment.getAsString().equals("server")) return;

        final ModInfo modInfo = createModInfo(fmj, roots, schemaVersion);
        if (modInfo == null) return;
        output.accept(modInfo);

        // Fabric loads JiJ by reading a list of JiJ'd mods from a "jars" element inside fabric.mod.json. This is an
        // array of objects where each object has a field called "file" that specifies the path, relative to the root of
        // the jar, of the JiJ'd mod. See V1ModMetadataParser.
        final JsonElement jarsElement = fmj.get("jars");
        if (!(jarsElement instanceof JsonArray jarsArray)) return;
        for (final JsonElement element : jarsArray) {
            if (!element.isJsonObject()) continue;
            final JsonElement innerFileElement = element.getAsJsonObject().get("file");
            if (!(innerFileElement instanceof JsonPrimitive)) continue;
            final Path innerPath = resolveFile(roots, innerFileElement.getAsString());
            if (innerPath == null) continue;
            final FileSystem fileSystem;
            try {
                fileSystem = FileSystems.newFileSystem(innerPath);
            } catch (IOException ignored) {
                continue;
            }
            resolveJiJ(toCollection(fileSystem.getRootDirectories()), output);
        }
    }

    @Nullable
    private static ModInfo createModInfo(JsonObject metadata, Collection<Path> roots, int schemaVersion) {
        // Fabric Loom generation checks, if generated, is not actually a mod, but a library
        final JsonElement custom = metadata.get("custom");
        if (custom instanceof JsonObject object) {
            if (
                    new JsonPrimitive(true).equals(object.get("fabric-loom:generated")) ||
                    new JsonPrimitive(true).equals(object.get("hydraulic:ignore"))
            ) {
                return null;
            } else if (object.get("modmenu") instanceof JsonObject modMenuData) {
                final JsonElement badges = modMenuData.get("badges");
                if (badges instanceof JsonArray badgeArray && badgeArray.contains(new JsonPrimitive("library"))) {
                    return null;
                }
            }
        }

        // Server sided mod check, ignore polymer content and other lib mods
        final JsonElement environment = metadata.get("environment");
        if (environment instanceof JsonPrimitive primitive && primitive.getAsString().equals("server")) {
            return null;
        }

        final JsonElement id = metadata.get("id");
        final JsonElement version = metadata.get("version");
        if (!(id instanceof JsonPrimitive) || !(version instanceof JsonPrimitive)) {
            return null;
        }

        if (USER_BLOCKED_MODS.contains(id.getAsString())) return null;

        JsonElement name = metadata.get("name");
        if (name != null && !(name instanceof JsonPrimitive)) {
            return null;
        }
        if (!(name instanceof JsonPrimitive) || name.getAsString().isEmpty()) {
            name = id;
        }

        final String iconPath = switch (schemaVersion) {
            case 1 -> {
                final JsonElement iconElement = metadata.get("icon");
                if (iconElement == null) {
                    yield null;
                }
                if (iconElement.isJsonPrimitive()) {
                    yield iconElement.getAsString();
                }
                if (!iconElement.isJsonObject()) {
                    yield null;
                }
                final SortedMap<Integer, String> icons = new TreeMap<>();
                for (final var entry : iconElement.getAsJsonObject().entrySet()) {
                    if (!entry.getValue().isJsonPrimitive()) continue;
                    final int size;
                    try {
                        size = Integer.parseInt(entry.getKey());
                    } catch (NumberFormatException e) {
                        continue;
                    }
                    if (size < 1) continue;
                    icons.put(size, entry.getValue().getAsString());
                }
                int iconValue = -1;
                for (int i : icons.keySet()) {
                    iconValue = i;
                    if (iconValue >= ICON_SIZE) break;
                }
                yield icons.get(iconValue);
            }
            default -> "assets/" + id.getAsString() + "/icon.png";
        };

        return new ModInfo(
            id.getAsString(),
            id.getAsString(),
            name.getAsString(),
            version.getAsString(),
            iconPath != null ? resolveFile(roots, iconPath) : null,
            roots
        );
    }

    /**
     * Finds the first file matching the specified path in the specified roots.
     *
     * @param roots The root directories to search. Earlier elements are searched first.
     * @param file The file to search for.
     * @return The {@link Path} to the searched file, or {@code null} if it wasn't found.
     */
    @Nullable
    public static Path resolveFile(Collection<Path> roots, String file) {
        for (final Path path : roots) {
            final Path resolved = path.resolve(file.replace("/", path.getFileSystem().getSeparator()));
            if (Files.isRegularFile(resolved)) {
                return resolved;
            }
        }
        return null;
    }

    /**
     * Converts an {@link Iterable} to a {@link Collection}, unless it already is one.
     *
     * @param iterable The {@link Iterable} to convert.
     * @return {@code iterable} if it's already a {@link Collection}, or a copy of it as a {@link Collection} otherwise.
     */
    public static <T> Collection<T> toCollection(Iterable<T> iterable) {
        return iterable instanceof Collection<T> collection ? collection : ImmutableList.copyOf(iterable);
    }
}
