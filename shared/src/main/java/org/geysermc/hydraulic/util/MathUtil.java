package org.geysermc.hydraulic.util;

public class MathUtil {
    /**
     * Rounds a float to a specified number of decimal places.
     *
     * @param value the value to round
     * @param places the number of decimal places to round to
     * @return the rounded value
     */
    public static float round(float value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (float) tmp / factor;
    }
}
