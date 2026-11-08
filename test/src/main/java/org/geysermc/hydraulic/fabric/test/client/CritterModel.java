package org.geysermc.hydraulic.fabric.test.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.geysermc.hydraulic.fabric.test.HydraulicTestMod;

public class CritterModel extends EntityModel<CritterRenderer.State> {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(HydraulicTestMod.MOD_ID, "critter"), "main");

    private final ModelPart head;
    private final ModelPart[] legs;

    public CritterModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.legs = new ModelPart[4];
        for (int i = 0; i < this.legs.length; i++) {
            this.legs[i] = root.getChild("leg" + i);
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, -4.0F, 6.0F, 4.0F, 8.0F), PartPose.offset(0.0F, 19.0F, 0.0F));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 12).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 3.0F), PartPose.offset(0.0F, 18.0F, -4.0F));
        root.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(14, 12).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offset(-2.0F, 21.0F, -3.0F));
        root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(14, 12).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offset(2.0F, 21.0F, -3.0F));
        root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(14, 12).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offset(-2.0F, 21.0F, 3.0F));
        root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(14, 12).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offset(2.0F, 21.0F, 3.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(CritterRenderer.State state) {
        super.setupAnim(state);
        this.head.xRot = state.xRot * ((float) Math.PI / 180.0F);
        this.head.yRot = state.yRot * ((float) Math.PI / 180.0F);
        for (int i = 0; i < this.legs.length; i++) {
            this.legs[i].xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (i % 2) * (float) Math.PI) * 1.4F * state.walkAnimationSpeed;
        }
    }
}
