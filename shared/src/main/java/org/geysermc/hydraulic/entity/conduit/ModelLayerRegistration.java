package org.geysermc.hydraulic.entity.conduit;

import java.util.function.Supplier;

public record ModelLayerRegistration(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
}
