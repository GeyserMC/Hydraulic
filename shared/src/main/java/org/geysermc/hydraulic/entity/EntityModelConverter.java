package org.geysermc.hydraulic.entity;

import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.bedrock.resource.models.entity.ModelEntity;
import org.geysermc.pack.bedrock.resource.models.entity.modelentity.Geometry;
import org.geysermc.pack.bedrock.resource.models.entity.modelentity.geometry.Bones;
import org.geysermc.pack.bedrock.resource.models.entity.modelentity.geometry.Description;
import org.geysermc.pack.bedrock.resource.models.entity.modelentity.geometry.bones.Cubes;
import org.geysermc.pack.bedrock.resource.models.entity.modelentity.geometry.bones.cubes.Uv;
import org.geysermc.pack.bedrock.resource.models.entity.modelentity.geometry.bones.cubes.uv.Down;
import org.geysermc.pack.bedrock.resource.models.entity.modelentity.geometry.bones.cubes.uv.East;
import org.geysermc.pack.bedrock.resource.models.entity.modelentity.geometry.bones.cubes.uv.North;
import org.geysermc.pack.bedrock.resource.models.entity.modelentity.geometry.bones.cubes.uv.South;
import org.geysermc.pack.bedrock.resource.models.entity.modelentity.geometry.bones.cubes.uv.Up;
import org.geysermc.pack.bedrock.resource.models.entity.modelentity.geometry.bones.cubes.uv.West;
import org.geysermc.pack.converter.pipeline.AssetCombiner;
import org.geysermc.pack.converter.pipeline.AssetConverter;
import org.geysermc.pack.converter.pipeline.AssetExtractor;
import org.geysermc.pack.converter.pipeline.CombineContext;
import org.geysermc.pack.converter.pipeline.ConversionContext;
import org.geysermc.pack.converter.pipeline.ExtractionContext;
import org.geysermc.pack.converter.type.model.BedrockModel;
import org.geysermc.pack.converter.type.model.ModelConverter;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.base.CubeFace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Converts Java Edition entity models into Bedrock Edition geometry. Java Edition builds these
 * models in code rather than reading them from its resource packs, so they are given to the
 * converter instead of extracted from the pack.
 *
 * <p>
 * This is intentionally not part of the standard pipeline as it requires constructed models
 * that are sourced from the client code, rather than a resource pack or set of files.
 *
 * @param models the models to convert
 */
public record EntityModelConverter(Collection<JavaEntityModel> models) implements AssetExtractor<JavaEntityModel>, AssetConverter<JavaEntityModel, BedrockModel>, AssetCombiner<BedrockModel> {
    private static final String FORMAT_VERSION = "1.12.0";
    private static final String GEOMETRY = "geometry.";
    private static final String DEFAULT_LAYER = "main";
    private static final float PIVOT_HEIGHT = 24.0f;

    @Override
    public Collection<JavaEntityModel> extract(ResourcePack pack, ExtractionContext context) {
        return this.models;
    }

    @Override
    public BedrockModel convert(JavaEntityModel model, ConversionContext context) {
        ModelEntity entity = new ModelEntity();
        entity.formatVersion(FORMAT_VERSION);
        entity.geometry(List.of(createGeometry(model)));
        return new BedrockModel(BedrockModel.ModelType.ENTITY, identifier(model).substring(GEOMETRY.length()) + ".json", entity);
    }

    @Override
    public void include(BedrockResourcePack pack, List<BedrockModel> models, CombineContext context) {
        ModelConverter.INSTANCE.include(pack, models, context);
    }

    /**
     * Gets the name each part of a model is given as a bone, since parts sharing a name are renamed.
     *
     * @param model the model
     * @return the bone name of each part, keyed by the names of the parts leading to it from the root
     */
    @NotNull
    public static Map<List<String>, String> boneNames(@NotNull JavaEntityModel model) {
        Map<List<String>, String> names = new HashMap<>();
        createBones(new ArrayList<>(), names, model.parts(), null, List.of(), 0f, 0f, 0f, new HashSet<>());
        return names;
    }

    /**
     * Gets the identifier of the geometry a model is converted to.
     *
     * @param model the model
     * @return the geometry's identifier, such as {@code geometry.example.critter}
     */
    @NotNull
    static String identifier(@NotNull JavaEntityModel model) {
        StringBuilder identifier = new StringBuilder(GEOMETRY).append(model.namespace()).append('.').append(model.path());
        if (model.layer() != null && !model.layer().isEmpty() && !model.layer().equals(DEFAULT_LAYER)) {
            identifier.append('.').append(model.layer());
        }

        return identifier.toString().replace('/', '.');
    }

