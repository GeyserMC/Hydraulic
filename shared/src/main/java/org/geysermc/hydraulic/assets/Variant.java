package org.geysermc.hydraulic.assets;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.minecraft.resources.ResourceLocation;

public record Variant(ResourceLocation model, String x, String y, @JsonProperty("uvlock") boolean uvLock, boolean weight) {
}
