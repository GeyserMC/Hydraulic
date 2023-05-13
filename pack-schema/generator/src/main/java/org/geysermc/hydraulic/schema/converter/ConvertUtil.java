package org.geysermc.hydraulic.schema.converter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for converting schemas.
 */
public class ConvertUtil {

    /**
     * Converts a field name to a class name.
     *
     * @param fieldName the field name to convert
     * @return the converted class name
     */
    @NotNull
    public static String toClassName(@NotNull String fieldName) {
        String className = StringUtils.capitalize(fieldName);
        className = className.replace("-", "Minus");
        className = className.replace("+", "Plus");
        return className;
    }

    /**
     * Sanitizes a package name.
     *
     * @param packageName the package name to sanitize
     * @return the converted package name
     */
    @NotNull
    public static String sanitizePackageName(@NotNull String packageName) {
        packageName = packageName.toLowerCase();
        packageName = packageName.replace("-", "_");
        packageName = packageName.replace("+", "plus");
        return packageName;
    }

    /**
     * Sanitizes a field name.
     *
     * @param fieldName the field name to sanitize
     * @return the sanitized field name
     */
    @NotNull
    public static String sanitizeFieldName(@NotNull String fieldName) {
        if (fieldName.contains(":")) {
            fieldName = fieldName.split(":")[1];
        }

        if (fieldName.contains("+")) {
            fieldName = fieldName.replace("+", "plus_");
        }

        if (fieldName.contains("-")) {
            fieldName = fieldName.replace("-", "minus_");
        }

        if (fieldName.contains("_")) {
            fieldName = CaseUtils.toCamelCase(fieldName, false, '_');
        }

        return fieldName;
    }
}
