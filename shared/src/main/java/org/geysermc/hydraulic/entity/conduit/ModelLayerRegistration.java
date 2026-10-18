package org.geysermc.hydraulic.entity.conduit;

import org.geysermc.conduit.Trace;

import java.util.function.Supplier;

/**
 * A model layer registration seen while scanning a mod.
 *
 * @param location the layer's location
 * @param definition evaluates the definition provider, returning the layer definition it builds
 */
public record ModelLayerRegistration(ModelLayerLocation location, Supplier<Trace> definition) {
}
