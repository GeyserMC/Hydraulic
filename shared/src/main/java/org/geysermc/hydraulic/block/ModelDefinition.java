package org.geysermc.hydraulic.block;

import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.blockstate.Variant;
import team.unnamed.creative.model.Model;

public record ModelDefinition(
        @NotNull Model model,
        @NotNull Variant variant
) {
}
