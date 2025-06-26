package org.geysermc.hydraulic.fabric.test.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.geysermc.hydraulic.fabric.test.HydraulicTestMod;
import org.jetbrains.annotations.NotNull;

public class ModelGeneration extends FabricModelProvider {
    public ModelGeneration(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        for (Block block : BuiltInRegistries.BLOCK.stream().toList()) {
            if (!BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(HydraulicTestMod.MOD_ID)) continue;

            blockModelGenerators.createTrivialCube(block);
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        for (Item item : BuiltInRegistries.ITEM.stream().toList()) {
            if (!BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(HydraulicTestMod.MOD_ID)) continue;

            boolean isHandheld = item.components().has(DataComponents.TOOL) || item.components().has(DataComponents.WEAPON);

            ModelTemplate modelTemplate = isHandheld ? ModelTemplates.FLAT_HANDHELD_ITEM : ModelTemplates.FLAT_ITEM;

            itemModelGenerators.generateFlatItem(item, modelTemplate);
        }
    }

    @Override
    public @NotNull String getName() {
        return "Hydraulic Test Mod Model Provider";
    }
}
