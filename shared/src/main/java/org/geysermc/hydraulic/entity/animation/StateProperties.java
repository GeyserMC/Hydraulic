package org.geysermc.hydraulic.entity.animation;

import org.geysermc.conduit.Summary;
import org.geysermc.conduit.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Resolves render state reads, sending anything the client can't compute as an entity property.
 *
 * <p>A renderer's {@code extractRenderState} fills the render state from the entity, so substituting
 * its assignments turns a value read from the state into one computed from the entity, which the
 * server can evaluate each tick.
 */
public final class StateProperties implements AnimationCompiler.StateResolver {
    // Bedrock refuses entities with more properties than this.
    private static final int LIMIT = 32;

    private static final Value STATE = new Value.Parameter(0);
    private static final Value EXTRACTED = new Value.Parameter(1);
    private static final Value PARTIAL_TICK = new Value.Parameter(2);
    private static final Value ENTITY = new Value.Parameter(0);
    private static final String LIVING_ENTITY = "net/minecraft/world/entity/LivingEntity";
    private static final String ENTITY_CLASS = "net/minecraft/world/entity/Entity";

    // Render state fields vanilla's renderers fill in, which a mod's renderer leaves to them.
    private static final Map<String, Value> VANILLA = Map.of(
        "isBaby", new Value.InstanceCall(LIVING_ENTITY, "isBaby", ENTITY, List.of()),
        "isInWater", new Value.InstanceCall(ENTITY_CLASS, "isInWater", ENTITY, List.of()),
        "isDiscrete", new Value.InstanceCall(ENTITY_CLASS, "isDiscrete", ENTITY, List.of()),
        "isInvisible", new Value.InstanceCall(ENTITY_CLASS, "isInvisible", ENTITY, List.of()),
        "ageInTicks", new Value.InstanceField(ENTITY, ENTITY_CLASS, "tickCount"),
        "deathTime", new Value.InstanceField(ENTITY, LIVING_ENTITY, "deathTime")
    );

    private final String namespace;
    private final Summary extract;
    private final int reserved;
    private final Map<String, Value> copies = new LinkedHashMap<>();
    private final Map<Value, StateProperty> properties = new LinkedHashMap<>();
    private final Set<String> names = new HashSet<>();
    private Set<Value> allowed;

    /**
     * Creates a resolver.
     *
     * @param namespace the namespace of the properties it creates
     * @param extract the summary of the renderer's {@code extractRenderState}, or null if there is none
     * @param reserved how many properties the entity already uses for other things
     */
    public StateProperties(@NotNull String namespace, @Nullable Summary extract, int reserved) {
        this.namespace = namespace;
        this.extract = extract;
        this.reserved = reserved;

        // State objects such as an AnimationState are copied into the render state rather than assigned.
        for (Summary.Invocation invocation : extract == null ? List.<Summary.Invocation>of() : extract.invocations()) {
            if (invocation.call() instanceof Value.InstanceCall(String _, String name, Value.InstanceField(Value receiver, String _, String field), List<Value> args)
                && name.equals("copyFrom") && EXTRACTED.equals(receiver) && args.size() == 1) {
                this.copies.put(field, args.getFirst());
            }
        }
    }

    @Override
    public String resolve(@NotNull String field) {
        return RenderStateQueries.query(field);
    }

    @Override
    public String property(@NotNull Value value, @Nullable String name) {
        StateProperty existing = this.properties.get(value);
        if (existing != null) {
            return query(existing);
        }

        if (this.extract == null || this.allowed != null && !this.allowed.contains(value)
            || Values.contains(value, Value.This.class::isInstance) || !Values.contains(value, STATE::equals)) {
            return null;
        }

        Value fromEntity = this.fromEntity(value);
        if (fromEntity == null) {
            return null;
        }

        StateProperty property = new StateProperty(this.namespace + ":" + this.name(value, name), fromEntity);
        this.properties.put(value, property);
        return query(property);
    }

    @Override
    public @NonNull List<StateProperty> properties() {
        return List.copyOf(this.properties.values());
    }

    /**
     * Checks whether an animation uses more properties than an entity may have.
     *
     * @param used the properties the animation uses
     * @return whether it does
     */
    public boolean overLimit(@NotNull List<StateProperty> used) {
        return used.size() + this.reserved > LIMIT;
    }

    /**
     * Allows only as many of the used properties as an entity may have, for compiling again. Whatever
     * reads the rest is left out of the animation.
     *
     * @param used the properties the animation uses, in the order they were first needed
     */
    public void restrict(@NotNull List<StateProperty> used) {
        Set<String> keep = new HashSet<>();
        for (StateProperty property : used.subList(0, Math.max(0, Math.min(used.size(), LIMIT - this.reserved)))) {
            keep.add(property.id());
        }

        this.allowed = new HashSet<>();
        this.properties.forEach((value, property) -> {
            if (keep.contains(property.id())) {
                this.allowed.add(value);
            }
        });

        this.properties.clear();
        this.names.clear();
    }

    /**
     * Rewrites a value read from the render state as one computed from the entity.
     *
     * @param value the value, reading the render state as {@link Value.Parameter} zero
     * @return the value, reading the entity as {@link Value.Parameter} zero, or null if the renderer
     *         doesn't fill in everything it reads
     */
    @Nullable
    public Value fromEntity(@NotNull Value value) {
        if (this.extract == null) {
            return null;
        }

        Value fromEntity = this.substitute(value);
        return fromEntity == null || Values.contains(fromEntity, EXTRACTED::equals) ? null : fromEntity;
    }

    @Nullable
    private Value substitute(@NotNull Value value) {
        boolean[] failed = {false};
        Value mapped = Values.map(value, node -> {
            if (node instanceof Value.InstanceField(Value receiver, String _, String field) && STATE.equals(receiver)) {
                Value assigned = this.extract.assigned(EXTRACTED, field);
                if (assigned == null) {
                    assigned = this.copies.get(field);
                }

                if (assigned == null) {
                    assigned = VANILLA.get(field);
                }

                if (assigned == null) {
                    failed[0] = true;
                    return node;
                }

                // Animations are posed at the latest tick, so interpolation lands on its end.
                return Values.map(assigned, inner -> PARTIAL_TICK.equals(inner) ? Value.literal(1f) : inner);
            }
            return node;
        });
        return failed[0] ? null : mapped;
    }

    @NotNull
    private String name(@NotNull Value value, @Nullable String hint) {
        String source = hint != null ? hint : value instanceof Value.InstanceField(Value _, String _, String field) ? field : null;
        String base = source == null ? "state" : AnimationCompiler.snake(source);
        String name = base;
        for (int suffix = 2; !this.names.add(name); suffix++) {
            name = base + "_" + suffix;
        }

        return name;
    }

    @NotNull
    private static String query(@NotNull StateProperty property) {
        return "query.property('" + property.id() + "')";
    }
}
