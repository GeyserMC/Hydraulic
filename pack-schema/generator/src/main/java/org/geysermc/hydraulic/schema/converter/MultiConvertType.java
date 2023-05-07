package org.geysermc.hydraulic.schema.converter;

/**
 * The type of conversion to use for multi-type schemas.
 */
public enum MultiConvertType {
    /**
     * Uses the simplest type for object conversion.
     */
    SIMPLE,
    /**
     * Uses the most complex type for object conversion.
     */
    COMPLEX,
    /**
     * Attempts to use an object that accepts all possible
     * types for a given property. However, this may not
     * always be possible and will just use an Object in
     * cases such as these.
     */
    MULTI
}
