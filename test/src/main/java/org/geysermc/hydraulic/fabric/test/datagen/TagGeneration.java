package org.geysermc.hydraulic.fabric.test.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import org.geysermc.hydraulic.fabric.test.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class TagGeneration {
    public static class Blocks extends FabricTagsProvider.BlockTagsProvider {
        public Blocks(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            ResourceKey<Block> goldenBarrel = BuiltInRegistries.BLOCK.getResourceKey(ModBlocks.GOLDEN_BARREL).orElseThrow();

            tag(BlockTags.NEEDS_IRON_TOOL)
                    .add(goldenBarrel);

            tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(goldenBarrel);
        }
    }

    public static class Items extends FabricTagsProvider.ItemTagsProvider {
        public Items(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
        }
    }
}
