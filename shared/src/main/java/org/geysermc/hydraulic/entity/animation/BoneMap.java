package org.geysermc.hydraulic.entity.animation;

import org.geysermc.conduit.Summary;
import org.geysermc.conduit.Value;
import org.geysermc.hydraulic.entity.BedrockEntityModelConverter;
import org.geysermc.hydraulic.entity.conduit.EntityModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolves the model parts a model's code refers to, such as {@code this.head} or
 * {@code this.segments[2]}, to the bones they become.
 */
public final class BoneMap {
    private static final String GET_CHILD = "getChild";

    private final Map<String, Value> fields = new HashMap<>();
    private final Map<List<String>, Bone> bones = new LinkedHashMap<>();

    private BoneMap(@NotNull Summary constructor, @NotNull EntityModel model) {
        for (Summary.Assignment assignment : constructor.assignments()) {
            if (assignment.target() instanceof Value.This) {
                this.fields.putIfAbsent(assignment.field(), assignment.value());
            }
        }

        Map<List<String>, String> names = BedrockEntityModelConverter.boneNames(model);
        index(model.parts(), List.of(), names, this.bones);
    }

    /**
     * Creates a bone map for a model class.
     *
     * @param constructor the summary of the model's constructor, which takes the baked root part first
     * @param model the geometry the model is baked from
     * @return the bone map
     */
    @NotNull
    public static BoneMap of(@NotNull Summary constructor, @NotNull EntityModel model) {
        return new BoneMap(constructor, model);
    }

    /**
     * Finds the bone a part refers to.
     *
     * @param part a part, as the model's code refers to it
     * @return the bone, or null if it is the root or can't be resolved
     */
    @Nullable
    public Bone bone(@NotNull Value part) {
        List<String> path = this.path(part, 0);
        return path == null || path.isEmpty() ? null : this.bones.get(path);
    }

    /**
     * Finds the bone for a part by name, the first found walking the model depth first, as baking a
     * keyframe animation looks parts up.
     *
     * @param name the part's name
     * @return the bone, or null if no part has that name
     */
    @Nullable
    public Bone named(@NotNull String name) {
        for (Map.Entry<List<String>, Bone> entry : this.bones.entrySet()) {
            if (entry.getKey().getLast().equals(name)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Gets what the model's constructor assigns to one of its fields.
     *
     * @param name the field
     * @return the value, or null if the constructor doesn't assign it
     */
    @Nullable
    public Value field(@NotNull String name) {
        return this.fields.get(name);
    }

    @Nullable
    private List<String> path(@Nullable Value part, int depth) {
        if (depth > 16) {
            return null;
        }

        return switch (part) {
            case Value.Parameter(int index) when index == 0 -> List.of();
            case Value.InstanceField(Value.This _, String _, String field) -> this.path(this.fields.get(field), depth + 1);
            case Value.Array _ -> null;
            case Value.Operation(Value.Operator operator, List<Value> operands)
                when operator == Value.Operator.INDEX && operands.get(1) instanceof Value.Literal(Integer index) -> {
                Value array = operands.get(0) instanceof Value.InstanceField(Value.This _, String _, String field)
                    ? this.fields.get(field)
                    : operands.get(0);
                yield array instanceof Value.Array(String _, List<Value> elements) && index >= 0 && index < elements.size()
                    ? this.path(elements.get(index), depth + 1)
                    : null;
            }
            case Value.InstanceCall(String _, String name, Value receiver, List<Value> args)
                when GET_CHILD.equals(name) && args.size() == 1 && args.getFirst() instanceof Value.Literal(String child) -> {
                List<String> parent = this.path(receiver, depth + 1);
                if (parent == null) {
                    yield null;
                }

                List<String> path = new ArrayList<>(parent);
                path.add(child);
                yield path;
            }
            case null, default -> null;
        };
    }

    private static void index(@NotNull List<EntityModel.Part> parts, @NotNull List<String> parent,
                              @NotNull Map<List<String>, String> names, @NotNull Map<List<String>, Bone> out) {
        for (EntityModel.Part part : parts) {
            List<String> path = new ArrayList<>(parent);
            path.add(part.name());
            String name = names.get(path);
            if (name != null) {
                out.put(List.copyOf(path), new Bone(name, part));
            }
            index(part.children(), path, names, out);
        }
    }

    /**
     * A bone, with the part it was converted from.
     *
     * @param name the bone's name in the geometry
     * @param part the part, holding its rest pose
     */
    public record Bone(@NotNull String name, @NotNull EntityModel.Part part) {
    }
}
