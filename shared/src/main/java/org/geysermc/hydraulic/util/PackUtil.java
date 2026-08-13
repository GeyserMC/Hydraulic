package org.geysermc.hydraulic.util;

import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import net.kyori.adventure.key.Key;
import org.geysermc.hydraulic.Constants;
import org.geysermc.pack.converter.util.JsonMappings;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Utility class for packs.
 */
public class PackUtil {
    protected static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Bedrock clients on some platforms (e.g. PS4/PS5, older mobile devices) fail to load resource pack files
     * whose path is 80 characters or longer. This shortens a generated pack path by hashing the over-long part
     * while keeping a readable filename tail, guaranteeing the path stays under the given limit.
     *
     * @param path the generated bedrock pack path (e.g. {@code textures/ui/waystones/.../the_nether.png})
     * @param maxLength the maximum allowed length
     * @return the shortened path if it exceeded the limit, the original path otherwise
     */
    public static String limitPathLength(String path, int maxLength) {
        if (path.length() <= maxLength) {
            return path;
        }

        final String prefix = "textures/";
        final String hash = Integer.toHexString(path.hashCode()) + Integer.toHexString((path + "|hydraulic").hashCode());

        String body = path;
        String extension = "";
        int dot = path.lastIndexOf('.');
        if (dot > path.lastIndexOf('/')) {
            extension = path.substring(dot);
            body = path.substring(0, dot);
        }

        String fileName = body.substring(body.lastIndexOf('/') + 1);
        String shortName = fileName.length() > 10 ? fileName.substring(0, 10) : fileName;

        return prefix + hash + "/" + shortName + extension;
    }

    /**
     * Ensures the given mod root is readable as a resource pack:
     * <ul>
     *     <li>some mods (e.g. Lootr) ship assets but no top-level pack.mcmeta, which makes the pack
     *     serializer throw a NullPointerException</li>
     *     <li>some mods (e.g. Lootr) use custom blockstate variant formats without a {@code model}
     *     field, which makes {@code BlockStateSerializer} throw a NullPointerException</li>
     * </ul>
     * For zip roots a temporary copy with a minimal pack.mcmeta injected and offending variants
     * stripped is returned; directory roots are returned unchanged.
     *
     * @param root the mod root (extracted directory or jar file)
     * @return a readable root with a pack.mcmeta present and parseable blockstates
     * @throws IOException if an I/O error occurs
     */
    public static Path ensurePackMeta(Path root) throws IOException {
        if (Files.isDirectory(root)) {
            return root;
        }

        boolean needsRepair = false;
        try (FileSystem fs = FileSystems.newFileSystem(root, (ClassLoader) null)) {
            if (!Files.exists(fs.getPath("/pack.mcmeta"))) {
                needsRepair = true;
            }
            if (!needsRepair) {
                try (Stream<Path> walker = Files.walk(fs.getPath("/assets"))) {
                    needsRepair = walker
                            .filter(p -> {
                                final String s = p.toString();
                                return (s.contains("/blockstates/") || s.contains("/items/")) && s.endsWith(".json");
                            })
                            .anyMatch(p -> {
                                try {
                                    final String content = Files.readString(p, StandardCharsets.UTF_8);
                                    return hasVariantWithoutModel(content) || content.contains("lootr:");
                                } catch (IOException e) {
                                    return false;
                                }
                            });
                }
            }
        } catch (IOException ignored) {
            return root;
        }

        if (!needsRepair) {
            return root;
        }

        LOGGER.warn("Mod jar {} needs pack repair (missing pack.mcmeta or non-standard blockstates), creating a temporary copy", root);

        final Path tmp = Files.createTempFile("hydraulic-packmeta-", ".jar");
        try (ZipFile zip = new ZipFile(root.toFile());
             ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(tmp))) {
            final Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }

                final byte[] data;
                try (InputStream in = zip.getInputStream(entry)) {
                    data = in.readAllBytes();
                }

                final byte[] toWrite;
                if (entry.getName().contains("/blockstates/") && entry.getName().endsWith(".json")) {
                    toWrite = sanitizeBlockstate(data);
                } else if (entry.getName().contains("/items/") && entry.getName().endsWith(".json")
                        && new String(data, StandardCharsets.UTF_8).contains("lootr:")) {
                    toWrite = sanitizeItemModel(data, entry.getName());
                } else {
                    toWrite = data;
                }

                // Skip blockstate files that end up with no variants at all - an empty variants
                // object is rejected by the serializer ("variants and multipart cannot be both empty")
                if (toWrite == null) {
                    continue;
                }

