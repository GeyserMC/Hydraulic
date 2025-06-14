package org.geysermc.hydraulic.fabric.test.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.geysermc.hydraulic.fabric.test.ModBlocks;
import org.geysermc.hydraulic.fabric.test.ModItems;

import java.util.concurrent.CompletableFuture;

public class LanguageGeneration extends FabricLanguageProvider {
    protected LanguageGeneration(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {
        translationBuilder.add(ModItems.BARREL_SWORD, "Barrel Sword");
        translationBuilder.add(ModItems.BARREL_PICKAXE, "Barrel Pickaxe");
        translationBuilder.add(ModItems.BARREL_AXE, "Barrel Axe");
        translationBuilder.add(ModItems.BARREL_SHOVEL, "Barrel Shovel");
        translationBuilder.add(ModItems.BARREL_HOE, "Barrel Hoe");
        translationBuilder.add(ModItems.BARREL_HELMET, "Barrel Helmet");
        translationBuilder.add(ModItems.BARREL_CHESTPLATE, "Barrel Chestplate");
        translationBuilder.add(ModItems.BARREL_LEGGINGS, "Barrel Leggings");
        translationBuilder.add(ModItems.BARREL_BOOTS, "Barrel Boots");
        translationBuilder.add(ModItems.BARREL_HORSE_ARMOR, "Barrel Horse Armor");
        translationBuilder.add(ModItems.BARREL_STICK, "Barrel Stick");
        translationBuilder.add(ModItems.BARREL_PACK, "Barrel Pack");

        translationBuilder.add(ModBlocks.GOLDEN_BARREL, "Golden Barrel");
    }
}
