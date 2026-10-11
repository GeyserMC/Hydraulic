package org.geysermc.hydraulic.entity.vanilla;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.geysermc.conduit.Summary;
import org.geysermc.conduit.Value;
import org.geysermc.hydraulic.entity.conduit.EntityModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(VanillaModels.class);
    private static final Gson GSON = new Gson();
    private static volatile VanillaModels instance;

    private final JsonObject layers;
    private final JsonObject models;
    private final Map<String, Summary> summaries = new ConcurrentHashMap<>();

    private VanillaModels(@NotNull JsonObject json) {
        this.layers = json.getAsJsonObject("layers");
        this.models = json.getAsJsonObject("models");
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
    public EntityModel layer(@NotNull Value.StaticCall call, @NotNull String namespace, @NotNull String path, @Nullable String layer) {
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

        EntityModel model = GSON.fromJson(found, EntityModel.class);
        return new EntityModel(namespace, path, layer, model.textureWidth(), model.textureHeight(), model.parts(), model.scale());
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
     * Gets a vanilla model's {@code setupAnim}, as summarized from the client jar.
     *
     * @param modelClass the internal name of the model class
     * @return the summary, or null if the class isn't a vanilla model
     */
    @Nullable
    public Summary setupAnim(@NotNull String modelClass) {
        return this.summary(modelClass, "setupAnim");
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
