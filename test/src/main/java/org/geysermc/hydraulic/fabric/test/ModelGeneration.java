package org.geysermc.hydraulic.fabric.test;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ModelGeneration extends FabricModelProvider {
    public ModelGeneration(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.createGenericCube(ModBlocks.GOLDEN_BARREL);
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        for (Item item : ModItems.ITEMS) {
            boolean isHandheld = item.components().has(DataComponents.TOOL) || item.components().has(DataComponents.WEAPON);

            ModelTemplate modelTemplate = new ModelTemplate(
                    Optional.of(ResourceLocation.fromNamespaceAndPath("minecraft", "item/" + (isHandheld ? "handheld" : "genderated"))),
                    Optional.empty()
            );
            itemModelGenerators.generateFlatItem(item, modelTemplate);
        }

        ModelTemplate modelTemplate = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath("minecraft", "item/generated")),
                Optional.empty()
        );
        itemModelGenerators.generateFlatItem(ModBlocks.GOLDEN_BARREL.asItem(), modelTemplate);
    }

    @Override
    public @NotNull String getName() {
        return "Hydraulic Test Mod Model Provider";
    }
}
