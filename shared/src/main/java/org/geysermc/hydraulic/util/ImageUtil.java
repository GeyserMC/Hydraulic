package org.geysermc.hydraulic.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.unnamed.creative.base.Writable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Utility class for images.
 */
public class ImageUtil {
    /**
     * Reads an image from the given data.
     *
     * @param data the image data, EG a texture
     * @return the image, or null if it couldn't be read
     */
    @Nullable
    public static BufferedImage read(@NotNull Writable data) {
        try {
            return ImageIO.read(new ByteArrayInputStream(data.toByteArray()));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Works out how the given image uses transparency.
     * <p>
     * An image only counts as translucent if at least a quarter of its visible pixels
     * are semi-transparent, so a few anti-aliased edges don't make a cutout texture translucent.
     *
     * @param data the image data, EG a texture
     * @return the transparency of the image
     */
    @NotNull
    public static Transparency transparency(@NotNull Writable data) {
        BufferedImage image = read(data);
        if (image == null || !image.getColorModel().hasAlpha()) {
            return Transparency.OPAQUE;
        }

        int visible = 0;
        int semiTransparent = 0;
        boolean transparent = false;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int alpha = image.getRGB(x, y) >>> 24;
                if (alpha == 0) {
                    transparent = true;
                    continue;
                }

                visible++;
                if (alpha < 255) {
                    semiTransparent++;
                }
            }
        }

        if (semiTransparent > 0 && semiTransparent * 4 >= visible) {
            return Transparency.TRANSLUCENT;
        }

        return transparent || semiTransparent > 0 ? Transparency.CUTOUT : Transparency.OPAQUE;
    }

    public enum Transparency {
        /**
         * Every pixel is fully opaque.
         */
        OPAQUE,
        /**
         * Pixels are either fully opaque or fully transparent, EG leaves or plants.
         */
        CUTOUT,
        /**
         * Pixels are semi-transparent, EG stained glass or ice.
         */
        TRANSLUCENT
    }

    /**
     * Multiplies the colour of every pixel by the given tint, the same way Java applies item tints.
     *
     * @param image the image to tint
     * @param tint the tint colour in RGB, alpha is ignored
     * @return a new tinted image
     */
    @NotNull
    public static BufferedImage tint(@NotNull BufferedImage image, int tint) {
        int tintRed = (tint >> 16) & 0xFF;
        int tintGreen = (tint >> 8) & 0xFF;
        int tintBlue = tint & 0xFF;

        BufferedImage tinted = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int argb = image.getRGB(x, y);
                int red = ((argb >> 16) & 0xFF) * tintRed / 255;
                int green = ((argb >> 8) & 0xFF) * tintGreen / 255;
                int blue = (argb & 0xFF) * tintBlue / 255;
                tinted.setRGB(x, y, (argb & 0xFF000000) | (red << 16) | (green << 8) | blue);
            }
        }

        return tinted;
    }

    /**
     * Encodes the given image as a PNG.
     *
     * @param image the image
     * @return the PNG bytes, or null if it couldn't be written
     */
    public static byte @Nullable [] toPng(@NotNull BufferedImage image) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", output);
            return output.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }
}
