package org.geysermc.hydraulic.entity.geckolib;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resources.Identifier;
import org.geysermc.conduit.ConduitContext;
import org.geysermc.conduit.Conduits;
import org.geysermc.conduit.Summary;
import org.geysermc.conduit.Value;
import org.geysermc.conduit.VirtualMachine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Finds the geometry, texture and animations a GeckoLib renderer draws its entity with.
 */
public final class GeckoLibScanner {
    private static final String GEO_ENTITY_RENDERER = "com/geckolib/renderer/GeoEntityRenderer";
    private static final String DEFAULT_ANIMATIONS = "com/geckolib/constant/DefaultAnimations";
    private static final String WITH_PREFIX = "withPrefix";
    private static final String WITH_PATH = "withPath";

    private GeckoLibScanner() {
    }

    /**
     * Reads a renderer as a GeckoLib one.
     *
     * @param vm the machine the renderer was captured by
     * @param context the context the renderer was read from
     * @param id the entity type the renderer draws
     * @param renderer the renderer, as constructed by its provider
     * @param logger the logger to report what could not be found to
     * @return the entity, or null if the renderer is not a GeckoLib one or its assets could not be found
     */
    @Nullable
    public static GeckoLibEntity scan(@NotNull VirtualMachine vm, @NotNull ConduitContext context, @NotNull Identifier id,
                                      @NotNull Value.Constructed renderer, @NotNull Logger logger) {
        Summary constructor = vm.summarize(renderer);
        Value model = constructor == null ? null : constructor.assignments().stream()
                .filter(assignment -> assignment.className().equals(GEO_ENTITY_RENDERER) && assignment.field().equals("model"))
                .map(Summary.Assignment::value)
                .findFirst().orElse(null);
        if (model == null) {
            return null;
        }

        Identifier geometry = null;
        Identifier texture = null;
        Identifier animations = null;
        if (model instanceof Value.Constructed constructed) {
            Summary fields = vm.summarize(constructed);
            geometry = resource(vm, context, constructed.className(), fields, "getModelResource");
            texture = resource(vm, context, constructed.className(), fields, "getTextureResource");
            animations = resource(vm, context, constructed.className(), fields, "getAnimationResource");
        }

        // A renderer given only its entity type uses a DefaultedEntityGeoModel named after it.
        geometry = geometry == null ? id.withPrefix("entity/") : geometry;
        texture = texture == null ? id.withPath(path -> "textures/entity/" + path + ".png") : texture;
        animations = animations == null ? geometry : animations;

        JsonObject geo = read(context, geometry, "models/", ".geo.json");
        if (geo == null) {
            logger.debug("Could not find the GeckoLib model {} that {} is drawn with", geometry, id);
            return null;
        }

        JsonObject animation = read(context, animations, "animations/", ".animation.json");
        if (animation == null) {
            animation = new JsonObject();
            animation.add(GeckoLibEntity.ANIMATIONS, new JsonObject());
        }

        logger.debug("GeckoLib renderer for {}: model={} texture={} animations={}", id, geometry, texture, animations);
        return new GeckoLibEntity(id, geo, texture.toString(), animation, heads(vm, renderer.className()));
    }

    @Nullable
    private static Identifier resource(@NotNull VirtualMachine vm, @NotNull ConduitContext context, @NotNull String modelClass,
                                       @Nullable Summary fields, @NotNull String getter) {
        Summary summary = vm.summarize(modelClass, getter, null);
        Value returned = summary == null ? null : summary.returned();
        if (returned instanceof Value.InstanceField(Value.This _, String _, String field) && fields != null) {
            returned = fields.assigned(new Value.This(), field);
        }

        return identifier(returned, context);
    }

    @Nullable
    private static Identifier identifier(@Nullable Value value, @NotNull ConduitContext context) {
        if (value instanceof Value.InstanceCall(String _, String name, Value base, List<Value> args)
                && args.size() == 1 && (name.equals(WITH_PREFIX) || name.equals(WITH_PATH))) {
            Identifier identifier = identifier(base, context);
            String part = string(args.getFirst(), context);
            if (identifier == null || part == null) {
                return null;
            }

            return name.equals(WITH_PREFIX) ? identifier.withPrefix(part) : identifier.withPath(part);
        }

        org.geysermc.hydraulic.entity.conduit.Identifier conduit = value == null ? null
                : Conduits.as(value, org.geysermc.hydraulic.entity.conduit.Identifier.class, context);
        return conduit == null || conduit.namespace() == null || conduit.path() == null ? null
                : Identifier.tryBuild(conduit.namespace(), conduit.path());
    }

    @Nullable
    private static String string(@NotNull Value value, @NotNull ConduitContext context) {
        return switch (value) {
            case Value.Literal(String text) -> text;
            case Value.Operation(Value.Operator operator, List<Value> parts) when operator == Value.Operator.CONCAT -> {
                StringBuilder joined = new StringBuilder();
                for (Value part : parts) {
                    String text = string(part, context);
                    if (text == null) {
                        yield null;
                    }
                    joined.append(text);
                }

                yield joined.toString();
            }

            case Value.InstanceCall(String _, String name, Value base, List<Value> args) when args.isEmpty() && name.equals("getPath") -> {
                Identifier identifier = identifier(base, context);
                yield identifier == null ? null : identifier.getPath();
            }
            default -> null;
        };
    }

    @NotNull
    private static List<String> heads(@NotNull VirtualMachine vm, @NotNull String rendererClass) {
        Summary summary = vm.summarize(rendererClass, "adjustModelBonesForRender", null);
        List<String> heads = new ArrayList<>();
        for (Summary.Invocation invocation : summary == null ? List.<Summary.Invocation>of() : summary.invocations()) {
            if (invocation.call() instanceof Value.InstanceCall(String _, String _, Value.InstanceCall(String _, String _, Value _, List<Value> lookup), List<Value> args)
                    && args.size() == 1 && args.getFirst() instanceof Value.Lambda lambda && DEFAULT_ANIMATIONS.equals(lambda.implOwner())
                    && lookup.size() == 1 && lookup.getFirst() instanceof Value.Literal(String bone) && !heads.contains(bone)) {
                heads.add(bone);
            }
        }

        return heads;
    }

    @Nullable
    private static JsonObject read(@NotNull ConduitContext context, @NotNull Identifier id, @NotNull String folder, @NotNull String extension) {
        String path = id.getPath();
        path = path.startsWith("geckolib/") ? path.substring("geckolib/".length()) : path;
        path = path.startsWith(folder) ? path.substring(folder.length()) : path;
        path = path.endsWith(extension) ? path.substring(0, path.length() - extension.length()) : path;

        try (InputStream stream = context.classLoader().getResourceAsStream("assets/" + id.getNamespace() + "/geckolib/" + folder + path + extension)) {
            return stream == null ? null : JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }
}
