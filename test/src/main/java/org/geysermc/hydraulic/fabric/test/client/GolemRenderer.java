package org.geysermc.hydraulic.fabric.test.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;
import org.geysermc.hydraulic.fabric.test.HydraulicTestMod;
import org.geysermc.hydraulic.fabric.test.entity.Golem;

public class GolemRenderer extends MobRenderer<Golem, GolemRenderer.State, GolemModel> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(HydraulicTestMod.MOD_ID, "textures/entity/golem.png");

    public GolemRenderer(EntityRendererProvider.Context context) {
        super(context, new GolemModel(context.bakeLayer(GolemModel.LAYER)), 0.5F);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(Golem golem, State state, float partialTick) {
        super.extractRenderState(golem, state, partialTick);
        state.charging = golem.isCharging();
        state.waveAnimationState.copyFrom(golem.waveAnimationState);
    }

    @Override
    public Identifier getTextureLocation(State state) {
        return TEXTURE;
    }

    public static class State extends LivingEntityRenderState {
        public boolean charging;
        public final AnimationState waveAnimationState = new AnimationState();
    }
}
