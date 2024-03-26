package org.geysermc.hydraulic.fabric;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.GsonHelper;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class FabricUtil {
    public static final int ICON_SIZE = 256;

    public static Stream<ModInfo> resolveJiJ(Collection<Path> roots) {
        return Stream.of(roots).mapMulti(FabricUtil::resolveJiJ);
    }

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
    public static ModInfo createModInfo(JsonObject metadata, Collection<Path> roots, int schemaVersion) {
        final JsonElement custom = metadata.get("custom");
        if (custom instanceof JsonObject object && new JsonPrimitive(true).equals(object.get("fabric-loom:generated"))) {
            return null;
        }

        final JsonElement id = metadata.get("id");
        final JsonElement version = metadata.get("version");
        if (!(id instanceof JsonPrimitive) || !(version instanceof JsonPrimitive)) {
            return null;
        }

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

    public static <T> Collection<T> toCollection(Iterable<T> iterable) {
        return iterable instanceof Collection<T> collection ? collection : ImmutableList.copyOf(iterable);
    }
}
