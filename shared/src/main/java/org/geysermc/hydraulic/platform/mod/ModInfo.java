package org.geysermc.hydraulic.platform.mod;

import org.jetbrains.annotations.NotNull;

/**
 * Represents info about a mod.
 *
 * @param id the mod's ID
 * @param version the mod's version
 * @param name the mod's name
 */
public record ModInfo(@NotNull String id, @NotNull String version, @NotNull String name) {
}