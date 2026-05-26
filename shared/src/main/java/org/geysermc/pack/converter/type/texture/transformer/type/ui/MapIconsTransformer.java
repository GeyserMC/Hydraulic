package org.geysermc.pack.converter.type.texture.transformer.type.ui;

import net.kyori.adventure.key.Key;
import org.geysermc.pack.converter.type.texture.transformer.TextureTransformer;
import org.geysermc.pack.converter.type.texture.transformer.TransformContext;
import org.geysermc.pack.converter.util.KeyUtil;

import java.io.IOException;

/**
 * Null-safe replacement for PackConverter's map icon transformer.
 */
public class MapIconsTransformer implements TextureTransformer {
    private static final String OUTPUT_TEXTURE = "map/map_icons.png";
    private static final String[] SOURCE_TEXTURES = {
            "map/decorations/player.png",
            "map/decorations/frame.png",
            "map/decorations/red_marker.png",
            "map/decorations/blue_marker.png",
            "map/decorations/red_x.png",
            "map/decorations/target_point.png",
            "map/decorations/player_off_map.png",
            null,
            null,
            null,
            null,
            null,
            null,
            "map/decorations/player_off_limits.png",
            "map/decorations/woodland_mansion.png",
            "map/decorations/ocean_monument.png"
    };

    @Override
    public void transform(TransformContext context) throws IOException {
        Key[] textures = new Key[SOURCE_TEXTURES.length];
        int skipped = 0;
        for (int i = 0; i < SOURCE_TEXTURES.length; i++) {
            String sourceTexture = SOURCE_TEXTURES[i];
            if (sourceTexture == null || sourceTexture.isBlank()) {
                skipped++;
                continue;
            }

            textures[i] = KeyUtil.key(Key.MINECRAFT_NAMESPACE, sourceTexture);
        }

        if (skipped > 0) {
            context.warn("Skipping " + skipped + " empty map icon grid slot(s) while building minecraft:" + OUTPUT_TEXTURE);
        }

        gridTransform(context, true, 4, 4, KeyUtil.key(Key.MINECRAFT_NAMESPACE, OUTPUT_TEXTURE), textures);
    }
}
