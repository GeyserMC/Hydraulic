package org.geysermc.hydraulic.entity.animation;

import org.geysermc.conduit.ConduitContext;
import org.geysermc.conduit.Conduits;
import org.geysermc.conduit.Summary;
import org.geysermc.conduit.Value;
import org.geysermc.conduit.VirtualMachine;
import org.geysermc.hydraulic.entity.conduit.Identifier;
import org.geysermc.hydraulic.entity.conduit.ModelLayerLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The model a renderer draws its entity with and the layer that model is baked from.
 *
 * @param modelClass the internal name of the model class
 * @param namespace the namespace of the layer's model location
 * @param path the path of the layer's model location
 * @param layer the layer name
 */
public record ModelBinding(@NotNull String modelClass, @NotNull String namespace, @NotNull String path, @NotNull String layer) {
    private static final String BAKE_LAYER = "bakeLayer";

    /**
     * Finds the model a renderer is constructed with, as either {@code new Model(context.bakeLayer(LAYER))}
     * or a {@code Model::new} reference applied to the baked layer.
     *
     * @param vm the machine the renderer was captured by
     * @param context the context the renderer was read from
     * @param renderer the renderer, as constructed by its provider
     * @return the binding, or null if the renderer doesn't build its model that way
     */
    @Nullable
    public static ModelBinding of(@NotNull VirtualMachine vm, @NotNull ConduitContext context, @Nullable Value renderer) {
        if (!(renderer instanceof Value.Constructed constructed)) {
            return null;
        }

        ModelBinding direct = find(renderer, context);
        if (direct != null) {
            return direct;
        }

        Summary summary = vm.summarize(constructed);
        if (summary == null) {
            return null;
        }

        for (Summary.Invocation invocation : summary.invocations()) {
            ModelBinding found = find(invocation.call(), context);
            if (found != null) {
                return found;
            }
        }

        for (Summary.Assignment assignment : summary.assignments()) {
            ModelBinding found = find(assignment.value(), context);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    @Nullable
    private static ModelBinding find(@Nullable Value value, @NotNull ConduitContext context) {
        return switch (value) {
            case Value.Constructed(String className, List<Value> args) -> {
                ModelBinding bound = args.size() == 1 ? bind(className, args.getFirst(), context) : null;
                yield bound != null ? bound : first(args, context);
            }

            case Value.InstanceCall(String owner, String name, Value receiver, List<Value> args) -> {
                if (receiver instanceof Value.Lambda lambda && "<init>".equals(lambda.implMethod()) && args.size() == 1) {
                    ModelBinding bound = bind(lambda.implOwner(), args.getFirst(), context);
                    if (bound != null) {
                        yield bound;
                    }
                }

                ModelBinding inReceiver = find(receiver, context);
                yield inReceiver != null ? inReceiver : first(args, context);
            }

            case Value.StaticCall(String owner, String name, List<Value> args) -> first(args, context);
            case null, default -> null;
        };
    }

    @Nullable
    private static ModelBinding first(@NotNull List<Value> values, @NotNull ConduitContext context) {
        for (Value value : values) {
            ModelBinding found = find(value, context);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    @Nullable
    private static ModelBinding bind(@Nullable String modelClass, @NotNull Value part, @NotNull ConduitContext context) {
        if (modelClass == null || !(part instanceof Value.InstanceCall(String owner, String name, Value receiver, List<Value> args))
                || !BAKE_LAYER.equals(name) || args.size() != 1) {
            return null;
        }

        ModelLayerLocation location = Conduits.as(args.getFirst(), ModelLayerLocation.class, context);
        Identifier model = location == null ? null : location.model();
        if (model == null || model.namespace() == null || model.path() == null || location.layer() == null) {
            return null;
        }

        return new ModelBinding(modelClass, model.namespace(), model.path(), location.layer());
    }
}
