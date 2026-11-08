package org.geysermc.hydraulic.item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Bakes an item's texture layers and their tints into one image, as Bedrock item icons can't be tinted.
 */
final class ItemIcons {
    private ItemIcons() {
    }

    /**
     * Bakes an icon.
     *
     * @param layers the PNG of each layer, bottom first
     * @param tints the colour each layer is multiplied by, as {@code 0xRRGGBB}, or null to leave it as is
     * @return the icon as a PNG, or null if a layer couldn't be read
     */
    static byte @Nullable [] bake(@NotNull List<byte[]> layers, @NotNull List<Integer> tints) {
        try {
            BufferedImage icon = null;
            for (int i = 0; i < layers.size(); i++) {
                BufferedImage layer = ImageIO.read(new ByteArrayInputStream(layers.get(i)));
                if (layer == null) {
                    return null;
                }
                if (icon == null) {
                    icon = new BufferedImage(layer.getWidth(), layer.getHeight(), BufferedImage.TYPE_INT_ARGB);
                }

                Integer tint = i < tints.size() ? tints.get(i) : null;
                draw(icon, layer, tint);
            }

            if (icon == null) {
                return null;
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(icon, "png", out);
            return out.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    // Multiplies the layer by the tint and blends it over what's below, as Java renders item layers.
    private static void draw(@NotNull BufferedImage icon, @NotNull BufferedImage layer, @Nullable Integer tint) {
        int width = Math.min(icon.getWidth(), layer.getWidth());
        int height = Math.min(icon.getHeight(), layer.getHeight());
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int source = layer.getRGB(x, y);
                int alpha = source >>> 24;
                if (alpha == 0) {
                    continue;
                }

                int red = (source >> 16) & 0xFF;
                int green = (source >> 8) & 0xFF;
                int blue = source & 0xFF;
                if (tint != null) {
                    red = red * ((tint >> 16) & 0xFF) / 255;
                    green = green * ((tint >> 8) & 0xFF) / 255;
                    blue = blue * (tint & 0xFF) / 255;
                }

                int below = icon.getRGB(x, y);
                float a = alpha / 255f;
                int outAlpha = Math.max(alpha, below >>> 24);
                int outRed = Math.round(red * a + ((below >> 16) & 0xFF) * (1 - a));
                int outGreen = Math.round(green * a + ((below >> 8) & 0xFF) * (1 - a));
                int outBlue = Math.round(blue * a + (below & 0xFF) * (1 - a));
                icon.setRGB(x, y, (outAlpha << 24) | (outRed << 16) | (outGreen << 8) | outBlue);
            }
        }
    }
}
