package org.geysermc.hydraulic.fabric.test.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGeneration implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModelGeneration::new);
        pack.addProvider(LanguageGeneration::new);
        pack.addProvider(TagGeneration.Blocks::new);
        pack.addProvider(TagGeneration.Items::new);
        pack.addProvider(EquipmentGeneration::new);
    }
}
