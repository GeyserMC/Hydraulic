package org.geysermc.hydraulic.entity;

import net.minecraft.resources.Identifier;
import org.geysermc.conduit.ConduitContext;
import org.geysermc.conduit.Conduits;
import org.geysermc.conduit.Summary;
import org.geysermc.conduit.Trace;
import org.geysermc.conduit.Value;
import org.geysermc.conduit.VirtualMachine;
import org.geysermc.conduit.VirtualMachineFactory;
import org.geysermc.hydraulic.entity.animation.AnimationCompiler;
import org.geysermc.hydraulic.entity.animation.AnimationCompiler.CompiledAnimation;
import org.geysermc.hydraulic.entity.animation.BoneMap;
import org.geysermc.hydraulic.entity.animation.ModelBinding;
import org.geysermc.hydraulic.entity.animation.StateProperties;
import org.geysermc.hydraulic.entity.animation.Values;
import org.geysermc.hydraulic.entity.conduit.EntityFactory;
import org.geysermc.hydraulic.entity.conduit.EntityModel;
import org.geysermc.hydraulic.entity.conduit.EntityRendererRegistration;
import org.geysermc.hydraulic.entity.conduit.EntityType;
import org.geysermc.hydraulic.entity.conduit.EntityTypeBuilder;
import org.geysermc.hydraulic.entity.conduit.LivingEntityRenderer;
import org.geysermc.hydraulic.entity.conduit.ModelLayerLocation;
import org.geysermc.hydraulic.entity.runtime.ClientCall;
import org.geysermc.hydraulic.entity.vanilla.VanillaModels;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Scans a mod's jars for its entity types, the renderers registered for them, and the model
 * layers those renderers draw with.
 */
public final class EntityScanner {
    private static final String DEFAULT_LAYER = "main";
    private static final String ANIMATION_STATE = "net/minecraft/world/entity/AnimationState";
    private static final String IS_CLIENT_SIDE = "isClientSide";
    private static final String REGISTRY = "net/minecraft/core/Registry";
    private static final String REGISTER = "register";
    private static final String DEFERRED_REGISTER = "net/neoforged/neoforge/registries/DeferredRegister";
    private static final String CREATE = "create";
    private static final Set<String> STATE_CALLS = Set.of("start", "startIfStopped", "animateWhen", "stop", "fastForward");

    private final ConduitContext context;
    private final Logger logger;
    private final Collection<Path> textureSources;
    private List<String> assets;

    public EntityScanner(@NotNull ConduitContext context, @NotNull Logger logger) {
        this(context, logger, List.of());
    }

    public EntityScanner(@NotNull ConduitContext context, @NotNull Logger logger, @NotNull Collection<Path> textureSources) {
        this.context = context;
        this.logger = logger;
        this.textureSources = textureSources;
    }

