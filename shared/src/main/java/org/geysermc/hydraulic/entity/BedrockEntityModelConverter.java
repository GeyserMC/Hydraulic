package org.geysermc.hydraulic.entity;

import org.geysermc.hydraulic.entity.conduit.EntityModel;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

// TODO: Move to pack converter?
public final class BedrockEntityModelConverter {
    private static final String FORMAT_VERSION = "1.12.0";
    private static final float PIVOT_HEIGHT = 24.0f;

    /**
     * How each rotation axis of a Java model part maps onto Bedrock's. Bedrock's own vanilla models keep
     * Java's signs, such as the cow's body at a pitch of +90 in both and a look animation of
     * {@code query.target_x_rotation} where Java uses the head pitch unchanged.
     */
    public static final float[] ROTATION_SIGNS = {1f, 1f, 1f};
    private static final String DEFAULT_LAYER = "main";

    private BedrockEntityModelConverter() {
    }

    @NotNull
    public static ModelEntity convert(@NotNull List<EntityModel> models) {
        List<Geometry> geometries = new ArrayList<>(models.size());
        for (EntityModel model : models) {
            geometries.add(geometry(model));
        }

        ModelEntity entity = new ModelEntity();
        entity.formatVersion(FORMAT_VERSION);
        entity.geometry(geometries);
        return entity;
    }

    @NotNull
    public static String identifier(@NotNull EntityModel model) {
        StringBuilder identifier = new StringBuilder("geometry.")
            .append(model.namespace()).append('.').append(model.path());

        String layer = model.layer();
        if (layer != null && !layer.isEmpty() && !layer.equals(DEFAULT_LAYER)) {
            identifier.append('.').append(layer);
        }

        return identifier.toString().replace('/', '.');
    }

    private static Geometry geometry(EntityModel unscaled) {
        EntityModel model = scaled(unscaled);
        Description description = new Description();
        description.identifier(identifier(model));
        if (model.textureWidth() > 0) {
            description.textureWidth((float) model.textureWidth());
        }
        if (model.textureHeight() > 0) {
            description.textureHeight((float) model.textureHeight());
        }

        float[] box = { Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE };
        bounds(model.parts(), 0f, 0f, 0f, box);
        if (box[3] >= box[0]) {
            description.visibleBoundsWidth(Math.max(box[3] - box[0], box[5] - box[2]) / 16f);
            description.visibleBoundsHeight((box[4] - box[1]) / 16f);
            description.visibleBoundsOffset(floats((box[0] + box[3]) / 32f, (box[1] + box[4]) / 32f, (box[2] + box[5]) / 32f));
        }

        List<Bones> bones = new ArrayList<>();
        bones(bones, new HashMap<>(), model.parts(), null, List.of(), 0f, 0f, 0f, new HashSet<>());

        Geometry geometry = new Geometry();
        geometry.description(description);
        geometry.bones(bones);
        return geometry;
    }

