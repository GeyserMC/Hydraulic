package org.geysermc.hydraulic.pack.context;

import org.geysermc.event.Event;
import org.geysermc.hydraulic.HydraulicImpl;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the context of a pack for an event.
 *
 * @param <T> the module type
 */
public class PackEventContext<E extends Event, T extends PackModule<T>> extends PackContext<T> {
    private final E event;

    public PackEventContext(@NotNull HydraulicImpl hydraulic, @NotNull ModInfo mod, @NotNull T module, @NotNull E event) {
        super(hydraulic, mod, module);

        this.event = event;
    }

    /**
     * Gets the event that this context is part of.
     *
     * @return the event that this context is part of
     */
    @NotNull
    public E event() {
        return this.event;
    }
}
