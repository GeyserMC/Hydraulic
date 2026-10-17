package org.geysermc.hydraulic.entity.vanilla;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.geysermc.conduit.Summary;
import org.geysermc.conduit.Value;
import org.geysermc.hydraulic.entity.JavaEntityModel;
import org.geysermc.hydraulic.entity.animation.RenderStateDefaults;
import org.geysermc.hydraulic.entity.animation.Values;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Vanilla's entity model layers and model code, read from the client jar when Hydraulic was built,
 * for mods that reuse them. The client jar isn't on a server, so this is the only way to see them.
 */
public final class VanillaModels {
    public static final String RESOURCE = "hydraulic/vanilla-models.json";
    public static final String VANILLA_TEXTURES = "vanilla/textures/";
    public static final String MODEL_LAYERS = "net/minecraft/client/model/geom/ModelLayers";
    private static final String CONSTRUCTOR = "<init>";

    private static final Logger LOGGER = LoggerFactory.getLogger(VanillaModels.class);
    private static final Gson GSON = new Gson();
    private static volatile VanillaModels instance;

    private final JsonObject layers;
    private final JsonObject locations;
    private final JsonObject models;
    private final JsonObject states;
    private final Map<String, Summary> summaries = new ConcurrentHashMap<>();
    private final Map<String, RenderStateDefaults.StateClass> renderStates = new ConcurrentHashMap<>();

    private VanillaModels(@NotNull JsonObject json) {
        this.layers = json.getAsJsonObject("layers");
        this.locations = json.has("locations") ? json.getAsJsonObject("locations") : new JsonObject();
        this.models = json.getAsJsonObject("models");
        this.states = json.has("states") ? json.getAsJsonObject("states") : new JsonObject();
    }

    /**
     * Gets the bundle shipped with Hydraulic.
     *
     * @return the bundle, empty if it is missing
     */
    @NotNull
    public static VanillaModels get() {
        VanillaModels loaded = instance;
        if (loaded == null) {
            synchronized (VanillaModels.class) {
                loaded = instance;
                if (loaded == null) {
                    instance = loaded = load();
                }
            }
        }

        return loaded;
    }

    @NotNull
    private static VanillaModels load() {
        try (InputStream stream = VanillaModels.class.getClassLoader().getResourceAsStream(RESOURCE)) {
            if (stream != null) {
                return new VanillaModels(JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject());
            }
            LOGGER.warn("{} is missing, so mods reusing vanilla entity models won't be converted", RESOURCE);
        } catch (IOException | RuntimeException e) {
            LOGGER.error("Failed to read {}", RESOURCE, e);
        }

        JsonObject empty = new JsonObject();
        empty.add("layers", new JsonObject());
        empty.add("models", new JsonObject());
        return new VanillaModels(empty);
    }