    private static Geometry createGeometry(JavaEntityModel unscaled) {
        JavaEntityModel model = scaleModel(unscaled);
        Description description = new Description();
        description.identifier(identifier(model));
        if (model.textureWidth() > 0) {
            description.textureWidth((float) model.textureWidth());
        }

        if (model.textureHeight() > 0) {
            description.textureHeight((float) model.textureHeight());
        }

        float[] box = { Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE };

        expandBounds(model.parts(), 0f, 0f, 0f, box);

        if (box[3] >= box[0]) {
            description.visibleBoundsWidth(Math.max(box[3] - box[0], box[5] - box[2]) / 16f);
            description.visibleBoundsHeight((box[4] - box[1]) / 16f);
            description.visibleBoundsOffset(sanitizeFloats((box[0] + box[3]) / 32f, (box[1] + box[4]) / 32f, (box[2] + box[5]) / 32f));
        }

        List<Bones> bones = new ArrayList<>();
        createBones(bones, new HashMap<>(), model.parts(), null, List.of(), 0f, 0f, 0f, new HashSet<>());

        Geometry geometry = new Geometry();
        geometry.description(description);
        geometry.bones(bones);
        return geometry;
    }

    private static void expandBounds(List<JavaEntityModel.Part> parts, float parentX, float parentY, float parentZ, float[] box) {
        for (JavaEntityModel.Part part : parts) {
            JavaEntityModel.Pose pose = part.pose() == null ? JavaEntityModel.Pose.ZERO : part.pose();
            float x = parentX + pose.x();
            float y = parentY + pose.y();
            float z = parentZ + pose.z();

            for (JavaEntityModel.Cube cube : part.cubes()) {
                float cx = x + cube.x();
                float cy = y + cube.y();
                float cz = z + cube.z();

                box[0] = Math.min(box[0], cx);
                box[1] = Math.min(box[1], PIVOT_HEIGHT - (cy + cube.height()));
                box[2] = Math.min(box[2], cz);
                box[3] = Math.max(box[3], cx + cube.width());
                box[4] = Math.max(box[4], PIVOT_HEIGHT - cy);
                box[5] = Math.max(box[5], cz + cube.depth());
            }

            expandBounds(part.children(), x, y, z, box);
        }
    }

    private static JavaEntityModel scaleModel(JavaEntityModel model) {
        if (model.scale() == 1f) {
            return model;
        }

        List<JavaEntityModel.Part> parts = new ArrayList<>(model.parts().size());
        for (JavaEntityModel.Part part : model.parts()) {
            JavaEntityModel.Pose pose = part.pose() == null ? JavaEntityModel.Pose.ZERO : part.pose();
            JavaEntityModel.Pose root = new JavaEntityModel.Pose(pose.x() * model.scale(), PIVOT_HEIGHT - (PIVOT_HEIGHT - pose.y()) * model.scale(),
                pose.z() * model.scale(), pose.xRot(), pose.yRot(), pose.zRot());
            parts.add(scalePart(part, root, model.scale()));
        }

        return new JavaEntityModel(model.namespace(), model.path(), model.layer(), model.textureWidth(), model.textureHeight(), parts, 1f);
    }

    private static JavaEntityModel.Part scalePart(JavaEntityModel.Part part, JavaEntityModel.Pose pose, float scale) {
        List<JavaEntityModel.Cube> cubes = new ArrayList<>(part.cubes().size());
        for (JavaEntityModel.Cube cube : part.cubes()) {
            cubes.add(new JavaEntityModel.Cube(cube.texOffsU(), cube.texOffsV(), cube.x() * scale, cube.y() * scale, cube.z() * scale,
                cube.width() * scale, cube.height() * scale, cube.depth() * scale, cube.inflate() * scale, cube.mirror()));
        }

        List<JavaEntityModel.Part> children = new ArrayList<>(part.children().size());
        for (JavaEntityModel.Part child : part.children()) {
            JavaEntityModel.Pose offset = child.pose() == null ? JavaEntityModel.Pose.ZERO : child.pose();
            children.add(scalePart(child, new JavaEntityModel.Pose(offset.x() * scale, offset.y() * scale, offset.z() * scale,
                offset.xRot(), offset.yRot(), offset.zRot()), scale));
        }

        return new JavaEntityModel.Part(part.name(), pose, cubes, children);
    }

