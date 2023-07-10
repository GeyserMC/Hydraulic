package org.geysermc.hydraulic.block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents data relating to Materials.
 */
public class Materials {
    private final Map<String, Material> materials = new HashMap<>();

    /**
     * Add a material to the map.
     *
     * @param identifier the identifier of the material
     * @param material the material
     */
    public void addMaterial(@NotNull String identifier, @NotNull Material material) {
        this.materials.put(identifier, material);
    }

    /**
     * Get a material by its identifier.
     *
     * @param identifier the identifier of the material
     * @return the material
     */
    @Nullable
    public Material material(@NotNull String identifier) {
        return this.materials.get(identifier);
    }

    /**
     * Gets all the materials.
     *
     * @return all the materials
     */
    @NotNull
    public Map<String, Material> materials() {
        return Collections.unmodifiableMap(this.materials);
    }

    public static class Material {
        private final Map<String, String> textures;

        public Material(@NotNull Map<String, String> textures) {
            this.textures = textures;
        }

        @NotNull
        public Map<String, String> textures() {
            return this.textures;
        }
    }
}
