package org.geysermc.hydraulic.block;

import com.google.auto.service.AutoService;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.context.PackCreateContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@AutoService(PackModule.class)
public class BlockPackModule extends PackModule<BlockPackModule> {

    @Override
    public void create(@NotNull PackCreateContext<BlockPackModule> context) {
        List<Block> blocks = context.registryValues(Registries.BLOCK);
        if (blocks.size() >= 1) {
            LOGGER.info("Blocks to convert: " + blocks.size() + " in mod " + context.mod().id());
        }
    }

    @Override
    public boolean test(@NotNull PackCreateContext<BlockPackModule> context) {
        return context.registryValues(Registries.BLOCK).size() > 0;
    }
}
