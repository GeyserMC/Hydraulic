package org.geysermc.hydraulic.fabric.entity;

import org.geysermc.conduit.CaptureChannel;
import org.geysermc.conduit.Intercept;
import org.geysermc.conduit.Trace;
import org.geysermc.conduit.Value;
import org.geysermc.hydraulic.entity.conduit.EntityRendererRegistration;

import java.util.function.Supplier;

@Intercept("net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry")
public class EntityRendererRegistry {

    @Intercept.Capture
    public static CaptureChannel<EntityRendererRegistration> CAPTURE;

    public static void register(Value type, Supplier<Trace> provider) {
        CAPTURE.emit(new EntityRendererRegistration(type, provider));
    }
}
