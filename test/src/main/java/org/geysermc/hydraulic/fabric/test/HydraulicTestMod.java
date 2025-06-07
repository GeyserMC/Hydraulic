package org.geysermc.hydraulic.fabric.test;

import net.fabricmc.api.ModInitializer;

public class HydraulicTestMod implements ModInitializer {
    @Override
    public void onInitialize() {
        ModItems.init();
        ModBlocks.init();
    }
}
