package org.geysermc.hydraulic.fabric.test.datagen;

import com.mojang.serialization.Codec;
import net.minecraft.client.data.models.EquipmentAssetProvider;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import org.geysermc.hydraulic.fabric.test.HydraulicTestMod;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class EquipmentGeneration extends EquipmentAssetProvider {
    private final PackOutput.PathProvider pathProvider;

    public EquipmentGeneration(PackOutput packOutput) {
        super(packOutput);
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
    }

    @Override
    public @NotNull CompletableFuture<?> run(CachedOutput cachedOutput) {
        Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> map = new HashMap<>();
        ResourceLocation barrel = ResourceLocation.fromNamespaceAndPath(HydraulicTestMod.MOD_ID, "barrel");
        map.put(
                ResourceKey.create(
                        EquipmentAssets.ROOT_ID,
                        barrel
                ),
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
}
