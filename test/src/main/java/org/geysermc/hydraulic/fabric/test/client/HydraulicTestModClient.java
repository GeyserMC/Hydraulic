package org.geysermc.hydraulic.fabric.test.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import org.geysermc.hydraulic.fabric.test.ModEntities;

public class HydraulicTestModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelLayerRegistry.registerModelLayer(CritterModel.LAYER, CritterModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(GolemModel.LAYER, GolemModel::createBodyLayer);

        EntityRendererRegistry.register(ModEntities.CRITTER, context -> new CritterRenderer<>(context, CritterModel::new));
        EntityRendererRegistry.register(ModEntities.GOLEM, GolemRenderer::new);
    }
}
