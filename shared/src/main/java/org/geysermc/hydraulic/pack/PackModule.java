package org.geysermc.hydraulic.pack;

import org.geysermc.event.Event;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.pack.context.PackPreProcessContext;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final Map<Class<? extends Event>, List<Consumer<PackEventContext<?, T>>>> eventListeners = new HashMap<>();

    private final List<Consumer<PackPreProcessContext<T>>> preProcessors = new ArrayList<>();
    private final List<Consumer<PackPostProcessContext<T>>> postProcessors = new ArrayList<>();

    /**
     * Adds a pre processor to this pack module.
     *
     * @param preProcessor the pre processor
     */
    public final void preProcess(@NotNull Consumer<PackPreProcessContext<T>> preProcessor) {
        this.preProcessors.add(preProcessor);
    }

    /**
     * Adds a post processor to this pack module.
     *
     * @param postProcessor the post processor
     */
    public final void postProcess(@NotNull Consumer<PackPostProcessContext<T>> postProcessor) {
        this.postProcessors.add(postProcessor);
    }

    /**
     * Tests if this pack module should be used.
     *
     * @param context the context of the pack
     * @return if this pack module should be used
     */
    public boolean test(@NotNull PackPostProcessContext<T> context) {
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

    boolean hasPreProcessors() {
        return !this.preProcessors.isEmpty();
    }

    void preProcess0(@NotNull PackPreProcessContext<T> context) {
        for (Consumer<PackPreProcessContext<T>> preProcessor : this.preProcessors) {
            try {
                preProcessor.accept(context);
            } catch (Throwable t) {
                context.logger().error("Error processing pre processor {}", preProcessor, t);
            }
        }
    }

    void postProcess0(@NotNull PackPostProcessContext<T> context) {
        for (Consumer<PackPostProcessContext<T>> postProcessor : this.postProcessors) {
            try {
                postProcessor.accept(context);
            } catch (Throwable t) {
                context.logger().error("Error processing post processor {}", postProcessor, t);
            }
        }
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
                    context.logger().error("Error processing event {}", event, t);
                }
            }
        }
    }

    /**
     * Gets the logger for this pack module.
     *
     * @param mod the mod that this pack module is parsing
     * @return the logger
     */
    public Logger logger(ModInfo mod) {
        return LoggerFactory.getLogger(this.getClass().getSimpleName() + "/" + mod.id());
    }
}
