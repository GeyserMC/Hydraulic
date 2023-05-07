package org.geysermc.hydraulic.platform.mod;

/**
 * Represents info about a mod.
 *
 * @param id the mod's ID
 * @param version the mod's version
 * @param name the mod's name
 */
public record ModInfo(String id, String version, String name) {
}