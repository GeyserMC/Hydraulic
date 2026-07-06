package org.geysermc.hydraulic.fabric.test.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import org.geysermc.hydraulic.fabric.test.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class TagGeneration {
    public static class Blocks extends FabricTagsProvider.BlockTagsProvider {
        public Blocks(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(BlockTags.NEEDS_IRON_TOOL)
                    .add(BuiltInRegistries.BLOCK.getResourceKey(ModBlocks.GOLDEN_BARREL).get());

            tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(BuiltInRegistries.BLOCK.getResourceKey(ModBlocks.GOLDEN_BARREL).get());
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
