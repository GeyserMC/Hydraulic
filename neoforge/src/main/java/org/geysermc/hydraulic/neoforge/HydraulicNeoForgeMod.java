package org.geysermc.hydraulic.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.neoforge.platform.HydraulicNeoForgeBootstrap;
import org.geysermc.hydraulic.platform.HydraulicPlatform;

@Mod(Constants.MOD_ID)
public class HydraulicNeoForgeMod {
    private final HydraulicImpl hydraulic;

    public HydraulicNeoForgeMod(IEventBus modEventBus) {
        this.hydraulic = HydraulicImpl.load(HydraulicPlatform.NEOFORGE, new HydraulicNeoForgeBootstrap());

        modEventBus.addListener(this::onServerStarting);
    }

    private void onServerStarting(ServerStartingEvent event) {
        this.hydraulic.onServerStarting(event.getServer());
    }
}
