package org.geysermc.hydraulic.entity.animation;

import org.geysermc.conduit.ConduitContext;
import org.geysermc.conduit.Conduits;
import org.geysermc.conduit.Summary;
import org.geysermc.conduit.Value;
import org.geysermc.conduit.VirtualMachine;
import org.geysermc.hydraulic.entity.JavaEntityModel;
import org.geysermc.hydraulic.entity.conduit.Identifier;
import org.geysermc.hydraulic.entity.conduit.ModelLayerLocation;
import org.geysermc.hydraulic.entity.vanilla.VanillaModels;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
     * Finds the models a renderer may be constructed with, as either {@code new Model(context.bakeLayer(LAYER))}
     * or a {@code Model::new} reference applied to the baked layer. A renderer that picks between models
     * has each of them, in the order they appear.
     *
     * @param vm the machine the renderer was captured by
     * @param context the context the renderer was read from
     * @param renderer the renderer, as constructed by its provider
     * @return the bindings, empty if the renderer doesn't build its model that way
     */
    @NotNull
    public static List<ModelBinding> of(@NotNull VirtualMachine vm, @NotNull ConduitContext context, @Nullable Value renderer) {
        if (!(renderer instanceof Value.Constructed constructed)) {
            return List.of();
        }

        List<ModelBinding> direct = find(renderer, context);
        if (!direct.isEmpty()) {
            return direct;
        }

        Summary summary = vm.summarize(constructed);
        if (summary == null) {
            return List.of();
        }

        for (Summary.Invocation invocation : summary.invocations()) {
            List<ModelBinding> found = find(invocation.call(), context);
            if (!found.isEmpty()) {
                return found;
            }
        }

        for (Summary.Assignment assignment : summary.assignments()) {
            List<ModelBinding> found = find(assignment.value(), context);
            if (!found.isEmpty()) {
                return found;
            }
        }

        return List.of();
    }

    @NotNull
    private static List<ModelBinding> find(@Nullable Value value, @NotNull ConduitContext context) {
        return switch (value) {
            case Value.Constructed(String className, List<Value> args) -> {
                ModelBinding bound = args.size() == 1 ? bind(className, args.getFirst(), context) : null;
                yield bound != null ? List.of(bound) : first(args, context);
            }

            case Value.InstanceCall(String owner, String name, Value receiver, List<Value> args) -> {
                if (receiver instanceof Value.Lambda lambda && "<init>".equals(lambda.implMethod()) && args.size() == 1) {
                    ModelBinding bound = bind(lambda.implOwner(), args.getFirst(), context);
                    if (bound != null) {
                        yield List.of(bound);
                    }
                }

                List<ModelBinding> inReceiver = find(receiver, context);
                yield !inReceiver.isEmpty() ? inReceiver : first(args, context);
            }

            case Value.Conditional(Value _, Value then, Value otherwise) -> {
                List<ModelBinding> both = new ArrayList<>(find(then, context));
                both.addAll(find(otherwise, context));
                yield both;
            }

            case Value.StaticCall(String owner, String name, List<Value> args) -> first(args, context);
            case null, default -> List.of();
        };
    }

    @NotNull
    private static List<ModelBinding> first(@NotNull List<Value> values, @NotNull ConduitContext context) {
        for (Value value : values) {
            List<ModelBinding> found = find(value, context);
            if (!found.isEmpty()) {
                return found;
            }
        }

        return List.of();
    }

    @Nullable
    private static ModelBinding bind(@Nullable String modelClass, @NotNull Value part, @NotNull ConduitContext context) {
        if (modelClass == null || !(part instanceof Value.InstanceCall(String owner, String name, Value receiver, List<Value> args))
                || !BAKE_LAYER.equals(name) || args.size() != 1) {
            return null;
        }

        // Vanilla's layer locations are client only, so they are looked up in the bundle instead
        if (args.getFirst() instanceof Value.Field(String fieldOwner, String field) && fieldOwner.equals(VanillaModels.MODEL_LAYERS)) {
            JavaEntityModel vanilla = VanillaModels.get().location(field);
            return vanilla == null ? null : new ModelBinding(modelClass, vanilla.namespace(), vanilla.path(), vanilla.layer());
        }

        ModelLayerLocation location = Conduits.as(args.getFirst(), ModelLayerLocation.class, context);
        Identifier model = location == null ? null : location.model();
        if (model == null || model.namespace() == null || model.path() == null || location.layer() == null) {
            return null;
        }

        return new ModelBinding(modelClass, model.namespace(), model.path(), location.layer());
    }
}