    /**
     * Runs the scan.
     *
     * @param layerInfo the platform's model layer registry
     * @param rendererInfo the platform's entity renderer registry
     * @return every entity type that has a renderer drawing a known model layer
     */
    @NotNull
    public List<CustomEntity> scan(@NotNull ModelLayerRegistryInfo layerInfo, @NotNull EntityRendererRegistryInfo rendererInfo) {
        VirtualMachine vm = VirtualMachineFactory.create(this.context).await();

        Map<String, EntityModel> layers = new HashMap<>();
        vm.intercept(layerInfo.registryClass());
        vm.capture(layerInfo.captureChannel().get(), registration -> {
            ModelLayerLocation location = registration.location();
            if (location == null || location.model() == null) {
                this.logger.debug("Model layer registration could not be resolved");
                return;
            }

            Trace trace = registration.definition().get();
            if (trace == null) {
                this.logger.debug("Model layer {} has no definition that could be read", location.model().path());
                return;
            }

            EntityModelExtractor.Layer unwrapped = EntityModelExtractor.unwrap(trace.returned(), transformer ->
                this.logger.debug("Ignoring a mesh transformer on {} that is not a plain scale: {}", location.model().path(), transformer));
            EntityModel model = unwrapped.definition() instanceof Value.StaticCall call
                ? VanillaModels.get().layer(call, location.model().namespace(), location.model().path(), location.layer())
                : null;
            if (model != null) {
                model = new EntityModel(model.namespace(), model.path(), model.layer(), model.textureWidth(), model.textureHeight(),
                    model.parts(), model.scale() * unwrapped.scale());
                this.logger.debug("Using vanilla's geometry for {}: {}", location.model().path(), unwrapped.definition());
            } else {
                model = EntityModelExtractor.extract(location.model().namespace(), location.model().path(), location.layer(), unwrapped, trace);
            }

            layers.put(layerKey(model.namespace(), model.path(), model.layer()), model);

            this.logger.debug("Extracted {}:{} layer={} texture={}x{} parts={}",
                model.namespace(), model.path(), model.layer(),
                model.textureWidth(), model.textureHeight(), model.parts().size());
        });

        Map<String, Identifier> types = new HashMap<>();
        vm.intercept(EntityTypeBuilder.class);
        vm.capture(EntityTypeBuilder.CAPTURE, build -> {
            String entityClass = build.builder() == null ? null : implementation(build.builder().factory());
            org.geysermc.hydraulic.entity.conduit.Identifier id = build.key() == null ? null : build.key().identifier();
            if (entityClass != null && id != null && id.namespace() != null && id.path() != null) {
                types.putIfAbsent(entityClass, Identifier.fromNamespaceAndPath(id.namespace(), id.path()));
            }
        });

        List<Renderer> renderers = new ArrayList<>();
        vm.intercept(rendererInfo.registryClass());
        vm.capture(rendererInfo.captureChannel().get(), registration -> {
            Renderer renderer = this.renderer(vm, registration);
            if (renderer != null) {
                renderers.add(renderer);
            }
        });

        vm.execute();
        this.logger.debug("Scan captured {} layer(s), {} entity type(s) and {} renderer(s)", layers.size(), types.size(), renderers.size());

        List<CustomEntity> entities = new ArrayList<>();
        for (Renderer renderer : renderers) {
            Identifier id = types.get(renderer.entityClass());
            if (id == null) {
                id = renderer.id();
            }

            if (id == null) {
                this.logger.debug("Found a renderer for {} but could not find its entity type", renderer.entityClass());
                continue;
            }

            ModelBinding binding = renderer.binding();
            EntityModel model = binding != null
                    ? layers.get(layerKey(binding.namespace(), binding.path(), binding.layer()))
                    : layers.get(layerKey(id.getNamespace(), id.getPath(), DEFAULT_LAYER));

            if (model == null) {
                this.logger.debug("Could not find the model layer {} draws {} with", renderer.entityClass(), id);
                continue;
            }

            Summary extract = renderer.rendererClass() == null ? null : vm.summarize(renderer.rendererClass(), "extractRenderState", null);
            int reserved = renderer.textures().size() > 1 ? 1 : 0;
            CompiledAnimation animation = binding == null ? null : this.animation(vm, id, binding, model, extract, reserved);
            entities.add(new CustomEntity(id, model, renderer.textures(), binding, animation, this.texture(id, renderer, extract),
                    animation == null || animation.keyframes().isEmpty() ? List.of() : clientCalls(vm, renderer.entityClass()))
            );
        }

        return entities;
    }

    @Nullable
    private Renderer renderer(@NotNull VirtualMachine vm, @NotNull EntityRendererRegistration registration) {
        EntityType type = registration.type() == null ? null : Conduits.as(registered(registration.type()), EntityType.class, this.context);
        String entityClass = type == null ? null : implementation(type.factory());
        Trace trace = registration.renderer().get();
        if (entityClass == null || trace == null) {
            this.logger.debug("Renderer registration for {} could not be resolved", registration.type());
            return null;
        }

        List<String> textures = List.of();
        LivingEntityRenderer renderer = trace.as(LivingEntityRenderer.class);
        if (renderer != null) {
            // A renderer shared between entities only knows its entity as a type variable.
            textures = parseValidTextures(Conduits.withTypeArgument(renderer, entityClass).getTextureLocation());
        }

        ModelBinding binding = ModelBinding.of(vm, this.context, trace.returned());
        String rendererClass = trace.returned() instanceof Value.Constructed constructed ? constructed.className() : null;
        Summary texture = rendererClass == null ? null : vm.summarize(rendererClass, "getTextureLocation", null);
        Value textureLocation = texture == null ? null : texture.returned();
        if (textures.isEmpty() && textureLocation != null) {
            if (this.assets == null) {
                this.assets = TextureTemplates.assets(this.textureSources);
            }

            textures = TextureTemplates.expand(textureLocation, this.assets);
        }
        this.logger.debug("Renderer for {}: textures={} model={}", entityClass, textures, binding);
        return new Renderer(entityClass, deferredId(registration.type()), rendererClass, textures, binding, textureLocation);
    }

    @Nullable
    private Value texture(@NotNull Identifier id, @NotNull Renderer renderer, @Nullable Summary extract) {
        if (renderer.textures().size() < 2 || renderer.textureLocation() == null) {
            return null;
        }

        Value fromEntity = new StateProperties(id.getNamespace(), extract, 0).fromEntity(renderer.textureLocation());
        if (fromEntity == null) {
            this.logger.debug("Could not work out how {} picks between its textures", id);
        }

        return fromEntity;
    }

