package org.geysermc.hydraulic.entity.geckolib;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.Identifier;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.bedrock.resource.entity.ClientEntity;
import org.geysermc.pack.bedrock.resource.entity.Entity;
import org.geysermc.pack.bedrock.resource.entity.cliententity.Description;
import org.geysermc.pack.bedrock.resource.entity.cliententity.description.Scripts;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Writes a GeckoLib entity into a Bedrock resource pack, playing its animations from the entity
 * properties {@link GeckoLibAnimator} sends.
 */
public final class GeckoLibPack {

    /**
     * Writes an entity.
     *
     * @param pack the pack to write to
     * @param entity the entity
     * @param texture the texture it is drawn with, as a path in the pack without its extension
     */
    public static void write(@NotNull BedrockResourcePack pack, @NotNull GeckoLibEntity entity, @NotNull String texture) {
        String path = entity.id().getPath();
        JsonObject animations = animations(entity);
        JsonObject controllers = controllers(entity);
        pack.addExtraFile(geometry(entity), "models/entity/" + path + ".geo.json");
        pack.addExtraFile(animations, "animations/" + path + ".animation.json");
        pack.addExtraFile(controllers, "animation_controllers/" + path + ".animation_controllers.json");

        // The entity refers to each animation and controller by the last part of its identifier
        Map<String, String> names = new LinkedHashMap<>();
        List<Map<String, String>> animate = new ArrayList<>();
        for (String id : animations.getAsJsonObject("animations").keySet()) {
            names.put(id.substring(id.lastIndexOf('.') + 1), id);
        }

        for (String id : controllers.getAsJsonObject("animation_controllers").keySet()) {
            String name = id.substring(id.lastIndexOf('.') + 1);
            names.put(name, id);
            animate.add(Map.of(name, "1"));
        }

        if (names.containsKey("look_at")) {
            animate.add(Map.of("look_at", "1"));
        }

        Description description = new Description();
        description.identifier(entity.id().toString());
        description.materials(Map.of("default", "entity_alphatest"));
        description.textures(Map.of("default", texture));
        description.geometry(Map.of("default", "geometry." + name(entity.id())));
        description.renderControllers(new String[] { "controller.render.default" });
        description.animations(names);

        Scripts scripts = new Scripts();
        scripts.animate(animate);
        description.scripts(scripts);

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.description(description);

        Entity file = new Entity();
        file.formatVersion("1.10.0");
        file.clientEntity(clientEntity);
        pack.addClientEntity(file, "entity/" + path + ".entity.json");
    }

    /**
     * Gets the identifier of the entity property a controller's state is sent in.
     *
     * @param id the entity type's identifier
     * @param slot the controller's index
     * @return the property's identifier
     */
    @NotNull
    public static String property(@NotNull Identifier id, int slot) {
        return id.getNamespace() + ":geckolib_" + slot;
    }

    @NotNull
    static JsonObject geometry(@NotNull GeckoLibEntity entity) {
        JsonObject first = entity.geometry().getAsJsonArray("minecraft:geometry").get(0).getAsJsonObject().deepCopy();
        first.getAsJsonObject("description").addProperty("identifier", "geometry." + name(entity.id()));
        if (first.get("bones") instanceof JsonArray bones) {
            for (JsonElement bone : bones) {
                JsonObject object = bone.getAsJsonObject();
                for (String key : List.of("name", "parent")) {
                    if (object.has(key)) {
                        object.addProperty(key, sanitizeBone(object.get(key).getAsString()));
                    }
                }
            }
        }

        JsonArray geometries = new JsonArray();
        geometries.add(first);

        JsonObject file = new JsonObject();
        file.add("format_version", entity.geometry().get("format_version"));
        file.add("minecraft:geometry", geometries);
        return file;
    }

