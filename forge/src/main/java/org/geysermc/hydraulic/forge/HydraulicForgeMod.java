package org.geysermc.hydraulic.forge;

import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.forge.platform.HydraulicForgeBootstrap;
import org.geysermc.hydraulic.platform.HydraulicPlatform;

@Mod(Constants.MOD_ID)
public class HydraulicForgeMod {
    private final HydraulicImpl hydraulic;

    public HydraulicForgeMod() {
        this.hydraulic = HydraulicImpl.load(HydraulicPlatform.FORGE, new HydraulicForgeBootstrap());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onServerStarted);
    }

    private void onServerStarted(ServerStartingEvent event) {
        this.hydraulic.onServerStarting(event.getServer());
    }
}
