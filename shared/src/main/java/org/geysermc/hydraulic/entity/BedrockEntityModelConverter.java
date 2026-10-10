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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

// TODO: Move to pack converter?
public final class BedrockEntityModelConverter {
    private static final String FORMAT_VERSION = "1.12.0";
    private static final float PIVOT_HEIGHT = 24.0f;
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

    private static Geometry geometry(EntityModel model) {
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
        bones(bones, model.parts(), null, 0f, 0f, 0f, new HashSet<>());

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

    private static void bones(List<Bones> out, List<EntityModel.Part> parts, String parent,
                              float parentX, float parentY, float parentZ, Set<String> used) {
        for (EntityModel.Part part : parts) {
            EntityModel.Pose pose = part.pose() == null ? EntityModel.Pose.ZERO : part.pose();

            float x = parentX + pose.x();
            float y = parentY + pose.y();
            float z = parentZ + pose.z();

            String name = uniqueName(part.name(), parent, used);

            Bones bone = new Bones();
            bone.name(name);
            if (parent != null) {
                bone.parent(parent);
            }
            bone.pivot(floats(x, PIVOT_HEIGHT - y, z));

            if (pose.xRot() != 0f || pose.yRot() != 0f || pose.zRot() != 0f) {
                bone.rotation(floats(
                    (float) -Math.toDegrees(pose.xRot()),
                    (float) Math.toDegrees(pose.yRot()),
                    (float) -Math.toDegrees(pose.zRot())
                ));
            }

            List<Cubes> cubes = new ArrayList<>(part.cubes().size());
            for (EntityModel.Cube cube : part.cubes()) {
                cubes.add(cube(cube, x, y, z));
            }
            bone.cubes(cubes);

            out.add(bone);

            bones(out, part.children(), name, x, y, z, used);
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
