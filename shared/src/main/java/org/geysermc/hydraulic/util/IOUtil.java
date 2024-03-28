package org.geysermc.hydraulic.util;

import org.apache.commons.io.function.IOFunction;

import java.io.IOException;
import java.util.function.Function;

public class IOUtil {
    public static <T, R> Function<T, R> uncheckFunction(IOFunction<T, R> function) {
        return t -> {
            try {
                return function.apply(t);
            } catch (IOException e) {
                throw rethrow(e);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T rethrow(IOException exception) throws T {
        throw (T)exception;
    }
}
