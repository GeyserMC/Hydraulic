package org.geysermc.hydraulic.platform.mod;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * Represents info about a mod.
 *
 * @param id the mod's ID
 * @param version the mod's version
 * @param name the mod's name
 * @param modPath the mod's path
 */
public record ModInfo(
        @NotNull String id,
        @NotNull String version,
        @NotNull String name,
        @NotNull Path modPath,
        @NotNull Path modFile,
        @NotNull String iconPath
) {
}