    private static void createBones(List<Bones> out, Map<List<String>, String> names, List<JavaEntityModel.Part> parts, String parent,
                                   List<String> parentPath, float parentX, float parentY, float parentZ, Set<String> used) {
        for (JavaEntityModel.Part part : parts) {
            JavaEntityModel.Pose pose = part.pose() == null ? JavaEntityModel.Pose.ZERO : part.pose();
            float x = parentX + pose.x();
            float y = parentY + pose.y();
            float z = parentZ + pose.z();

            String name = uniqueName(part.name(), parent, used);
            List<String> path = new ArrayList<>(parentPath);
            path.add(part.name());
            names.put(List.copyOf(path), name);

            Bones bone = new Bones();
            bone.name(name);
            if (parent != null) {
                bone.parent(parent);
            }

            bone.pivot(sanitizeFloats(x, PIVOT_HEIGHT - y, z));

            if (pose.xRot() != 0f || pose.yRot() != 0f || pose.zRot() != 0f) {
                bone.rotation(sanitizeFloats((float) Math.toDegrees(pose.xRot()), (float) Math.toDegrees(pose.yRot()), (float) Math.toDegrees(pose.zRot())));
            }

            List<Cubes> cubes = new ArrayList<>(part.cubes().size());
            for (JavaEntityModel.Cube cube : part.cubes()) {
                cubes.add(createCube(cube, x, y, z));
            }

            bone.cubes(cubes);
            out.add(bone);

            createBones(out, names, part.children(), name, path, x, y, z, used);
        }
    }

    private static Cubes createCube(JavaEntityModel.Cube cube, float partX, float partY, float partZ) {
        float x = partX + cube.x();
        float y = partY + cube.y();
        float z = partZ + cube.z();

        Cubes out = new Cubes();
        out.origin(sanitizeFloats(x, PIVOT_HEIGHT - (y + cube.height()), z));
        out.size(sanitizeFloats(cube.width(), cube.height(), cube.depth()));
        out.uv(uv(cube));
        if (cube.inflate() != 0f) {
            out.inflate(cube.inflate());
        }

        return out;
    }

    private static Uv uv(JavaEntityModel.Cube cube) {
        float u = cube.texOffsU();
        float v = cube.texOffsV();
        float width = cube.width();
        float height = cube.height();
        float depth = cube.depth();

        float u0 = u;
        float u1 = u + depth;
        float u2 = u + depth + width;
        float u3 = u + depth + width + width;
        float u4 = u + depth + width + depth;
        float u5 = u + depth + width + depth + width;

        float v0 = v;
        float v1 = v + depth;
        float v2 = v + depth + height;

        Uv uv = new Uv();

        // Java's -Y face is Bedrock's +Y face and vice versa, since the two spaces disagree on Y
        applyUvFace(uv, CubeFace.UP, cube.mirror(), u1, v0, u2, v1);
        applyUvFace(uv, CubeFace.DOWN, cube.mirror(), u2, v1, u3, v0);
        applyUvFace(uv, CubeFace.WEST, cube.mirror(), u0, v1, u1, v2);
        applyUvFace(uv, CubeFace.NORTH, cube.mirror(), u1, v1, u2, v2);
        applyUvFace(uv, CubeFace.EAST, cube.mirror(), u2, v1, u4, v2);
        applyUvFace(uv, CubeFace.SOUTH, cube.mirror(), u4, v1, u5, v2);
        return uv;
    }

    private static void applyUvFace(Uv uv, CubeFace face, boolean mirror, float uStart, float vStart, float uEnd, float vEnd) {
        float[] start = mirror ? sanitizeFloats(uEnd, vStart) : sanitizeFloats(uStart, vStart);
        float[] size = mirror ? sanitizeFloats(uStart - uEnd, vEnd - vStart) : sanitizeFloats(uEnd - uStart, vEnd - vStart);
        switch (face) {
            case NORTH -> {
                North north = new North();
                north.uv(start);
                north.uvSize(size);
                uv.north(north);
            }
            case SOUTH -> {
                South south = new South();
                south.uv(start);
                south.uvSize(size);
                uv.south(south);
            }
            case EAST -> {
                East east = new East();
                east.uv(start);
                east.uvSize(size);
                uv.east(east);
            }
            case WEST -> {
                West west = new West();
                west.uv(start);
                west.uvSize(size);
                uv.west(west);
            }
            case UP -> {
                Up up = new Up();
                up.uv(start);
                up.uvSize(size);
                uv.up(up);
            }
            case DOWN -> {
                Down down = new Down();
                down.uv(start);
                down.uvSize(size);
                uv.down(down);
            }
        }
    }

    private static String uniqueName(String name, String parent, Set<String> used) {
        String candidate = name == null || name.isEmpty() ? "bone" : name;
        if (used.add(candidate)) {
            return candidate;
        }

        String qualified = parent + "." + candidate;
        if (parent != null && used.add(qualified)) {
            return qualified;
        }

        for (int suffix = 2; ; suffix++) {
            String numbered = candidate + suffix;
            if (used.add(numbered)) {
                return numbered;
            }
        }
    }

    private static float[] sanitizeFloats(float... values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] == 0f) {
                values[i] = 0f;
            }
        }

        return values;
    }
}
