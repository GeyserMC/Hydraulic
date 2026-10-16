package org.geysermc.hydraulic.entity.runtime;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.geysermc.conduit.Value;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.api.entity.property.type.GeyserFloatEntityProperty;
import org.geysermc.geyser.api.entity.property.type.GeyserIntEntityProperty;
import org.geysermc.geyser.session.GeyserSession;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.ToIntFunction;

/**
 * Sends Bedrock players the entity properties custom entities' animations and textures read, worked
 * out each tick from the server's own entities.
 */
public final class PropertySync {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertySync.class);
    private static final double RANGE = 128;
    public static final float MIN = -10000f;
    public static final float MAX = 10000f;

    private final ValueEvaluator evaluator = new ValueEvaluator();
    private final Map<Identifier, Binding> bindings = new ConcurrentHashMap<>();
    private final Set<String> failures = ConcurrentHashMap.newKeySet();
    private final Map<GeyserSession, Map<org.geysermc.geyser.entity.type.Entity, Values>> sent = Collections.synchronizedMap(new WeakHashMap<>());

    /**
     * Stops sending anything for an entity type, before its properties are registered again.
     *
     * @param type the entity type
     */
    public void clear(@NotNull Identifier type) {
        this.bindings.remove(type);
    }

    /**
     * Repeats calls an entity type only makes on the client before working out its properties.
     *
     * @param type the entity type
     * @param calls the calls
     */
    public void addClientCalls(@NotNull Identifier type, @NotNull List<ClientCall> calls) {
        this.bindings.computeIfAbsent(type, _ -> new Binding()).calls.addAll(calls);
    }

    /**
     * Sends a float property, computed from the entity.
     *
     * @param type the entity type
     * @param property the property, as registered with Geyser
     * @param value computes the property from the entity
     */
    public void addProperty(@NotNull Identifier type, @NotNull GeyserFloatEntityProperty property, @NotNull Value value) {
        this.bindings.computeIfAbsent(type, _ -> new Binding()).floats.add(new FloatProperty(property, value));
    }

    /**
     * Sends an int property, computed from the entity by a function rather than a summarized value.
     *
     * @param type the entity type
     * @param property the property, as registered with Geyser
     * @param value computes the property from the entity, on the server thread
     */
    public void addProperty(@NotNull Identifier type, @NotNull GeyserIntEntityProperty property, @NotNull ToIntFunction<Entity> value) {
        this.bindings.computeIfAbsent(type, _ -> new Binding()).ints.add(new IntProperty(property, value));
    }

    /**
     * Sends which texture an entity is drawn with, as an index into its textures.
     *
     * @param type the entity type
     * @param property the property, as registered with Geyser
     * @param texture computes the texture's identifier from the entity
     * @param textures the textures, in the order their indices refer to
     */
    public void setVariant(@NotNull Identifier type, @NotNull GeyserIntEntityProperty property, @NotNull Value texture, @NotNull List<String> textures) {
        this.bindings.computeIfAbsent(type, _ -> new Binding()).variant = new Variant(property, texture, List.copyOf(textures));
    }

    /**
     * Works out and sends the properties of every custom entity near a Bedrock player.
     *
     * @param server the server
     */
    public void tick(@NotNull MinecraftServer server) {
        if (this.bindings.isEmpty()) {
            return;
        }

        Map<Entity, Values> evaluated = new HashMap<>();
        for (GeyserSession session : GeyserImpl.getInstance().onlineConnections()) {
            UUID uuid = session.javaUuid();
            ServerPlayer player = uuid == null ? null : server.getPlayerList().getPlayer(uuid);
            if (player == null) {
                continue;
            }

            Map<Integer, Values> nearby = new HashMap<>();
            for (Entity entity : player.level().getEntities((Entity) null, player.getBoundingBox().inflate(RANGE),
                    candidate -> this.bindings.containsKey(BuiltInRegistries.ENTITY_TYPE.getKey(candidate.getType())))) {
                nearby.put(entity.getId(), evaluated.computeIfAbsent(entity, this::evaluate));
            }

            if (!nearby.isEmpty()) {
                session.ensureInEventLoop(() -> this.send(session, nearby));
            }
        }
    }

    @NotNull
    private Values evaluate(@NotNull Entity entity) {
        Identifier type = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        Binding binding = this.bindings.get(type);

        for (int i = 0; i < binding.calls.size(); i++) {
            ClientCall call = binding.calls.get(i);
            Object made = call.condition() == null ? Boolean.TRUE : this.attempt(type, "client call " + i, call.condition(), entity, true);
            if (made != null && ValueEvaluator.number(made) != 0) {
                this.attempt(type, "client call " + i, call.call(), entity, false);
            }
        }

        float[] floats = new float[binding.floats.size()];
        for (int i = 0; i < floats.length; i++) {
            FloatProperty property = binding.floats.get(i);
            Object result = this.attempt(type, property.property().identifier().toString(), property.value(), entity, true);
            floats[i] = result == null ? Float.NaN : Math.clamp((float) ValueEvaluator.number(result), MIN, MAX);
        }

        int[] ints = new int[binding.ints.size()];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = this.attempt(type, binding.ints.get(i), entity);
        }

        int variant = -1;
        if (binding.variant != null) {
            Object texture = this.attempt(type, "texture", binding.variant.texture(), entity, false);
            variant = texture == null ? -1 : binding.variant.textures().indexOf(texture.toString());
        }

        return new Values(binding, floats, ints, variant);
    }

    @Nullable
    private Object attempt(@NotNull Identifier type, @NotNull String what, @NotNull Value value, @NotNull Entity entity, boolean numeric) {
        String key = type + "/" + what;
        if (this.failures.contains(key)) {
            return null;
        }

        try {
            Object result = this.evaluator.evaluate(value, entity);
            if (numeric) {
                ValueEvaluator.number(result);
            }

            return result;
        } catch (RuntimeException | LinkageError e) {
            if (this.failures.add(key)) {
                LOGGER.debug("Can't compute {} for {}, so it won't be sent: {}", what, type, e.getMessage());
            }

            return null;
        }
    }

    private int attempt(@NotNull Identifier type, @NotNull IntProperty property, @NotNull Entity entity) {
        String key = type + "/" + property.property().identifier();
        if (this.failures.contains(key)) {
            return -1;
        }

        try {
            return property.value().applyAsInt(entity);
        } catch (RuntimeException | LinkageError e) {
            if (this.failures.add(key)) {
                LOGGER.debug("Can't compute {} for {}, so it won't be sent: {}", property.property().identifier(), type, e.getMessage());
            }

            return -1;
        }
    }

    private void send(@NotNull GeyserSession session, @NotNull Map<Integer, Values> nearby) {
        Map<org.geysermc.geyser.entity.type.Entity, Values> sent = this.sent.computeIfAbsent(session, _ -> new WeakHashMap<>());
        nearby.forEach((id, values) -> {
            org.geysermc.geyser.entity.type.Entity entity = session.getEntityCache().getEntityByJavaId(id);
            if (entity == null) {
                return;
            }

            Values last = sent.get(entity);
            List<FloatProperty> floats = values.binding().floats;
            float[] merged = values.floats().clone();
            List<Integer> changed = new ArrayList<>();
            for (int i = 0; i < merged.length; i++) {
                if (Float.isNaN(merged[i])) {
                    merged[i] = last == null ? Float.NaN : last.floats()[i];
                } else if (last == null || last.floats()[i] != merged[i]) {
                    changed.add(i);
                }
            }

            List<IntProperty> ints = values.binding().ints;
            int[] mergedInts = values.ints().clone();
            List<Integer> changedInts = new ArrayList<>();
            for (int i = 0; i < mergedInts.length; i++) {
                if (mergedInts[i] < 0) {
                    mergedInts[i] = last == null ? -1 : last.ints()[i];
                } else if (last == null || last.ints()[i] != mergedInts[i]) {
                    changedInts.add(i);
                }
            }

            Variant variant = values.binding().variant;
            boolean variantChanged = variant != null && values.variant() >= 0 && (last == null || last.variant() != values.variant());
            if (changed.isEmpty() && changedInts.isEmpty() && !variantChanged) {
                return;
            }

            entity.updatePropertiesBatched(updater -> {
                for (int i : changed) {
                    updater.update(floats.get(i).property(), merged[i]);
                }
                for (int i : changedInts) {
                    updater.update(ints.get(i).property(), mergedInts[i]);
                }
                if (variantChanged) {
                    updater.update(variant.property(), values.variant());
                }
            }, false);

            sent.put(entity, new Values(values.binding(), merged, mergedInts, variantChanged || last == null ? values.variant() : last.variant()));
        });
    }

    private static final class Binding {
        private final List<FloatProperty> floats = new CopyOnWriteArrayList<>();
        private final List<IntProperty> ints = new CopyOnWriteArrayList<>();
        private final List<ClientCall> calls = new CopyOnWriteArrayList<>();
        private volatile Variant variant;
    }

    private record FloatProperty(GeyserFloatEntityProperty property, Value value) {
    }

    private record IntProperty(GeyserIntEntityProperty property, ToIntFunction<Entity> value) {
    }

    private record Variant(GeyserIntEntityProperty property, Value texture, List<String> textures) {
    }

    private record Values(Binding binding, float[] floats, int[] ints, int variant) {
    }
}
