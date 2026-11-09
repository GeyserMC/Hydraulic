package org.geysermc.hydraulic.block;

import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.base.CubeFace;
import team.unnamed.creative.base.Vector3Float;
import team.unnamed.creative.model.Element;
import team.unnamed.creative.model.ElementFace;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.texture.TextureUV;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Java models can leave out a face's UV (e.g. the vanilla wall and fence templates),
 * in which case it is worked out from the element bounds. Bedrock geometry has no
 * such default, so these faces need their UV set explicitly.
 */
public final class DefaultUvs {
    /**
     * Gets the default UV Java uses for a face of an element.
     *
     * @param face the face
     * @param from the element start, in model units
     * @param to the element end, in model units
     * @return the default UV, in the 0-1 range used by creative
     */
    @NotNull
    public static TextureUV defaultUv(@NotNull CubeFace face, @NotNull Vector3Float from, @NotNull Vector3Float to) {
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

    /**
     * Sets the default UV on every face of the model that doesn't have one.
     *
     * @param model the stitched model
     * @return the model with a UV on every face, or the same model if none were missing
     */
    @NotNull
    public static Model fill(@NotNull Model model) {
        if (!hasMissingUv(model)) {
            return model;
        }

        List<Element> elements = new ArrayList<>(model.elements().size());
        for (Element element : model.elements()) {
            Map<CubeFace, ElementFace> faces = new EnumMap<>(CubeFace.class);
            for (Map.Entry<CubeFace, ElementFace> entry : element.faces().entrySet()) {
                CubeFace direction = entry.getKey();
                ElementFace face = entry.getValue();
                if (face.uv() == null) {
                    TextureUV uv = defaultUv(direction, element.from(), element.to());
                    face = ElementFace.face(uv, face.texture(), face.cullFace(), face.rotation(), face.tintIndex());
                }

                faces.put(direction, face);
            }

            elements.add(element.toBuilder().faces(faces).build());
        }

        return model.toBuilder().elements(elements).build();
    }

    private static boolean hasMissingUv(@NotNull Model model) {
        for (Element element : model.elements()) {
            for (ElementFace face : element.faces().values()) {
                if (face.uv() == null) {
                    return true;
                }
            }
        }

        return false;
    }
}
