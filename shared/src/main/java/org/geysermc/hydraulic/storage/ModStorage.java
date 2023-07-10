package org.geysermc.hydraulic.storage;

import com.mojang.logging.LogUtils;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.block.Materials;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Stores data relevant to a mod.
 */
public class ModStorage {
    private static final Logger LOGGER = LogUtils.getLogger();

    private ModInfo mod;
    private Materials materials = new Materials();

    private ModStorage(@NotNull ModInfo mod) {
        this.mod = mod;
    }

    /**
     * Gets the materials for this mod.
     *
     * @return the materials
     */
    @NotNull
    public Materials materials() {
        return this.materials;
    }

    /**
     * Sets the materials for this mod.
     *
     * @param materials the materials
     */
    public void materials(@NotNull Materials materials) {
        this.materials = materials;
    }

    /**
     * Saves the mod storage.
     */
    public void save() {
        try {
            Path path = storagePath(this.mod);
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path.resolve("materials.json"))) {
                Constants.GSON.toJson(this.materials, writer);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save mod storage for {}", this.mod.id());
        }
    }

    /**
     * Loads the mod storage for the given mod.
     *
     * @param mod the mod
     * @return the mod storage
     */
    public static ModStorage load(@NotNull ModInfo mod) {
        Path path = storagePath(mod);
        if (Files.notExists(path)) {
            return new ModStorage(mod);
        }

        try {
            try (BufferedReader reader = Files.newBufferedReader(path.resolve("materials.json"))) {
                ModStorage storage = Constants.GSON.fromJson(reader, ModStorage.class);
                storage.mod = mod;
                return storage;
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load mod storage for {}", mod.id());
            return new ModStorage(mod);
        }
    }

    private static Path storagePath(@NotNull ModInfo mod) {
        return HydraulicImpl.instance().dataFolder(Constants.MOD_ID)
                .resolve("storage")
                .resolve(mod.id());
    }
}
