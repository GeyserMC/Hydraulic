package org.geysermc.hydraulic.assets;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record ModelOverride(ResourceLocation model, Map<String, Float> predicate) {
}
