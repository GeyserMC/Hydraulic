package org.geysermc.hydraulic.fabric.entity;

import org.geysermc.conduit.CaptureChannel;
import org.geysermc.conduit.Intercept;
import org.geysermc.hydraulic.entity.conduit.EntityRendererRegistration;
import org.geysermc.hydraulic.entity.conduit.EntityType;
import org.geysermc.hydraulic.entity.conduit.LivingEntityRenderer;

import java.util.function.Supplier;

@Intercept("net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry")
public class EntityRendererRegistry {

    @Intercept.Capture
    public static CaptureChannel<EntityRendererRegistration> CAPTURE;

    public static void register(EntityType type, Supplier<LivingEntityRenderer> provider) {
        CAPTURE.emit(new EntityRendererRegistration(type, provider));
    }
}
