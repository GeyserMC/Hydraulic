package org.geysermc.hydraulic.block;

import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.blockstate.BlockState;

public record StateDefinition(
        @NotNull BlockState state,
        @NotNull ResourcePack pack
) {
}