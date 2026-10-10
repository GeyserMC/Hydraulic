package org.geysermc.hydraulic.entity;

import org.geysermc.hydraulic.entity.conduit.CubeDefinition;
import org.geysermc.hydraulic.entity.conduit.CubeDeformation;
import org.geysermc.hydraulic.entity.conduit.EntityModel;
import org.geysermc.hydraulic.entity.conduit.Identifier;
import org.geysermc.hydraulic.entity.conduit.LayerDefinition;
import org.geysermc.hydraulic.entity.conduit.MaterialDefinition;
import org.geysermc.hydraulic.entity.conduit.MeshDefinition;
import org.geysermc.hydraulic.entity.conduit.PartDefinition;
import org.geysermc.hydraulic.entity.conduit.PartPose;
import org.geysermc.hydraulic.entity.conduit.UVPair;
import org.geysermc.hydraulic.entity.conduit.Vector3fc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class EntityModelExtractor {
    private EntityModelExtractor() {
    }

    public static EntityModel extract(Identifier model, String layer, LayerDefinition definition) {
        MaterialDefinition material = definition == null ? null : definition.material();
        MeshDefinition mesh = definition == null ? null : definition.mesh();
        PartDefinition root = mesh == null ? null : mesh.root();

        return new EntityModel(
                model.namespace(),
                model.path(),
                layer,
                material == null ? 0 : integer(material.xTexSize()),
                material == null ? 0 : integer(material.yTexSize()),
                root == null ? List.of() : parts(root.children())
        );
    }

    private static List<EntityModel.Part> parts(Map<String, PartDefinition> children) {
        List<EntityModel.Part> parts = new ArrayList<>(children.size());
        children.forEach((name, child) -> parts.add(new EntityModel.Part(name, pose(child.partPose()), boxes(child.cubes()), parts(child.children()))));
        return parts;
    }

    private static List<EntityModel.Cube> boxes(List<CubeDefinition> cubes) {
        List<EntityModel.Cube> out = new ArrayList<>();
        for (CubeDefinition cube : cubes) {
            UVPair texCoord = cube.texCoord();
            Vector3fc origin = cube.origin();
            Vector3fc dimensions = cube.dimensions();
            CubeDeformation grow = cube.grow();
            out.add(new EntityModel.Cube(
                    texCoord == null ? 0 : Math.round(number(texCoord.u())),
                    texCoord == null ? 0 : Math.round(number(texCoord.v())),
                    origin == null ? 0f : number(origin.x()),
                    origin == null ? 0f : number(origin.y()),
                    origin == null ? 0f : number(origin.z()),
                    dimensions == null ? 0f : number(dimensions.x()),
                    dimensions == null ? 0f : number(dimensions.y()),
                    dimensions == null ? 0f : number(dimensions.z()),
                    grow == null ? 0f : number(grow.growX()),
                    Boolean.TRUE.equals(cube.mirror())
            ));
        }

        return out;
    }

    private static EntityModel.Pose pose(PartPose pose) {
        if (pose == null) {
            return EntityModel.Pose.ZERO;
        }

        return new EntityModel.Pose(
                number(pose.x()), number(pose.y()), number(pose.z()),
                number(pose.xRot()), number(pose.yRot()), number(pose.zRot())
        );
    }

    private static float number(Float value) {
        return value == null ? 0f : value;
    }

    private static int integer(Integer value) {
        return value == null ? 0 : value;
    }
}
