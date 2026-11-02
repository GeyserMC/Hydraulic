package org.geysermc.hydraulic.neoforge.entity;

import org.geysermc.conduit.CaptureChannel;
import org.geysermc.conduit.Intercept;
import org.geysermc.conduit.Trace;
import org.geysermc.conduit.Value;
import org.geysermc.hydraulic.entity.conduit.EntityRendererRegistration;

import java.util.function.Supplier;

@Intercept("net.neoforged.neoforge.client.event.EntityRenderersEvent$RegisterRenderers")
public class RegisterRenderers {

    @Intercept.Capture
    public static CaptureChannel<EntityRendererRegistration> CAPTURE;

    public static void registerEntityRenderer(Value event, Value type, Supplier<Trace> provider) {
        CAPTURE.emit(new EntityRendererRegistration(type, provider));
    }
}
