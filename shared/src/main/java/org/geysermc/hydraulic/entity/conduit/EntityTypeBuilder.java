package org.geysermc.hydraulic.entity.conduit;

import org.geysermc.conduit.CaptureChannel;
import org.geysermc.conduit.Intercept;

@Intercept("net.minecraft.world.entity.EntityType$Builder")
public class EntityTypeBuilder {

    @Intercept.Capture
    public static CaptureChannel<EntityTypeBuild> CAPTURE;

    public static void build(Builder builder, ResourceKey key) {
        CAPTURE.emit(new EntityTypeBuild(builder, key));
    }
}