                final ZipEntry copy = new ZipEntry(entry.getName());
                out.putNextEntry(copy);
                out.write(toWrite);
                out.closeEntry();
            }
            out.putNextEntry(new ZipEntry("pack.mcmeta"));
            out.write("{\"pack\":{\"description\":\"hydraulic-injected\",\"pack_format\":48}}".getBytes(StandardCharsets.UTF_8));
            out.closeEntry();
        }
        return tmp;
    }

    /**
     * Removes blockstate variant entries that have no {@code model} field (custom mod formats such
     * as Lootr's {@code "type": "lootr:custom"}) which the pack serializer cannot parse.
     *
     * @param data the raw blockstate JSON
     * @return the sanitized JSON, the original data if nothing needed changing, or {@code null}
     *         if no variants remain and the file should be omitted entirely
     */
    private static byte[] sanitizeBlockstate(byte[] data) {
        try {
            final JsonObject root = JsonParser.parseString(new String(data, StandardCharsets.UTF_8)).getAsJsonObject();
            final JsonElement variants = root.get("variants");
            if (variants == null || !variants.isJsonObject()) {
                return data;
            }

            final JsonObject variantsObject = variants.getAsJsonObject();
            final List<String> toRemove = new ArrayList<>();
            for (final Map.Entry<String, JsonElement> entry : variantsObject.entrySet()) {
                final JsonElement variant = entry.getValue();
                if (variant.isJsonObject() && !variant.getAsJsonObject().has("model")) {
                    toRemove.add(entry.getKey());
                }
            }

            if (toRemove.isEmpty()) {
                return data;
            }

            for (final String key : toRemove) {
                variantsObject.remove(key);
            }

            if (variantsObject.isEmpty()) {
                return null;
            }
            return root.toString().getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            return data;
        }
    }

    private static boolean hasVariantWithoutModel(String json) {
        try {
            final JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            final JsonElement variants = root.get("variants");
            if (variants == null || !variants.isJsonObject()) {
                return false;
            }
            for (final JsonElement variant : variants.getAsJsonObject().entrySet().stream().map(Map.Entry::getValue).toList()) {
                if (variant.isJsonObject() && !variant.getAsJsonObject().has("model")) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Replaces an item model definition that uses custom (non-vanilla) model types or select
     * properties (e.g. Lootr's {@code lootr:chest} or {@code lootr:config_type}) with a simple
     * vanilla model referencing the mod's block model, which the pack serializer can parse.
     *
     * @param data the raw item model JSON
     * @param entryName the zip entry name (e.g. {@code assets/lootr/items/chest.json})
     * @return the vanilla replacement model
     */
    private static byte[] sanitizeItemModel(byte[] data, String entryName) {
        final String[] parts = entryName.split("/");
        String namespace = parts.length > 1 ? parts[1] : "minecraft";
        String name = entryName.substring(entryName.lastIndexOf('/') + 1);
        if (name.endsWith(".json")) {
            name = name.substring(0, name.length() - 5);
        }
        // Lootr's barrel item references block models named barrel_unopened / opened_barrel
        if (name.equals("barrel")) {
            name = "barrel_unopened";
        }
        final String replacement = "{\"model\":{\"type\":\"minecraft:model\",\"model\":\"" + namespace + ":block/" + name + "\"}}";
        LOGGER.warn("Replacing custom item model {} with vanilla fallback {}", entryName, replacement);
        return replacement.getBytes(StandardCharsets.UTF_8);
    }

    public static String getTextureName(@NotNull String modelName) {
        // TODO Sometimes things end up in the minecraft namespace when they shouldn't.
        //      We should look at the current mods resources to see if we find a match there first
        //      EG: betternether:wall_mushroom_red refrencing both mushroom_red_new (its own) and mushroom_block_inside (mc)
        if (modelName.startsWith(Key.MINECRAFT_NAMESPACE)) {
            String modelValue = modelName.split(":")[1];

            // Need to use the Bedrock value for vanilla textures
            JsonMappings mappings = JsonMappings.getMapping("textures");
            if (mappings != null) {
                String output = mappings.map(modelValue).getFirst();

                String value = output.substring(output.indexOf("/") + 1);

                if (modelValue.equals(output)) {
                    return value;
                }

                return Constants.MOD_ID + ":" + value;
            }

            return modelValue.substring(modelValue.indexOf("/") + 1);
        }

        return modelName.replace("block/", "").replace("item/", "");
    }

    public static UUID getModUUID(Collection<Path> modRoots) {
        final HashingOutputStream hos = new HashingOutputStream(Hashing.murmur3_128(), OutputStream.nullOutputStream());
        try (Stream<Path> stream = modRoots.parallelStream()) {
            stream.flatMap(IOUtil.uncheckFunction(Files::walk)).sorted().forEachOrdered(p -> {
                try {
                    hos.write(p.toString().getBytes(StandardCharsets.UTF_8));
                    if (Files.isRegularFile(p)) {
                        Files.copy(p, hos);
                    }
                } catch (IOException e) {
                    LOGGER.warn("Failed to hash {}", p, e);
                }
            });
        }
        return UUID.nameUUIDFromBytes(hos.hash().asBytes());
    }
}
