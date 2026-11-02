package org.geysermc.hydraulic.entity.conduit;

import org.geysermc.conduit.Trace;
import org.geysermc.conduit.Value;

import java.util.function.Supplier;

/**
 * A renderer registration seen while scanning a mod.
 *
 * @param type the entity type that the renderer is for, as passed to the registration
 * @param renderer evaluates the renderer provider, returning the renderer it constructs
 */
public record EntityRendererRegistration(Value type, Supplier<Trace> renderer) {
}
