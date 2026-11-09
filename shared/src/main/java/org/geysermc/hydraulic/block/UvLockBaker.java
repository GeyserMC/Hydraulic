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
 * Bedrock has no uvlock, so rotating with the transformation component would also rotate
 * the textures. Instead the elements are rotated and their default UVs are recalculated
 * from the rotated bounds, which matches what uvlock does on Java.
 */
public final class UvLockBaker {
    private static final Vector3Float BLOCK_CENTER = new Vector3Float(8, 8, 8);
    private static final float UV_TOLERANCE = 0.0001f;

    /**
     * Checks whether a variant needs a baked model.
     *
     * @param variant the variant
     * @return whether the variant is both uvlocked and rotated
     */
    public static boolean needsBaking(@NotNull Variant variant) {
        return variant.uvLock() && (Math.floorMod(variant.x(), 360) != 0 || Math.floorMod(variant.y(), 360) != 0);
    }

    /**
     * Checks whether a model can be baked. Models with rotated elements aren't
     * supported, so they keep using the transformation component instead.
     *
     * @param model the stitched model
     * @return whether the model can be baked
     */
    public static boolean canBake(@NotNull Model model) {
        return !model.elements().isEmpty() && model.elements().stream().allMatch(element -> element.rotation() == null);
    }

    /**
     * Gets the key of the baked model for a model and rotation.
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
     * Bakes a variant rotation into a model, recalculating default UVs so the textures stay locked.
     *
     * @param model the stitched model
     * @param x the variant X rotation
     * @param y the variant Y rotation
     * @return the baked model, or null if the model can't be baked
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
            for (Map.Entry<CubeFace, ElementFace> entry : element.faces().entrySet()) {
                CubeFace direction = entry.getKey();
                ElementFace face = entry.getValue();
                CubeFace rotatedDirection = rotate(direction, xTurns, yTurns);
                CubeFace cullFace = face.cullFace() == null ? null : rotate(face.cullFace(), xTurns, yTurns);

                // Only default UVs are locked, custom UVs are kept as they are
                boolean defaultUv = face.uv() == null || uvEquals(face.uv(), DefaultUvs.defaultUv(direction, element.from(), element.to()));
                if (defaultUv) {
                    faces.put(rotatedDirection, ElementFace.face(DefaultUvs.defaultUv(rotatedDirection, from, to), face.texture(), cullFace, 0, face.tintIndex()));
                } else {
                    faces.put(rotatedDirection, ElementFace.face(face.uv(), face.texture(), cullFace, face.rotation(), face.tintIndex()));
                }
            }

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

    /**
     * Rotates a point around a center the same way Java does: X first, then Y.
     * A quarter turn on X maps (y, z) to (z, -y), and a quarter turn on Y maps (x, z) to (-z, x).
     */
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

    private static boolean uvEquals(TextureUV a, TextureUV b) {
        return Math.abs(a.from().x() - b.from().x()) < UV_TOLERANCE
                && Math.abs(a.from().y() - b.from().y()) < UV_TOLERANCE
                && Math.abs(a.to().x() - b.to().x()) < UV_TOLERANCE
                && Math.abs(a.to().y() - b.to().y()) < UV_TOLERANCE;
    }
}
