package org.geysermc.hydraulic.fabric.test.client;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import org.geysermc.hydraulic.fabric.test.HydraulicTestMod;

public class GolemModel extends EntityModel<GolemRenderer.State> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(HydraulicTestMod.MOD_ID, "golem"), "main");

    private static final AnimationDefinition WAVE = AnimationDefinition.Builder.withLength(1.0F).looping()
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-150.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
            .build();

    private static final AnimationDefinition WALK = AnimationDefinition.Builder.withLength(1.0F).looping()
            .addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-30.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)))
            .build();

    private final ModelPart antenna;
    private final KeyframeAnimation waveAnimation;
    private final KeyframeAnimation walkAnimation;

    public GolemModel(ModelPart root) {
        super(root);
        this.antenna = root.getChild("body").getChild("antenna");
        this.waveAnimation = WAVE.bake(root);
        this.walkAnimation = WALK.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -12.0F, -3.0F, 8.0F, 12.0F, 6.0F), PartPose.offset(0.0F, 14.0F, 0.0F));
        body.addOrReplaceChild("antenna", CubeListBuilder.create().texOffs(28, 0).addBox(-0.5F, -4.0F, -0.5F, 1.0F, 4.0F, 1.0F), PartPose.offset(0.0F, -12.0F, 0.0F));
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 18).addBox(-3.0F, -1.0F, -1.5F, 3.0F, 10.0F, 3.0F), PartPose.offset(-4.0F, 3.0F, 0.0F));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(0.0F, -1.0F, -1.5F, 3.0F, 10.0F, 3.0F), PartPose.offset(4.0F, 3.0F, 0.0F));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(12, 18).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F), PartPose.offset(-2.0F, 14.0F, 0.0F));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(12, 18).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 10.0F, 3.0F), PartPose.offset(2.0F, 14.0F, 0.0F));
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public void setupAnim(GolemRenderer.State state) {
        super.setupAnim(state);
        this.antenna.visible = !state.charging;
        this.walkAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 2.0F, 2.5F);
        this.waveAnimation.apply(state.waveAnimationState, state.ageInTicks);
    }
}