    @Nullable
    private CompiledAnimation animation(@NotNull VirtualMachine vm, @NotNull Identifier id, @NotNull ModelBinding binding,
                                        @NotNull EntityModel model, @Nullable Summary extract, int reserved) {
        Summary constructor = vm.summarize(binding.modelClass(), "<init>", null);
        Summary setupAnim = vm.summarize(binding.modelClass(), "setupAnim", null);
        if (constructor == null || setupAnim == null) {
            constructor = VanillaModels.get().constructor(binding.modelClass());
            setupAnim = VanillaModels.get().setupAnim(binding.modelClass());
        }

        if (constructor == null || setupAnim == null) {
            this.logger.debug("Could not summarise the model {} draws {} with", binding.modelClass(), id);
            return null;
        }

        String name = "animation." + id.getNamespace() + "." + id.getPath().replace('/', '.') + ".setup";
        BoneMap bones = BoneMap.of(constructor, model);
        StateProperties state = new StateProperties(id.getNamespace(), extract, reserved);
        CompiledAnimation animation = AnimationCompiler.compile(name, setupAnim, bones, model.scale(), state, this.logger);
        if (animation != null && state.overLimit(animation.properties())) {
            this.logger.debug("{} needs {} properties, more than an entity may have, so some parts are left still", id, animation.properties().size());
            state.restrict(animation.properties());
            animation = AnimationCompiler.compile(name, setupAnim, bones, model.scale(), state, this.logger);
        }

        return animation;
    }

    // Animation states are usually only started on the client, so the server repeats those calls itself.
    @NotNull
    private static List<ClientCall> clientCalls(@NotNull VirtualMachine vm, @NotNull String entityClass) {
        Summary tick = vm.summarize(entityClass, "tick", "()V");
        List<ClientCall> calls = new ArrayList<>();
        for (Summary.Invocation invocation : tick == null ? List.<Summary.Invocation>of() : tick.invocations()) {
            if (invocation.call() instanceof Value.InstanceCall(String owner, String name, Value.InstanceField(Value.This _, String _, String _), List<Value> _)
                && owner.equals(ANIMATION_STATE) && STATE_CALLS.contains(name)
                && invocation.condition() != null && Values.contains(invocation.condition(), EntityScanner::isClientSide)) {
                calls.add(new ClientCall(invocation.call(), Values.map(invocation.condition(), value -> isClientSide(value) ? Value.literal(true) : value)));
            }
        }

        return calls;
    }

    private static boolean isClientSide(@NotNull Value value) {
        return value instanceof Value.InstanceCall(String _, String callName, Value _, List<Value> args) && callName.equals(IS_CLIENT_SIDE) && args.isEmpty()
            || value instanceof Value.InstanceField(Value _, String _, String fieldName) && fieldName.equals(IS_CLIENT_SIDE);
    }

    // Registry.register returns what it registers, so a type registered in a static field is the type it built.
    @NotNull
    private static Value registered(@NotNull Value value) {
        if (value instanceof Value.StaticCall(String owner, String name, List<Value> args)
            && owner.equals(REGISTRY) && name.equals(REGISTER) && args.size() == 3) {
            return args.get(2);
        }
        return value;
    }


    @Nullable
    private static Identifier deferredId(@Nullable Value value) {
        if (value instanceof Value.InstanceCall(String _, String _, Value holder, List<Value> _)
                && holder instanceof Value.InstanceCall(String owner, String name, Value register, List<Value> args)
                && owner.equals(DEFERRED_REGISTER) && name.equals(REGISTER)
                && !args.isEmpty() && args.getFirst() instanceof Value.Literal(String path)
                && register instanceof Value.StaticCall(String createOwner, String create, List<Value> createArgs)
                && createOwner.equals(DEFERRED_REGISTER) && create.equals(CREATE)
                && createArgs.size() == 2 && createArgs.get(1) instanceof Value.Literal(String namespace)) {
            return Identifier.fromNamespaceAndPath(namespace, path);
        }

        return null;
    }

    @Nullable
    private static String implementation(@Nullable EntityFactory factory) {
        return factory == null ? null : factory.implementation();
    }

    @NotNull
    private static String layerKey(@NotNull String namespace, @NotNull String path, @Nullable String layer) {
        return namespace + ":" + path + "#" + (layer == null || layer.isEmpty() ? DEFAULT_LAYER : layer);
    }

    @NotNull
    private static List<String> parseValidTextures(@NotNull List<org.geysermc.hydraulic.entity.conduit.Identifier> resolved) {
        List<String> out = new ArrayList<>(resolved.size());
        for (org.geysermc.hydraulic.entity.conduit.Identifier texture : resolved) {
            if (texture == null || texture.namespace() == null || texture.path() == null) {
                continue;
            }

            String value = texture.namespace() + ":" + texture.path();
            if (!out.contains(value)) {
                out.add(value);
            }
        }

        return out;
    }

    private record Renderer(@NotNull String entityClass, @Nullable Identifier id, @Nullable String rendererClass, @NotNull List<String> textures,
                            @Nullable ModelBinding binding, @Nullable Value textureLocation
    ) {
    }
}
