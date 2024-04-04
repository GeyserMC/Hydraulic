package org.geysermc.hydraulic.util;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.geysermc.pack.bedrock.resource.models.entity.ModelEntity;
import org.geysermc.pack.bedrock.resource.models.entity.modelentity.Geometry;
import org.geysermc.pack.bedrock.resource.models.entity.modelentity.geometry.Bones;
import org.geysermc.pack.bedrock.resource.models.entity.modelentity.geometry.Description;
import org.geysermc.pack.bedrock.resource.models.entity.modelentity.geometry.bones.Cubes;

import java.util.ArrayList;
import java.util.List;

public class GeoUtil {
    private static final String FORMAT_VERSION = "1.16.0";
    private static final float[] ELEMENT_OFFSET = new float[] { 8, 0, 8 };
    private static final int SCALE = 16;

    /**
     * Create a model entity from a voxel shape
     *
     * @param shape the voxel shape
     * @param geoName the name of the geometry
     * @return the created model entity
     */
    public static ModelEntity fromShape(VoxelShape shape, String geoName) {
        ModelEntity modelEntity = new ModelEntity();
        modelEntity.formatVersion(FORMAT_VERSION);

        Geometry geometry = new Geometry();

        Description description = new Description();
        description.identifier(geoName);
        description.textureWidth(16);
        description.textureHeight(16);
        description.visibleBoundsWidth(2);
        description.visibleBoundsHeight(2);
        description.visibleBoundsOffset(new float[] { 0.0f, 0.25f, 0.0f });
        geometry.description(description);

        List<Bones> bones = new ArrayList<>();

        for (AABB box : shape.toAabbs()) {
            float[] from = new float[] { (float) box.minX * SCALE, (float) box.minY * SCALE, (float) box.minZ * SCALE };
            float[] to = new float[] { (float) box.maxX * SCALE, (float) box.maxY * SCALE, (float) box.maxZ * SCALE };

            Bones bone = new Bones();
            bone.name("bone_" + bones.size());
            bone.pivot(new float[] { ELEMENT_OFFSET[0], ELEMENT_OFFSET[1], -ELEMENT_OFFSET[2] });

            Cubes cube = new Cubes();
            cube.origin(new float[] { ELEMENT_OFFSET[0] - to[0], from[1], from[2] - ELEMENT_OFFSET[2] });
            cube.size(new float[] { to[0] - from[0], to[1] - from[1], to[2] - from[2] });

            bone.cubes(List.of(cube));
            bones.add(bone);
        }

        geometry.bones(bones);

        modelEntity.geometry(List.of(geometry));

        return modelEntity;
    }

    /**
     * Create an empty model entity
     *
     * @param geoName the name of the geometry
     * @return the created model entity
     */
    public static ModelEntity empty(String geoName) {
        ModelEntity modelEntity = new ModelEntity();
        modelEntity.formatVersion(FORMAT_VERSION);

        Geometry geometry = new Geometry();

        Description description = new Description();
        description.identifier(geoName);
        description.textureWidth(16);
        description.textureHeight(16);
        description.visibleBoundsWidth(2);
        description.visibleBoundsHeight(2);
        description.visibleBoundsOffset(new float[] { 0.0f, 0.25f, 0.0f });
        geometry.description(description);

        modelEntity.geometry(List.of(geometry));

        return modelEntity;
    }
}
