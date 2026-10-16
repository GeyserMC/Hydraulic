package org.geysermc.hydraulic.fabric.entity;

import org.geysermc.conduit.CaptureChannel;
import org.geysermc.conduit.Intercept;
import org.geysermc.conduit.Trace;
import org.geysermc.hydraulic.entity.conduit.ModelLayerLocation;
import org.geysermc.hydraulic.entity.conduit.ModelLayerRegistration;

import java.util.function.Supplier;

@Intercept("net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry")
public class ModelLayerRegistry {

    @Intercept.Capture
    public static CaptureChannel<ModelLayerRegistration> CAPTURE;

    public static void registerModelLayer(ModelLayerLocation location, Supplier<Trace> definition) {
        CAPTURE.emit(new ModelLayerRegistration(location, definition));
    }
}
