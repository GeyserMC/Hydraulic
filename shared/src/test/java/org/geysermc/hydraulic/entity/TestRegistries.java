package org.geysermc.hydraulic.entity;

import org.geysermc.conduit.CaptureChannel;
import org.geysermc.conduit.Intercept;
import org.geysermc.conduit.Trace;
import org.geysermc.conduit.Value;
import org.geysermc.hydraulic.entity.conduit.EntityRendererRegistration;
import org.geysermc.hydraulic.entity.conduit.ModelLayerLocation;
import org.geysermc.hydraulic.entity.conduit.ModelLayerRegistration;

import java.util.function.Supplier;

final class TestRegistries {
    static final ModelLayerRegistryInfo LAYERS = new ModelLayerRegistryInfo(Layers.class, () -> Layers.CAPTURE);
    static final EntityRendererRegistryInfo RENDERERS = new EntityRendererRegistryInfo(Renderers.class, () -> Renderers.CAPTURE);

    private TestRegistries() {
    }

    @Intercept("net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry")
    public static class Layers {
        @Intercept.Capture
        public static CaptureChannel<ModelLayerRegistration> CAPTURE;

        public static void registerModelLayer(ModelLayerLocation location, Supplier<Trace> definition) {
            CAPTURE.emit(new ModelLayerRegistration(location, definition));
        }
    }

    @Intercept("net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry")
    public static class Renderers {
        @Intercept.Capture
        public static CaptureChannel<EntityRendererRegistration> CAPTURE;

        public static void register(Value type, Supplier<Trace> provider) {
            CAPTURE.emit(new EntityRendererRegistration(type, provider));
        }
    }
}
