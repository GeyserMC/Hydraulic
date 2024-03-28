package org.geysermc.hydraulic.block;

import org.geysermc.pack.converter.converter.model.ModelStitcher;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.blockstate.BlockState;

public record StateDefinition(
        @NotNull BlockState state,
        @NotNull ModelStitcher.Provider modelProvider
) {
}