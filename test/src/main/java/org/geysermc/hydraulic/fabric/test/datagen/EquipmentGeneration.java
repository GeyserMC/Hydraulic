package org.geysermc.hydraulic.fabric.test.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.EquipmentAsset;
import org.geysermc.hydraulic.fabric.test.HydraulicTestMod;
import org.geysermc.hydraulic.fabric.test.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class EquipmentGeneration implements DataProvider {
    private final PackOutput.PathProvider pathProvider;

    public EquipmentGeneration(FabricDataOutput packOutput) {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
    }

    @Override
    public @NotNull CompletableFuture<?> run(CachedOutput cachedOutput) {
        Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> map = new HashMap<>();
        ResourceLocation barrel = ResourceLocation.fromNamespaceAndPath(HydraulicTestMod.MOD_ID, "barrel");
        map.put(
                ModItems.BARREL_ARMOR_MATERIAL_KEY,
                EquipmentClientInfo.builder()
                        .addHumanoidLayers(barrel)
                        .addLayers(
                                EquipmentClientInfo.LayerType.HORSE_BODY,
                                EquipmentClientInfo.Layer.leatherDyeable(barrel, false)
                        )
                        .build()
        );

        return DataProvider.saveAll(cachedOutput, EquipmentClientInfo.CODEC, this.pathProvider::json, map);
    }

    @Override
    public @NotNull String getName() {
        return "Hydraulic Equipment Generator";
    }
}
