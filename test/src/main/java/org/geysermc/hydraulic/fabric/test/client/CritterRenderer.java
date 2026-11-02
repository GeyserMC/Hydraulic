package org.geysermc.hydraulic.fabric.test.client;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import org.geysermc.hydraulic.fabric.test.HydraulicTestMod;
import org.geysermc.hydraulic.fabric.test.entity.Critter;

import java.util.function.Function;

public class CritterRenderer<T extends Critter> extends MobRenderer<T, CritterRenderer.State, CritterModel> {
    private static final Identifier BROWN = Identifier.fromNamespaceAndPath(HydraulicTestMod.MOD_ID, "textures/entity/critter/brown.png");
    private static final Identifier GREY = Identifier.fromNamespaceAndPath(HydraulicTestMod.MOD_ID, "textures/entity/critter/grey.png");

    public CritterRenderer(EntityRendererProvider.Context context, Function<ModelPart, CritterModel> model) {
        super(context, model.apply(context.bakeLayer(CritterModel.LAYER)), 0.3F);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(T critter, State state, float partialTick) {
        super.extractRenderState(critter, state, partialTick);
        state.variant = critter.getVariant();
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return switch (state.variant) {
            case 1 -> GREY;
            default -> BROWN;
        };
    }

    public static class State extends LivingEntityRenderState {
        public int variant;
    }
}
