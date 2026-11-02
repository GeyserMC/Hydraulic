package org.geysermc.hydraulic.neoforge.entity;

import org.geysermc.conduit.CaptureChannel;
import org.geysermc.conduit.Intercept;
import org.geysermc.conduit.Trace;
import org.geysermc.conduit.Value;
import org.geysermc.hydraulic.entity.conduit.ModelLayerLocation;
import org.geysermc.hydraulic.entity.conduit.ModelLayerRegistration;

import java.util.function.Supplier;

@Intercept("net.neoforged.neoforge.client.event.EntityRenderersEvent$RegisterLayerDefinitions")
public class RegisterLayerDefinitions {

    @Intercept.Capture
    public static CaptureChannel<ModelLayerRegistration> CAPTURE;

    public static void registerLayerDefinition(Value event, ModelLayerLocation location, Supplier<Trace> definition) {
        CAPTURE.emit(new ModelLayerRegistration(location, definition));
    }
}
