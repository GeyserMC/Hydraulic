package org.geysermc.hydraulic.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.fabric.platform.HydraulicFabricBootstrap;
import org.geysermc.hydraulic.platform.HydraulicPlatform;

public class HydraulicFabricMod implements ModInitializer {
    private HydraulicImpl hydraulic;

    @Override
    public void onInitialize() {
        this.hydraulic = HydraulicImpl.load(HydraulicPlatform.FABRIC, new HydraulicFabricBootstrap());

        ServerLifecycleEvents.SERVER_STARTING.register(this.hydraulic::onServerStarting);
    }
}
