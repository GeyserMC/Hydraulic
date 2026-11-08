package org.geysermc.hydraulic.block;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.base.CubeFace;
import team.unnamed.creative.base.Vector3Float;
import team.unnamed.creative.blockstate.Variant;
import team.unnamed.creative.model.Element;
import team.unnamed.creative.model.ElementFace;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.texture.TextureUV;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Bakes the rotation of {@code uvlock} blockstate variants into a new model.
 * <p>
 * Bedrock has no uvlock, so rotating with the transformation component also rotates
 * the textures. Instead the rotation is applied to the elements and their default UVs
 * are recalculated from the rotated positions, matching what uvlock does on Java.
 */
public final class UvLockBaker {
    private static final Vector3Float BLOCK_CENTER = new Vector3Float(8, 8, 8);
    private static final float UV_TOLERANCE = 0.0001f;

    /**
     * @param variant the variant
     * @return whether the variant is uvlocked and rotated
     */
    public static boolean needsBaking(@NotNull Variant variant) {
        return variant.uvLock() && (Math.floorMod(variant.x(), 360) != 0 || Math.floorMod(variant.y(), 360) != 0);
    }

    /**
     * Elements with their own rotation aren't supported, so those models keep using the transformation.
     *
     * @param model the stitched model
     * @return whether the model can be baked
     */
    public static boolean canBake(@NotNull Model model) {
        return !model.elements().isEmpty() && model.elements().stream().allMatch(element -> element.rotation() == null);
    }

    /**
     * Gets the key of the baked model for the given model and rotation.
     *
     * @param model the original model key
     * @param x the variant X rotation
     * @param y the variant Y rotation
     * @return the key of the baked model
     */
    @NotNull
    public static Key bakedKey(@NotNull Key model, int x, int y) {
        return Key.key(model.namespace(), model.value() + "_uvlock_x" + Math.floorMod(x, 360) + "_y" + Math.floorMod(y, 360));
    }

    /**
     * Bakes the variant rotation into the given model, recalculating default UVs so they stay locked.
     *
     * @param model the stitched model
     * @param x the variant X rotation
     * @param y the variant Y rotation
     * @return the model with the rotation baked in, or null if it can't be baked
     */
    @Nullable
    public static Model bake(@NotNull Model model, int x, int y) {
        if (!canBake(model)) {
            return null;
        }

        int xTurns = Math.floorMod(x / 90, 4);
        int yTurns = Math.floorMod(y / 90, 4);

        List<Element> elements = new ArrayList<>(model.elements().size());
        for (Element element : model.elements()) {
            Vector3Float a = rotate(element.from(), BLOCK_CENTER, xTurns, yTurns);
            Vector3Float b = rotate(element.to(), BLOCK_CENTER, xTurns, yTurns);
            Vector3Float from = new Vector3Float(Math.min(a.x(), b.x()), Math.min(a.y(), b.y()), Math.min(a.z(), b.z()));
            Vector3Float to = new Vector3Float(Math.max(a.x(), b.x()), Math.max(a.y(), b.y()), Math.max(a.z(), b.z()));

            Map<CubeFace, ElementFace> faces = new EnumMap<>(CubeFace.class);
            element.faces().forEach((direction, face) -> {
                CubeFace rotatedDirection = rotate(direction, xTurns, yTurns);
                CubeFace cullFace = face.cullFace() == null ? null : rotate(face.cullFace(), xTurns, yTurns);

                // Custom UVs can't be locked this simply, so only default UVs are recalculated
                boolean lockUv = face.uv() == null || uvEquals(face.uv(), defaultUv(direction, element.from(), element.to()));
                faces.put(rotatedDirection, lockUv
                        ? ElementFace.face(defaultUv(rotatedDirection, from, to), face.texture(), cullFace, 0, face.tintIndex())
                        : ElementFace.face(face.uv(), face.texture(), cullFace, face.rotation(), face.tintIndex()));
            });

            elements.add(element.toBuilder()
                    .from(from)
                    .to(to)
                    .faces(faces)
                    .build());
        }

        return model.toBuilder()
                .key(bakedKey(model.key(), x, y))
                .elements(elements)
                .build();
    }

    // Java applies X then Y. Around the center, X+90 maps (y, z) -> (z, -y) and Y+90 maps (x, z) -> (-z, x)
    private static Vector3Float rotate(Vector3Float vector, Vector3Float center, int xTurns, int yTurns) {
        Vector3Float v = vector.subtract(center);
        for (int i = 0; i < xTurns; i++) {
            v = new Vector3Float(v.x(), v.z(), -v.y());
        }

        for (int i = 0; i < yTurns; i++) {
            v = new Vector3Float(-v.z(), v.y(), v.x());
        }

        return v.add(center);
    }

    private static CubeFace rotate(CubeFace face, int xTurns, int yTurns) {
        Vector3Float direction = rotate(Vector3Float.ZERO.with(face.axis(), face.factor()), Vector3Float.ZERO, xTurns, yTurns);
        for (CubeFace candidate : CubeFace.values()) {
            if (direction.get(candidate.axis()) == candidate.factor()) {
                return candidate;
            }
        }

        throw new IllegalStateException("Could not rotate face " + face);
    }

    // Minecraft's default UVs for a face, in the 0-1 range used by creative
    private static TextureUV defaultUv(CubeFace face, Vector3Float from, Vector3Float to) {
        Vector3Float f = from.divide(16);
        Vector3Float t = to.divide(16);
        return switch (face) {
            case WEST -> TextureUV.uv(f.z(), 1 - t.y(), t.z(), 1 - f.y());
            case EAST -> TextureUV.uv(1 - t.z(), 1 - t.y(), 1 - f.z(), 1 - f.y());
            case DOWN -> TextureUV.uv(f.x(), 1 - t.z(), t.x(), 1 - f.z());
            case UP -> TextureUV.uv(f.x(), f.z(), t.x(), t.z());
            case NORTH -> TextureUV.uv(1 - t.x(), 1 - t.y(), 1 - f.x(), 1 - f.y());
            case SOUTH -> TextureUV.uv(f.x(), 1 - t.y(), t.x(), 1 - f.y());
        };
    }

    private static boolean uvEquals(TextureUV a, TextureUV b) {
        return Math.abs(a.from().x() - b.from().x()) < UV_TOLERANCE
                && Math.abs(a.from().y() - b.from().y()) < UV_TOLERANCE
                && Math.abs(a.to().x() - b.to().x()) < UV_TOLERANCE
                && Math.abs(a.to().y() - b.to().y()) < UV_TOLERANCE;
    }
}