    /**
     * Reads a vanilla texture shipped with Hydraulic.
     *
     * @param path the texture's path within {@code textures}, such as {@code item/egg}, without the extension
     * @return the PNG, or null if it isn't bundled
     */
    public static byte @Nullable [] texture(@NotNull String path) {
        String resource = "hydraulic/" + VANILLA_TEXTURES + (path.endsWith(".png") ? path : path + ".png");
        try (InputStream stream = VanillaModels.class.getClassLoader().getResourceAsStream(resource)) {
            return stream == null ? null : stream.readAllBytes();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Gets the key a layer factory call is stored under.
     *
     * @param owner the internal name of the class declaring the factory
     * @param method the factory method
     * @param args the arguments it was called with
     * @return the key
     */
    @NotNull
    public static String layerKey(@NotNull String owner, @NotNull String method, @NotNull List<Value> args) {
        StringJoiner joined = new StringJoiner(", ", owner + "." + method + "(", ")");
        args.forEach(arg -> joined.add(String.valueOf(arg)));
        return joined.toString();
    }

    /**
     * Finds the geometry a vanilla layer factory builds, falling back to another overload of the same
     * method when it was called with arguments the bundle didn't see.
     *
     * @param call the factory call, such as {@code CowModel.createBodyLayer()}
     * @param namespace the namespace of the layer the geometry is registered as
     * @param path the path of the layer the geometry is registered as
     * @param layer the layer name
     * @return the geometry, or null if it isn't vanilla's
     */
    @Nullable
    public JavaEntityModel layer(@NotNull Value.StaticCall call, @NotNull String namespace, @NotNull String path, @Nullable String layer) {
        JsonElement found = this.layers.get(layerKey(call.className(), call.methodName(), call.args()));
        if (found == null) {
            String prefix = call.className() + "." + call.methodName() + "(";
            for (Map.Entry<String, JsonElement> entry : this.layers.entrySet()) {
                if (entry.getKey().startsWith(prefix)) {
                    found = entry.getValue();
                    break;
                }
            }
        }

        if (found == null) {
            return null;
        }

        JavaEntityModel model = GSON.fromJson(found, JavaEntityModel.class);
        return new JavaEntityModel(namespace, path, layer, model.textureWidth(), model.textureHeight(), model.parts(), model.scale());
    }

    /**
     * Finds the geometry vanilla registers for one of its layer locations.
     *
     * @param field the name of the {@code ModelLayers} field holding the location, such as {@code CREEPER}
     * @return the geometry, or null if it isn't bundled
     */
    @Nullable
    public JavaEntityModel location(@NotNull String field) {
        JsonElement found = this.locations.get(field);
        return found == null ? null : GSON.fromJson(found, JavaEntityModel.class);
    }

    /**
     * Finds the geometry vanilla registers for a layer location.
     *
     * @param namespace the namespace of the location's model
     * @param path the path of the location's model
     * @param layer the layer name
     * @return the geometry, or null if it isn't bundled
     */
    @Nullable
    public JavaEntityModel location(@NotNull String namespace, @NotNull String path, @NotNull String layer) {
        for (Map.Entry<String, JsonElement> entry : this.locations.entrySet()) {
            JavaEntityModel model = GSON.fromJson(entry.getValue(), JavaEntityModel.class);
            if (namespace.equals(model.namespace()) && path.equals(model.path()) && layer.equals(model.layer())) {
                return model;
            }
        }

        return null;
    }

    /**
     * Gets a vanilla model's constructor, as summarized from the client jar.
     *
     * @param modelClass the internal name of the model class
     * @return the summary, or null if the class isn't a vanilla model
     */
    @Nullable
    public Summary constructor(@NotNull String modelClass) {
        return this.summary(modelClass, "constructor");
    }

    /**
     * Gets a vanilla render state class, as read from the client jar.
     *
     * @param stateClass the internal name of the render state class
     * @return the render state class, or null if it isn't a vanilla render state
     */
    @Nullable
    public RenderStateDefaults.StateClass renderState(@NotNull String stateClass) {
        JsonObject state = this.states.getAsJsonObject(stateClass);
        return state == null ? null : this.renderStates.computeIfAbsent(stateClass, _ -> RenderStateDefaults.StateClass.read(state));
    }

    /**
     * Gets a vanilla model's {@code setupAnim}, as summarized from the client jar.
     *
     * @param modelClass the internal name of the model class
     * @return the summary, or null if the class isn't a vanilla model
     */
    @Nullable
    public Summary setupAnim(@NotNull String modelClass) {
        return this.summary(modelClass, "setupAnim");
    }

    /**
     * Inlines a mod model's call to the vanilla model it extends, such as {@code super(root)} or
     * {@code super.setupAnim(state)}, which can't be followed without the client jar.
     *
     * @param summary the summary of the mod model's constructor or {@code setupAnim}
     * @param method the method summarized, either {@code <init>} or {@code setupAnim}
     * @return the summary with the vanilla call's effects applied before its own
     */
    @NotNull
    public Summary inlineSuperCall(@NotNull Summary summary, @NotNull String method) {
        for (Summary.Invocation invocation : summary.invocations()) {
            if (invocation.condition() != null
                    || !(invocation.call() instanceof Value.InstanceCall(String owner, String name, Value.This _, List<Value> args))
                    || !name.equals(method)) {
                continue;
            }

            Summary parent = this.summary(owner, method.equals(CONSTRUCTOR) ? "constructor" : method);
            if (parent == null) {
                continue;
            }

            Map<String, Value> assigned = new HashMap<>();
            List<Summary.Assignment> assignments = new ArrayList<>();
            for (Summary.Assignment assignment : parent.assignments()) {
                Value target = assignment.target() == null ? null : withArguments(assignment.target(), args);
                Value value = withArguments(assignment.value(), args);
                assignments.add(new Summary.Assignment(target, assignment.className(), assignment.field(), value));

                String key = fieldKey(target, assignment.field());
                if (key != null) {
                    assigned.put(key, value);
                }
            }

            for (Summary.Assignment assignment : summary.assignments()) {
                String key = fieldKey(assignment.target(), assignment.field());
                assignments.removeIf(inherited -> key != null && key.equals(fieldKey(inherited.target(), inherited.field())));
                assignments.add(new Summary.Assignment(assignment.target(), assignment.className(), assignment.field(),
                        Values.map(assignment.value(), read -> readInherited(read, assigned)))
                );
            }

            List<Summary.Invocation> invocations = new ArrayList<>();
            for (Summary.Invocation inherited : parent.invocations()) {
                invocations.add(new Summary.Invocation(inherited.condition() == null ? null : withArguments(inherited.condition(), args),
                        withArguments(inherited.call(), args))
                );
            }

            summary.invocations().stream().filter(other -> other != invocation).forEach(invocations::add);
            return new Summary(summary.returned(), assignments, invocations);
        }

        return summary;
    }

    @NotNull
    private static Value withArguments(@NotNull Value value, @NotNull List<Value> args) {
        return Values.map(value, inner -> inner instanceof Value.Parameter(int index) && index < args.size() ? args.get(index) : inner);
    }

    @NotNull
    private static Value readInherited(@NotNull Value value, @NotNull Map<String, Value> assigned) {
        if (!(value instanceof Value.InstanceField(Value receiver, String _, String field))) {
            return value;
        }

        String key = fieldKey(receiver, field);
        return key != null && assigned.containsKey(key) ? assigned.get(key) : value;
    }

    @Nullable
    private static String fieldKey(@Nullable Value target, @NotNull String field) {
        String receiver = switch (target) {
            case Value.This _ -> "this";
            case Value.InstanceField(Value inner, String _, String name) -> fieldKey(inner, name);
            case null, default -> null;
        };

        return receiver == null ? null : receiver + "." + field;
    }

    @Nullable
    private Summary summary(@NotNull String modelClass, @NotNull String method) {
        String internal = modelClass.replace('.', '/');
        JsonObject model = this.models.getAsJsonObject(internal);
        if (model == null || !model.has(method)) {
            return null;
        }

        return this.summaries.computeIfAbsent(internal + "#" + method, _ -> SummaryCodec.read(model.getAsJsonObject(method)));
    }
}
