package org.geysermc.hydraulic.entity;

import org.geysermc.conduit.CaptureChannel;
import org.geysermc.hydraulic.entity.conduit.ModelLayerRegistration;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * The platform's model layer registry, and the channel its registrations are captured on.
 *
 * @param registryClass the class that contains the registration method
 * @param captureChannel the capture channel that will be used to capture the registration
 */
public record ModelLayerRegistryInfo(@NotNull Class<?> registryClass, @NotNull Supplier<CaptureChannel<ModelLayerRegistration>> captureChannel) {
}
