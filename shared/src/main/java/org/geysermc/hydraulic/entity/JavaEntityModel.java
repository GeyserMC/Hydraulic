package org.geysermc.hydraulic.entity;

import java.util.List;

/**
 * An entity model as Java Edition builds it in code from a {@code LayerDefinition} of parts and cubes.
 *
 * @param namespace the namespace of the model's layer location
 * @param path the path of the model's layer location
 * @param layer the layer's name, such as {@code main}
 * @param textureWidth the width of the texture the model is drawn with
 * @param textureHeight the height of the texture the model is drawn with
 * @param parts the root's parts
 * @param scale how much the mesh is scaled when baked, about the point the model stands on
 */
public record JavaEntityModel(String namespace, String path, String layer, int textureWidth, int textureHeight, List<Part> parts, float scale) {

    /**
     * A part of a model, as a part definition adds it.
     *
     * @param name the part's name, unique among its siblings
     * @param pose where the part sits and how it is rotated, relative to its parent
     * @param cubes the part's cubes
     * @param children the part's children
     */
    public record Part(String name, Pose pose, List<Cube> cubes, List<Part> children) {
    }

    /**
     * A part's pose, as a part pose defines it.
     *
     * @param x the offset along x
     * @param y the offset along y, downwards
     * @param z the offset along z
     * @param xRot the rotation around x, in radians
     * @param yRot the rotation around y, in radians
     * @param zRot the rotation around z, in radians
     */
    public record Pose(float x, float y, float z, float xRot, float yRot, float zRot) {
        public static final Pose ZERO = new Pose(0, 0, 0, 0, 0, 0);
    }

    /**
     * A cube of a part, as a cube definition defines it.
     *
     * @param texOffsU the u of the cube's texture offset
     * @param texOffsV the v of the cube's texture offset
     * @param x the x of the cube's origin, relative to its part
     * @param y the y of the cube's origin, relative to its part
     * @param z the z of the cube's origin, relative to its part
     * @param width the size along x
     * @param height the size along y
     * @param depth the size along z
     * @param inflate how much the cube grows on each side
     * @param mirror whether the cube's texture is mirrored
     */
    public record Cube(int texOffsU, int texOffsV, float x, float y, float z, float width, float height, float depth, float inflate, boolean mirror) {
    }
}
