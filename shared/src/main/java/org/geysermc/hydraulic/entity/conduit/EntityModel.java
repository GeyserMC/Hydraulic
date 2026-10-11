package org.geysermc.hydraulic.entity.conduit;

import java.util.List;

/**
 * A model layer as a mod's code builds it.
 *
 * @param scale how much the layer's mesh is scaled when baked, about the point the model stands on
 */
public record EntityModel(String namespace, String path, String layer,
                          int textureWidth, int textureHeight,
                          List<Part> parts, float scale
) {
    public record Part(String name, Pose pose, List<Cube> cubes, List<Part> children) {
    }

    public record Pose(float x, float y, float z, float xRot, float yRot, float zRot) {
        public static final Pose ZERO = new Pose(0, 0, 0, 0, 0, 0);
    }

    public record Cube(int texOffsU, int texOffsV, float x, float y, float z,
                       float width, float height, float depth, float inflate, boolean mirror
    ) {
    }
}
