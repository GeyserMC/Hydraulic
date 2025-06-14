package org.geysermc.hydraulic.fabric.test.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.geysermc.hydraulic.fabric.test.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class TagGeneration {
    public static class Blocks extends FabricTagProvider<Block> {
        public Blocks(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, Registries.BLOCK, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                    .add(ModBlocks.GOLDEN_BARREL);

            getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(ModBlocks.GOLDEN_BARREL);
        }
    }

    public static class Items extends FabricTagProvider<Item> {
        public Items(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, Registries.ITEM, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
        }
    }
}
