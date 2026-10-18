package org.geysermc.hydraulic.entity.animation;

import com.google.gson.JsonObject;
import org.geysermc.conduit.ConduitContext;
import org.geysermc.conduit.Summary;
import org.geysermc.conduit.Value;
import org.geysermc.conduit.VirtualMachine;
import org.geysermc.hydraulic.entity.vanilla.SummaryCodec;
import org.geysermc.hydraulic.entity.vanilla.VanillaModels;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.FieldModel;
import java.lang.classfile.constantpool.ClassEntry;
import java.lang.reflect.AccessFlag;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Gets the value a render state field holds while its entity is idle, for fields nothing sends to Bedrock.
 * <p>
 * This represents what the render state's constructor assigns, or the field's zero value, unless {@link #IDLE}
 * says an idle entity extracts something else.
 */
public final class RenderStateDefaults {
    private static final String CONSTRUCTOR = "<init>";
    private static final String NO_ARGS = "()V";
    private static final String ARMED_STATE = "net/minecraft/client/renderer/entity/state/ArmedEntityRenderState";
    private static final String ILLAGER_STATE = "net/minecraft/client/renderer/entity/state/IllagerRenderState";

    private static final Map<String, Value> IDLE = Map.of(
            ILLAGER_STATE + "#armPose", new Value.Constructed(
                    "net/minecraft/world/entity/monster/illager/AbstractIllager$IllagerArmPose",
                    List.of(Value.literal("CROSSED"), Value.literal(0))
            ),
            ARMED_STATE + "#getMainHandItemState().isEmpty", Value.literal(true)
    );

    private final Function<String, StateClass> classes;
    private final Map<String, Optional<StateClass>> cache = new ConcurrentHashMap<>();

    private RenderStateDefaults(@NotNull Function<String, StateClass> classes) {
        this.classes = classes;
    }

    /**
     * Creates defaults reading vanilla's render states from the bundle Hydraulic ships.
     *
     * @return the defaults
     */
    @NotNull
    public static RenderStateDefaults vanilla() {
        return new RenderStateDefaults(name -> VanillaModels.get().renderState(name));
    }

    /**
     * Creates defaults reading vanilla's render states from the bundle, and a mod's own from its jar.
     *
     * @param context the context holding the mod's jar
     * @param vm the virtual machine summarizing the mod's render states
     * @return the defaults
     */
    @NotNull
    public static RenderStateDefaults of(@NotNull ConduitContext context, @NotNull VirtualMachine vm) {
        return new RenderStateDefaults(name -> {
            StateClass vanilla = VanillaModels.get().renderState(name);
            return vanilla != null ? vanilla : read(context, vm, name);
        });
    }

    /**
     * Gets the value an idle entity's render state holds for a field, or for a call on it such as
     * {@code getMainHandItemState().isEmpty}.
     *
     * @param owner the internal name of the class the field or call is read through
     * @param member the field, or the calls made
     * @return the value, or null if it isn't known
     */
    @Nullable
    public Value idle(@NotNull String owner, @NotNull String member) {
        List<String> chain = this.chain(owner);
        for (String type : chain) {
            Value idle = IDLE.get(type + "#" + member);
            if (idle != null) {
                return idle;
            }
        }

        for (String type : chain) {
            StateClass state = this.state(type);
            if (state == null) {
                return null;
            }

            Value assigned = state.constructor() == null ? null : state.constructor().assigned(new Value.This(), member);
            if (assigned != null) {
                return assigned;
            }

            String descriptor = state.fields().get(member);
            if (descriptor != null) {
                return zero(descriptor);
            }
        }

        return null;
    }

    @NotNull
    private List<String> chain(@NotNull String owner) {
        List<String> chain = new ArrayList<>();
        for (String type = owner; type != null && !chain.contains(type); ) {
            chain.add(type);
            StateClass state = this.state(type);
            type = state == null ? null : state.superName();
        }

        return chain;
    }

    @Nullable
    private StateClass state(@NotNull String type) {
        return this.cache.computeIfAbsent(type, name -> Optional.ofNullable(this.classes.apply(name))).orElse(null);
    }

    @NotNull
    private static Value zero(@NotNull String descriptor) {
        return switch (descriptor.charAt(0)) {
            case 'Z' -> Value.literal(false);
            case 'L', '[' -> Value.literal(null);
            default -> Value.literal(0);
        };
    }

    @Nullable
    private static StateClass read(@NotNull ConduitContext context, @NotNull VirtualMachine vm, @NotNull String name) {
        try (InputStream in = context.classLoader().getResourceAsStream(name + ".class")) {
            if (in == null) {
                return null;
            }

            return StateClass.of(ClassFile.of().parse(in.readAllBytes()), summarize(vm, name));
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }

    /**
     * Summarizes a render state's constructor.
     *
     * @param vm the virtual machine
     * @param name the internal name of the render state
     * @return the summary, or null if it couldn't be summarized
     */
    @Nullable
    public static Summary summarize(@NotNull VirtualMachine vm, @NotNull String name) {
        Summary constructor = vm.summarize(name, CONSTRUCTOR, NO_ARGS);
        return constructor != null ? constructor : vm.summarize(name, CONSTRUCTOR, null);
    }

    /**
     * A render state class, as far as its idle values go.
     *
     * @param superName the internal name of the class it extends, or null for none
     * @param fields the descriptor of each instance field it declares
     * @param constructor its constructor, or null if it couldn't be summarized
     */
    public record StateClass(@Nullable String superName, @NotNull Map<String, String> fields, @Nullable Summary constructor) {

        /**
         * Reads a render state class from its class file.
         *
         * @param model the class file
         * @param constructor its constructor
         * @return the render state class
         */
        @NotNull
        public static StateClass of(@NotNull ClassModel model, @Nullable Summary constructor) {
            Map<String, String> fields = new LinkedHashMap<>();
            for (FieldModel field : model.fields()) {
                if (!field.flags().has(AccessFlag.STATIC)) {
                    fields.put(field.fieldName().stringValue(), field.fieldType().stringValue());
                }
            }

            String superName = model.superclass().map(ClassEntry::asInternalName).orElse(null);
            return new StateClass(superName, fields, constructor);
        }

        /**
         * Reads a render state class from the bundle.
         *
         * @param json the bundled render state class
         * @return the render state class
         */
        @NotNull
        public static StateClass read(@NotNull JsonObject json) {
            Map<String, String> fields = new LinkedHashMap<>();
            json.getAsJsonObject("fields").entrySet().forEach(entry -> fields.put(entry.getKey(), entry.getValue().getAsString()));
            return new StateClass(
                    json.has("super") ? json.get("super").getAsString() : null, fields,
                    json.has("constructor") ? SummaryCodec.read(json.getAsJsonObject("constructor")) : null
            );
        }

        /**
         * Writes this render state class for the bundle.
         *
         * @return the JSON
         */
        @NotNull
        public JsonObject write() {
            JsonObject json = new JsonObject();
            if (this.superName != null) {
                json.addProperty("super", this.superName);
            }

            JsonObject fields = new JsonObject();
            this.fields.forEach(fields::addProperty);
            json.add("fields", fields);
            if (this.constructor != null) {
                json.add("constructor", SummaryCodec.write(this.constructor));
            }

            return json;
        }
    }
}
