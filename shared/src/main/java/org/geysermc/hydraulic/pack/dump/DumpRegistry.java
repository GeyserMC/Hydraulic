package org.geysermc.hydraulic.pack.dump;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.geysermc.geyser.api.block.custom.CustomBlockData;
import org.geysermc.geyser.api.item.custom.v2.NonVanillaCustomItemDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Collects the custom items/blocks Hydraulic registers with Geyser and can write them out as JSON
 * (plus the converted Bedrock packs) so a Geyser-Standalone extension can re-register them without
 * the server-side mod. Enable with the system property {@code hydraulic.dumpDir} or the environment
 * variable {@code HYDRAULIC_DUMP_DIR}.
 */
public class DumpRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(DumpRegistry.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final List<DumpModels.DumpItem> items = new ArrayList<>();
    private final List<DumpModels.DumpBlock> blocks = new ArrayList<>();

    public void addItem(NonVanillaCustomItemDefinition definition) {
        synchronized (items) {
            items.add(DumpModels.DumpItem.of(definition));
        }
    }

    public void addBlock(CustomBlockData data) {
        synchronized (blocks) {
            blocks.add(DumpModels.DumpBlock.of(data));
        }
    }

    /**
     * Writes items.json, blocks.json and copies the given converted pack directories into the dump
     * directory (each zipped as packs/&lt;name&gt;.zip).
     */
    public void write(Path dumpDir, List<PackEntry> packs) {
        try {
            Files.createDirectories(dumpDir);
            Files.createDirectories(dumpDir.resolve("packs"));

            synchronized (items) {
                Files.writeString(dumpDir.resolve("items.json"), GSON.toJson(items));
                LOGGER.info("Dumped {} custom items to {}", items.size(), dumpDir.resolve("items.json"));
            }
            synchronized (blocks) {
                Files.writeString(dumpDir.resolve("blocks.json"), GSON.toJson(blocks));
                LOGGER.info("Dumped {} custom blocks to {}", blocks.size(), dumpDir.resolve("blocks.json"));
            }

            for (PackEntry pack : packs) {
                Path target = dumpDir.resolve("packs").resolve(pack.name() + ".zip");
                if (Files.isRegularFile(pack.path())) {
                    // Converted packs are already zipped (.mcpack); just copy them
                    Files.copy(pack.path(), target, StandardCopyOption.REPLACE_EXISTING);
                } else if (Files.isDirectory(pack.path())) {
                    zipDirectory(pack.path(), target);
                } else {
                    LOGGER.warn("Skipping dump for pack {}: {} does not exist", pack.name(), pack.path());
                    continue;
                }
                LOGGER.info("Dumped pack {} to {}", pack.name(), target);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to write dump to {}", dumpDir, e);
        }
    }

    private static void zipDirectory(Path sourceDir, Path targetZip) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(targetZip))) {
            try (var walker = Files.walk(sourceDir)) {
                for (Path file : walker.filter(Files::isRegularFile).toList()) {
                    String rel = sourceDir.relativize(file).toString().replace('\\', '/');
                    ZipEntry entry = new ZipEntry(rel);
                    zos.putNextEntry(entry);
                    try (InputStream in = Files.newInputStream(file)) {
                        in.transferTo(zos);
                    }
                    zos.closeEntry();
                }
            }
        }
    }

    /**
     * Resolves the dump directory from the system property or environment variable, or null when
     * dumping is disabled.
     */
    public static Path configuredDir() {
        String dir = System.getProperty("hydraulic.dumpDir");
        if (dir == null || dir.isBlank()) {
            dir = System.getenv("HYDRAULIC_DUMP_DIR");
        }
        return dir != null && !dir.isBlank() ? Path.of(dir) : null;
    }

    public record PackEntry(String name, Path path) {
    }
}
