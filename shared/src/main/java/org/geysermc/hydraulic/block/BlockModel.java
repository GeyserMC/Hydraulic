package org.geysermc.hydraulic.block;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record BlockModel(ResourceLocation parent, Map<String, ResourceLocation> textures) {
}