    @NotNull
    static JsonObject animations(@NotNull GeckoLibEntity entity) {
        String name = name(entity.id());
        JsonObject source = entity.animations().getAsJsonObject("animations");
        JsonObject animations = new JsonObject();
        for (int i = 0; i < entity.clips().size(); i++) {
            JsonObject converted = source.getAsJsonObject(entity.clips().get(i).name()).deepCopy();
            if (converted.get("bones") instanceof JsonObject bones) {
                converted.add("bones", bones(bones));
            }

            for (GeckoLibEntity.Loop loop : GeckoLibEntity.Loop.values()) {
                JsonObject animation = converted.deepCopy();
                animation.add("loop", loop.json());
                animations.add("animation." + name + ".clip" + GeckoLibEntity.state(i, loop), animation);
            }
        }

        if (!entity.heads().isEmpty()) {
            JsonArray rotation = new JsonArray();
            rotation.add("query.target_x_rotation");
            rotation.add("query.target_y_rotation");
            rotation.add(0);

            JsonObject bones = new JsonObject();
            for (String head : entity.heads()) {
                JsonObject bone = new JsonObject();
                bone.add("rotation", rotation);
                bones.add(sanitizeBone(head), bone);
            }

            JsonObject lookAt = new JsonObject();
            lookAt.addProperty("loop", true);
            lookAt.add("bones", bones);
            animations.add("animation." + name + ".look_at", lookAt);
        }

        JsonObject file = new JsonObject();
        file.addProperty("format_version", "1.8.0");
        file.add("animations", animations);
        return file;
    }

    @NotNull
    static JsonObject controllers(@NotNull GeckoLibEntity entity) {
        JsonObject controllers = new JsonObject();
        for (int slot = 0; slot < GeckoLibEntity.CONTROLLERS; slot++) {
            String property = "query.property('" + property(entity.id(), slot) + "')";

            JsonArray animations = new JsonArray();
            for (int state = 1; state < entity.states(); state++) {
                JsonObject animation = new JsonObject();
                animation.addProperty("clip" + state, "math.floor(" + property + " / 2) == " + state);
                animations.add(animation);
            }

            JsonObject states = new JsonObject();
            states.add("a", state(animations, "b", "math.mod(" + property + ", 2) == 1"));
            states.add("b", state(animations, "a", "math.mod(" + property + ", 2) == 0"));

            JsonObject controller = new JsonObject();
            controller.addProperty("initial_state", "a");
            controller.add("states", states);
            controllers.add("controller.animation." + name(entity.id()) + ".geckolib_" + slot, controller);
        }

        JsonObject file = new JsonObject();
        file.addProperty("format_version", "1.10.0");
        file.add("animation_controllers", controllers);
        return file;
    }

    @NotNull
    private static JsonObject state(@NotNull JsonArray animations, @NotNull String next, @NotNull String when) {
        JsonObject transition = new JsonObject();
        transition.addProperty(next, when);
        JsonArray transitions = new JsonArray();
        transitions.add(transition);

        JsonObject state = new JsonObject();
        state.add("animations", animations);
        state.add("transitions", transitions);
        return state;
    }

    @NotNull
    private static JsonObject bones(@NotNull JsonObject source) {
        JsonObject bones = new JsonObject();
        for (Map.Entry<String, JsonElement> bone : source.entrySet()) {
            JsonObject channels = new JsonObject();
            for (Map.Entry<String, JsonElement> channel : bone.getValue().getAsJsonObject().entrySet()) {
                channels.add(channel.getKey(), channel(channel.getValue()));
            }

            bones.add(sanitizeBone(bone.getKey()), channels);
        }

        return bones;
    }

    @NotNull
    private static JsonElement channel(@NotNull JsonElement channel) {
        if (!(channel instanceof JsonObject timeline) || timeline.has("vector") || timeline.has("pre") || timeline.has("post")) {
            return keyframe(channel);
        }

        JsonObject converted = new JsonObject();
        timeline.entrySet().forEach(keyframe -> converted.add(keyframe.getKey(), keyframe(keyframe.getValue())));
        return converted;
    }

    @NotNull
    private static JsonElement keyframe(@NotNull JsonElement keyframe) {
        if (!(keyframe instanceof JsonObject object) || !object.has("pre") && !object.has("post") && !object.has("lerp_mode")) {
            return vector(keyframe);
        }

        // GeckoLib wraps keyframe values as {"vector": [...]} which Bedrock does not read
        JsonObject converted = new JsonObject();
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            switch (entry.getKey()) {
                case "pre", "post", "lerp_mode" -> converted.add(entry.getKey(), vector(entry.getValue()));
                case "vector" -> {
                    if (!object.has("post")) {
                        converted.add("post", entry.getValue());
                    }
                }

                default -> {
                }
            }
        }

        return converted;
    }

    @NotNull
    private static JsonElement vector(@NotNull JsonElement value) {
        return value instanceof JsonObject object && object.has("vector") ? object.get("vector") : value;
    }

    @NotNull
    private static String sanitizeBone(@NotNull String name) {
        return name.replaceAll("[^A-Za-z0-9_.]", "_");
    }

    @NotNull
    private static String name(@NotNull Identifier id) {
        return id.getNamespace() + "." + id.getPath().replace('/', '.');
    }
}
