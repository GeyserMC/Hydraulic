package org.geysermc.hydraulic.forge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.forge.platform.HydraulicForgeBootstrap;
import org.geysermc.hydraulic.platform.HydraulicPlatform;

@Mod(Constants.MOD_ID)
public class HydraulicForgeMod {
    private final HydraulicImpl hydraulic;

    public HydraulicForgeMod(IEventBus modEventBus) {
        this.hydraulic = HydraulicImpl.load(HydraulicPlatform.NEOFORGE, new HydraulicForgeBootstrap());

        modEventBus.addListener(this::onServerStarting);
    }

    private void onServerStarting(ServerStartingEvent event) {
        this.hydraulic.onServerStarting(event.getServer());
    }
}
