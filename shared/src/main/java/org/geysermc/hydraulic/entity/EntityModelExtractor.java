package org.geysermc.hydraulic.entity;

import org.geysermc.conduit.Trace;
import org.geysermc.conduit.Value;
import org.geysermc.hydraulic.entity.conduit.CubeDefinition;
import org.geysermc.hydraulic.entity.conduit.CubeDeformation;
import org.geysermc.hydraulic.entity.conduit.LayerDefinition;
import org.geysermc.hydraulic.entity.conduit.MaterialDefinition;
import org.geysermc.hydraulic.entity.conduit.MeshDefinition;
import org.geysermc.hydraulic.entity.conduit.PartDefinition;
import org.geysermc.hydraulic.entity.conduit.PartPose;
import org.geysermc.hydraulic.entity.conduit.UVPair;
import org.geysermc.hydraulic.entity.conduit.Vector3fc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class EntityModelExtractor {
    private static final String APPLY = "apply";
    private static final String SCALING = "scaling";

    private EntityModelExtractor() {
    }

    /**
     * Strips mesh transformers off a layer definition, such as
     * {@code LayerDefinition.create(...).apply(MeshTransformer.scaling(0.8F))}, keeping the scale they apply.
     *
     * @param definition the layer definition as built
     * @param unsupported told about each transformer that isn't a plain scale, which is ignored
     * @return the definition without its transformers, and the scale
     */
    @NotNull
    public static Layer unwrap(@Nullable Value definition, @NotNull Consumer<Value> unsupported) {
        float scale = 1f;
        while (definition instanceof Value.InstanceCall(String _, String name, Value receiver, List<Value> args)
            && name.equals(APPLY) && args.size() == 1) {
            if (args.getFirst() instanceof Value.StaticCall(String _, String factory, List<Value> factoryArgs)
                && factory.equals(SCALING) && factoryArgs.size() == 1 && factoryArgs.getFirst() instanceof Value.Literal(Number factor)) {
                scale *= factor.floatValue();
            } else {
                unsupported.accept(args.getFirst());
            }
            definition = receiver;
        }
        return new Layer(definition, scale);
    }

    /**
     * Extracts a layer's geometry.
     *
     * @param namespace the namespace of the layer's model location
     * @param path the path of the layer's model location
     * @param layer the layer name
     * @param unwrapped the layer definition, without its transformers
     * @param trace the trace that built it, for the calls made building its parts
     * @return the model
     */
    @NotNull
    public static JavaEntityModel extract(@NotNull String namespace, @NotNull String path, @Nullable String layer,
                                      @NotNull Layer unwrapped, @NotNull Trace trace) {
        LayerDefinition definition = unwrapped.definition() == null
            ? null
            : new Trace(unwrapped.definition(), trace.calls(), trace.loader()).as(LayerDefinition.class);
        MaterialDefinition material = definition == null ? null : definition.material();
        MeshDefinition mesh = definition == null ? null : definition.mesh();
        PartDefinition root = mesh == null ? null : mesh.root();
        float scale = unwrapped.scale();

        return new JavaEntityModel(
                namespace,
                path,
                layer,
                material == null ? 0 : integer(material.xTexSize()),
                material == null ? 0 : integer(material.yTexSize()),
                root == null ? List.of() : parts(root.children()),
                scale
        );
    }

    private static List<JavaEntityModel.Part> parts(Map<String, PartDefinition> children) {
        List<JavaEntityModel.Part> parts = new ArrayList<>(children.size());
        children.forEach((name, child) -> parts.add(new JavaEntityModel.Part(name, pose(child.partPose()), boxes(child.cubes()), parts(child.children()))));
        return parts;
    }

    private static List<JavaEntityModel.Cube> boxes(List<CubeDefinition> cubes) {
        List<JavaEntityModel.Cube> out = new ArrayList<>();
        for (CubeDefinition cube : cubes) {
            UVPair texCoord = cube.texCoord();
            Vector3fc origin = cube.origin();
            Vector3fc dimensions = cube.dimensions();
            CubeDeformation grow = cube.grow();
            out.add(new JavaEntityModel.Cube(
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

    private static JavaEntityModel.Pose pose(PartPose pose) {
        if (pose == null) {
            return JavaEntityModel.Pose.ZERO;
        }

        return new JavaEntityModel.Pose(
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

    /**
     * A layer definition with its mesh transformers stripped off.
     *
     * @param definition the definition as created, or null if there is none
     * @param scale the scale the transformers applied
     */
    public record Layer(@Nullable Value definition, float scale) {
    }
}
