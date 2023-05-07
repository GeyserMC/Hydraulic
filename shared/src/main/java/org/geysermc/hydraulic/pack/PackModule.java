package org.geysermc.hydraulic.pack;

import com.mojang.logging.LogUtils;
import org.geysermc.event.Event;
import org.geysermc.hydraulic.pack.context.PackCreateContext;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Represents a pack module.
 * <p>
 * Pack modules actually handle converting data from the Minecraft
 * server into a pack that the Bedrock client can understand.
 * <p>
 * These are split up based on each specific aspect of the pack,
 * such as blocks, items, entities, etc. Certain pack loaders
 * may have more complex behaviors, such as listening on certain
 * events inside to register needed data into Geyser, such as
 * items.
 *
 * @param <T> the pack module
 */
public abstract class PackModule<T extends PackModule<T>> {
    protected static Logger LOGGER = LogUtils.getLogger();

    private final Map<Class<? extends Event>, List<Consumer<PackEventContext<?, T>>>> eventListeners = new HashMap<>();

    /**
     * Creates the relevant data for this section of the pack.
     *
     * @param context the context of the pack
     */
    public abstract void create(@NotNull PackCreateContext<T> context);

    /**
     * Listens on the given {@link Event event}.
     *
     * @param event the event to listen on
     * @param eventConsumer the event consumer
     * @param <E> the event type
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <E extends Event> void listenOn(@NotNull Class<E> event, Consumer<PackEventContext<E, T>> eventConsumer) {
        this.eventListeners.computeIfAbsent(event, v -> new ArrayList<>()).add((Consumer) eventConsumer);
    }

    <E extends Event> void call(Class<E> event, PackEventContext<E, T> context) {
        for (Map.Entry<Class<? extends Event>, List<Consumer<PackEventContext<?, T>>>> entry : this.eventListeners.entrySet()) {
            List<Consumer<PackEventContext<?, T>>> listeners = entry.getValue();
            for (Consumer<PackEventContext<?, T>> listener : listeners) {
                try {
                    listener.accept(context);
                } catch (Throwable t) {
                    LOGGER.error("Error processing event {}", event, t);
                }
            }
        }
    }
}
