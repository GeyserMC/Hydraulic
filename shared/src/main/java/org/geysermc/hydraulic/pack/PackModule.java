package org.geysermc.hydraulic.pack;

import com.mojang.logging.LogUtils;
import org.geysermc.event.Event;
import org.geysermc.hydraulic.pack.context.PackProcessContext;
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
 * Pack modules handle converting data from the Minecraft
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
    private final List<Consumer<PackProcessContext<T>>> postProcessors = new ArrayList<>();

    /**
     * Called after all the pack modules have
     * been called and the pack has been created.
     * <p>
     * This is where you should do any post-processing
     * of the pack, such as adding any extra files
     * or modifying existing ones.
     *
     * @param context the context of the pack
     */
    public abstract void postProcess(@NotNull PackProcessContext<T> context);

    /**
     * Adds a post processor to this pack module.
     *
     * @param postProcessor the post processor
     */
    public final void postProcess(@NotNull Consumer<PackProcessContext<T>> postProcessor) {
        this.postProcessors.add(postProcessor);
    }

    /**
     * Tests if this pack module should be used.
     *
     * @param context the context of the pack
     * @return if this pack module should be used
     */
    public boolean test(@NotNull PackProcessContext<T> context) {
        return true;
    }

    /**
     * Listens on the given {@link Event event}.
     *
     * @param event the event to listen on
     * @param eventConsumer the event consumer
     * @param <E> the event type
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <E extends Event> void listenOn(@NotNull Class<E> event, @NotNull Consumer<PackEventContext<E, T>> eventConsumer) {
        this.eventListeners.computeIfAbsent(event, v -> new ArrayList<>()).add((Consumer) eventConsumer);
    }

    void postProcess0(@NotNull PackProcessContext<T> context) {
        for (Consumer<PackProcessContext<T>> postProcessor : this.postProcessors) {
            try {
                postProcessor.accept(context);
            } catch (Throwable t) {
                LOGGER.error("Error processing post processor {}", postProcessor, t);
            }
        }

        this.postProcess(context);
    }

    Map<Class<? extends Event>, List<Consumer<PackEventContext<?, T>>>> eventListeners() {
        return this.eventListeners;
    }

    <E extends Event> void call(@NotNull Class<E> event, @NotNull PackEventContext<E, T> context) {
        for (Map.Entry<Class<? extends Event>, List<Consumer<PackEventContext<?, T>>>> entry : this.eventListeners.entrySet()) {
            if (!entry.getKey().isAssignableFrom(event)) {
                continue;
            }

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
