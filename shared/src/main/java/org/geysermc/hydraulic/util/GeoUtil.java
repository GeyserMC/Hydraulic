package org.geysermc.hydraulic.util;

import org.geysermc.hydraulic.Constants;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public class GeoUtil {
    private static final String FORMAT_VERSION = "1.16.0";
    private static final int SCALE = 16;

    private static final String EMPTY_KEY = "empty";

    public static final String EMPTY_GEOMETRY = sharedName(EMPTY_KEY);

    /**
     * Get the geometry name for a fluid level
     *
     * @param level the Java {@code LiquidBlock} level (0-15)
     * @return the geometry name
     */
    @NotNull
    public static String fluidGeometry(int level) {
        return sharedName(fluidKey(level));
    }

    public static void addSharedGeometries(@NotNull BedrockResourcePack pack) {
        addShared(pack, EMPTY_KEY, GeoUtil::empty);

        for (int level = 0; level <= 8; level++) {
            float height = fluidHeight(level);
            addShared(pack, fluidKey(level), name -> fluid(name, height));
        }
    }

    private static void addShared(@NotNull BedrockResourcePack pack, @NotNull String key, @NotNull Function<String, ModelEntity> factory) {
        pack.addBlockModel(factory.apply(sharedName(key)), key + ".json");
    }

    private static String sharedName(String key) {
        return "geometry." + Constants.MOD_ID + "." + key;
    }

    private static String fluidKey(int level) {
        return "fluid_" + Math.min(level, 8);
    }

    private static float fluidHeight(int level) {
        if (level >= 8) {
            return SCALE;
        }

        return (8 - level) / 9f * SCALE;
    }

    /**
     * Create a fluid model entity
     *
     * @param geoName the name of the geometry
     * @param height the height of the fluid in pixels
     * @return the created model entity
     */
    private static ModelEntity fluid(String geoName, float height) {
        ModelEntity modelEntity = empty(geoName);

        Cubes cube = new Cubes();
        cube.origin(new float[] { -SCALE / 2f, 0, -SCALE / 2f });
        cube.size(new float[] { SCALE, height, SCALE });

        Uv uv = new Uv();
        float[] sideUv = new float[] { 0, SCALE - height };
        float[] sideSize = new float[] { SCALE, height };

        North north = new North();
        north.uv(sideUv);
        north.uvSize(sideSize);
        uv.north(north);

        South south = new South();
        south.uv(sideUv);
        south.uvSize(sideSize);
        uv.south(south);

        East east = new East();
        east.uv(sideUv);
        east.uvSize(sideSize);
        uv.east(east);

        West west = new West();
        west.uv(sideUv);
        west.uvSize(sideSize);
        uv.west(west);

        Up up = new Up();
        up.uv(new float[] { 0, 0 });
        up.uvSize(new float[] { SCALE, SCALE });
        uv.up(up);

        Down down = new Down();
        down.uv(new float[] { 0, 0 });
        down.uvSize(new float[] { SCALE, SCALE });
        uv.down(down);

        cube.uv(uv);

        Bones bone = new Bones();
        bone.name("fluid");
        bone.pivot(new float[] { 0, 0, 0 });
        bone.cubes(List.of(cube));

        modelEntity.geometry().getFirst().bones(List.of(bone));

        return modelEntity;
    }

    /**
     * Create an empty model entity
     *
     * @param geoName the name of the geometry
     * @return the created model entity
     */
    private static ModelEntity empty(String geoName) {
        ModelEntity modelEntity = new ModelEntity();
        modelEntity.formatVersion(FORMAT_VERSION);

        Geometry geometry = new Geometry();

        Description description = new Description();
        description.identifier(geoName);
        description.textureWidth(SCALE);
        description.textureHeight(SCALE);
        description.visibleBoundsWidth(2);
        description.visibleBoundsHeight(2);
        description.visibleBoundsOffset(new float[] { 0.0f, 0.25f, 0.0f });
        geometry.description(description);

        modelEntity.geometry(List.of(geometry));

        return modelEntity;
    }
}
