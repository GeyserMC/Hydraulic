package org.geysermc.hydraulic.fabric.test.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.geysermc.hydraulic.fabric.test.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class TagGeneration {
    public static class Blocks extends FabricTagProvider.BlockTagProvider {
        public Blocks(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            valueLookupBuilder(BlockTags.NEEDS_IRON_TOOL)
                    .add(ModBlocks.GOLDEN_BARREL);

            valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(ModBlocks.GOLDEN_BARREL);
        }
    }

    public static class Items extends FabricTagProvider.ItemTagProvider {
        public Items(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
        }
    }
}
