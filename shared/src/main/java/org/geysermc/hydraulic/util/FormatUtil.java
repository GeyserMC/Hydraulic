package org.geysermc.hydraulic.util;

import java.time.Duration;

public class FormatUtil {
    /**
     * Formats a duration into a human-readable format.
     * <a href="https://stackoverflow.com/a/40487511/5299903">StackOverflow answer source</a>
     *
     * @param duration the duration to format
     * @return the formatted duration
     */
    public static String humanReadableFormat(Duration duration) {
        return duration
            .withNanos(0)
            .toString()
            .substring(2)
            .replaceAll("(\\d[HMS])(?!$)", "$1 ")
            .toLowerCase();
    }

    /**
     * Formats a duration into a human-readable format.
     * @see #humanReadableFormat(Duration)
     *
     * @param ms the time in milliseconds to format
     * @return the formatted duration
     */
    public static String humanReadableFormat(long ms) {
        return humanReadableFormat(Duration.ofMillis(ms));
    }
}
