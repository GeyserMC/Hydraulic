package org.geysermc.hydraulic.entity;

import org.geysermc.conduit.CaptureChannel;
import org.geysermc.hydraulic.entity.conduit.EntityRendererRegistration;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * The platform's entity renderer registry, and the channel its registrations are captured on.
 *
 * @param registryClass the class that contains the registration method
 * @param captureChannel the capture channel that will be used to capture the registration
 */
public record EntityRendererRegistryInfo(@NotNull Class<?> registryClass,
                                         @NotNull Supplier<CaptureChannel<EntityRendererRegistration>> captureChannel
) {
}
