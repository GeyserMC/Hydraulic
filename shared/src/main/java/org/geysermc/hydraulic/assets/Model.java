package org.geysermc.hydraulic.assets;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public record Model(ResourceLocation parent, Map<String, ResourceLocation> textures, List<ModelOverride> overrides) {
}