    private static void bounds(List<EntityModel.Part> parts, float parentX, float parentY, float parentZ, float[] box) {
        for (EntityModel.Part part : parts) {
            EntityModel.Pose pose = part.pose() == null ? EntityModel.Pose.ZERO : part.pose();
            float x = parentX + pose.x();
            float y = parentY + pose.y();
            float z = parentZ + pose.z();

            for (EntityModel.Cube cube : part.cubes()) {
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

            bounds(part.children(), x, y, z, box);
        }
    }

    // Applies the model's scale to every part, as baking the layer would.
    private static EntityModel scaled(EntityModel model) {
        if (model.scale() == 1f) {
            return model;
        }

        List<EntityModel.Part> parts = new ArrayList<>(model.parts().size());
        for (EntityModel.Part part : model.parts()) {
            EntityModel.Pose pose = part.pose() == null ? EntityModel.Pose.ZERO : part.pose();
            EntityModel.Pose root = new EntityModel.Pose(pose.x() * model.scale(), PIVOT_HEIGHT - (PIVOT_HEIGHT - pose.y()) * model.scale(),
                pose.z() * model.scale(), pose.xRot(), pose.yRot(), pose.zRot());
            parts.add(scale(part, root, model.scale()));
        }

        return new EntityModel(model.namespace(), model.path(), model.layer(), model.textureWidth(), model.textureHeight(), parts, 1f);
    }

    private static EntityModel.Part scale(EntityModel.Part part, EntityModel.Pose pose, float scale) {
        List<EntityModel.Cube> cubes = new ArrayList<>(part.cubes().size());
        for (EntityModel.Cube cube : part.cubes()) {
            cubes.add(new EntityModel.Cube(cube.texOffsU(), cube.texOffsV(), cube.x() * scale, cube.y() * scale, cube.z() * scale,
                cube.width() * scale, cube.height() * scale, cube.depth() * scale, cube.inflate() * scale, cube.mirror()));
        }

        List<EntityModel.Part> children = new ArrayList<>(part.children().size());
        for (EntityModel.Part child : part.children()) {
            EntityModel.Pose offset = child.pose() == null ? EntityModel.Pose.ZERO : child.pose();
            children.add(scale(child, new EntityModel.Pose(offset.x() * scale, offset.y() * scale, offset.z() * scale,
                offset.xRot(), offset.yRot(), offset.zRot()), scale));
        }

        return new EntityModel.Part(part.name(), pose, cubes, children);
    }

    /**
     * Gets the name each part of a model is given as a bone, since parts sharing a name are renamed.
     *
     * @param model the model
     * @return the bone name of each part, keyed by the names of the parts leading to it from the root
     */
    @NotNull
    public static Map<List<String>, String> boneNames(@NotNull EntityModel model) {
        Map<List<String>, String> names = new HashMap<>();
        bones(new ArrayList<>(), names, model.parts(), null, List.of(), 0f, 0f, 0f, new HashSet<>());
        return names;
    }

    private static void bones(List<Bones> out, Map<List<String>, String> names, List<EntityModel.Part> parts, String parent,
                              List<String> parentPath, float parentX, float parentY, float parentZ, Set<String> used) {
        for (EntityModel.Part part : parts) {
            EntityModel.Pose pose = part.pose() == null ? EntityModel.Pose.ZERO : part.pose();

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
            bone.pivot(floats(x, PIVOT_HEIGHT - y, z));

            if (pose.xRot() != 0f || pose.yRot() != 0f || pose.zRot() != 0f) {
                bone.rotation(floats(
                    ROTATION_SIGNS[0] * (float) Math.toDegrees(pose.xRot()),
                    ROTATION_SIGNS[1] * (float) Math.toDegrees(pose.yRot()),
                    ROTATION_SIGNS[2] * (float) Math.toDegrees(pose.zRot())
                ));
            }

            List<Cubes> cubes = new ArrayList<>(part.cubes().size());
            for (EntityModel.Cube cube : part.cubes()) {
                cubes.add(cube(cube, x, y, z));
            }
            bone.cubes(cubes);

            out.add(bone);

            bones(out, names, part.children(), name, path, x, y, z, used);
        }
    }

    private static Cubes cube(EntityModel.Cube cube, float partX, float partY, float partZ) {
        float x = partX + cube.x();
        float y = partY + cube.y();
        float z = partZ + cube.z();

        Cubes out = new Cubes();
        out.origin(floats(x, PIVOT_HEIGHT - (y + cube.height()), z));
        out.size(floats(cube.width(), cube.height(), cube.depth()));
        out.uv(uv(cube));

        if (cube.inflate() != 0f) {
            out.inflate(cube.inflate());
        }

        return out;
    }

    private static Uv uv(EntityModel.Cube cube) {
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

        // Java's -Y face is Bedrock's +Y face and vice versa, since the two spaces disagree on Y.
        Up up = new Up();
        face(cube.mirror(), u1, v0, u2, v1, up::uv, up::uvSize);
        uv.up(up);

        Down down = new Down();
        face(cube.mirror(), u2, v1, u3, v0, down::uv, down::uvSize);
        uv.down(down);

        West west = new West();
        face(cube.mirror(), u0, v1, u1, v2, west::uv, west::uvSize);
        uv.west(west);

        North north = new North();
        face(cube.mirror(), u1, v1, u2, v2, north::uv, north::uvSize);
        uv.north(north);

        East east = new East();
        face(cube.mirror(), u2, v1, u4, v2, east::uv, east::uvSize);
        uv.east(east);

        South south = new South();
        face(cube.mirror(), u4, v1, u5, v2, south::uv, south::uvSize);
        uv.south(south);

        return uv;
    }

    private static void face(boolean mirror, float uStart, float vStart, float uEnd, float vEnd,
                             Consumer<float[]> uv, Consumer<float[]> uvSize) {
        if (mirror) {
            uv.accept(floats(uEnd, vStart));
            uvSize.accept(floats(uStart - uEnd, vEnd - vStart));
        } else {
            uv.accept(floats(uStart, vStart));
            uvSize.accept(floats(uEnd - uStart, vEnd - vStart));
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

    private static float[] floats(float... values) {
        // -0.0 == 0.0, so this replaces negative zero, which would otherwise serialize as -0.0.
        for (int i = 0; i < values.length; i++) {
            if (values[i] == 0f) {
                values[i] = 0f;
            }
        }

        return values;
    }
}
