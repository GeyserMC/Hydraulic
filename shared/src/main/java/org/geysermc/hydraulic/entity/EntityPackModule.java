package org.geysermc.hydraulic.entity;

import com.google.auto.service.AutoService;
import net.kyori.adventure.key.Key;
import net.minecraft.resources.Identifier;
import org.geysermc.conduit.ConduitContext;
import org.geysermc.geyser.api.entity.custom.CustomEntityDefinition;
import org.geysermc.geyser.api.entity.property.type.GeyserFloatEntityProperty;
import org.geysermc.geyser.api.entity.property.type.GeyserIntEntityProperty;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineEntitiesEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineEntityPropertiesEvent;
import org.geysermc.hydraulic.entity.animation.AnimationCompiler.CompiledAnimation;
import org.geysermc.hydraulic.entity.animation.AnimationCompiler.KeyframeAnimation;
import org.geysermc.hydraulic.entity.animation.StateProperty;
import org.geysermc.hydraulic.entity.geckolib.GeckoLibEntities;
import org.geysermc.hydraulic.entity.runtime.PropertySync;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.hydraulic.pack.TexturePackModule;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.pack.context.PackPreProcessContext;
import org.geysermc.pack.bedrock.resource.BedrockResourcePack;
import org.geysermc.pack.bedrock.resource.Sounds;
import org.geysermc.pack.bedrock.resource.entity.ClientEntity;
import org.geysermc.pack.bedrock.resource.entity.Entity;
import org.geysermc.pack.bedrock.resource.entity.cliententity.Description;
import org.geysermc.pack.bedrock.resource.entity.cliententity.description.Scripts;
import org.geysermc.pack.bedrock.resource.render_controllers.RenderControllers;
import org.geysermc.pack.bedrock.resource.render_controllers.rendercontrollers.Arrays;
import org.geysermc.pack.bedrock.resource.sounds.EntitySounds;
import org.geysermc.pack.bedrock.resource.sounds.entitysounds.Entities;
import org.geysermc.pack.bedrock.resource.sounds.entitysounds.entities.Events;
import org.geysermc.pack.converter.pipeline.AssetConverters;
import org.geysermc.pack.converter.pipeline.ConverterPipeline;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@AutoService(PackModule.class)
public class EntityPackModule extends TexturePackModule<EntityPackModule> {
    private static final String DEFAULT_TEXTURE = "default";
    private static final String VARIANT_ARRAY = "Array.variants";
    private static final String SETUP_ANIMATION = "setup";
    private static final String TEXTURES_DIRECTORY = "textures/";
    private static final String PNG = ".png";
    private static final List<String> SILENCED_SOUNDS = List.of("hurt", "death", "fall.big", "fall.small");

    private final Map<String, List<CustomEntity>> entities = new HashMap<>();
    private final GeckoLibEntities geckoLib = new GeckoLibEntities();
    private final PropertySync sync = new PropertySync();
    private final AtomicBoolean ticking = new AtomicBoolean();

    public EntityPackModule() {
        this.listenOn(GeyserDefineEntitiesEvent.class, this::onDefineEntities);
        this.listenOn(GeyserDefineEntityPropertiesEvent.class, this::onDefineEntityProperties);

        this.preProcess(this::preProcess);
        this.postProcess(this::postProcess);
    }

    private void preProcess(@NotNull PackPreProcessContext<EntityPackModule> context) {
        ConduitContext conduit = this.createContext(context);
        if (conduit == null) {
            return;
        }

        Set<Path> textureSources = new LinkedHashSet<>(context.mod().origin());
        textureSources.addAll(context.hydraulic().modJars());
        EntityScanner scanner = new EntityScanner(conduit, context.logger(), textureSources);
        List<CustomEntity> entities = scanner.scan(context.hydraulic().modelLayerInfo(), context.hydraulic().entityRendererInfo());
        this.entities.put(context.mod().id(), entities);

        for (CustomEntity entity : entities) {
            context.logger().info("Found entity {} with {} texture(s): {}", entity.id(), entity.textures().size(), entity.textures());
        }

        scanner.entityClasses().forEach(CustomEntityTypes::remember);

        this.geckoLib.preProcess(context.mod().id(), scanner.geckoLibEntities(), context.logger());
    }

    private void postProcess(@NotNull PackPostProcessContext<EntityPackModule> context) {
        this.geckoLib.postProcess(context.mod().id(), context.bedrockResourcePack(), texture -> bedrockTexture(context, texture), context.logger());

        List<CustomEntity> entities = this.entities.get(context.mod().id());
        List<String> ids = new ArrayList<>(this.geckoLib.ids(context.mod().id()));
        if (entities != null) {
            entities.forEach(entity -> ids.add(entity.id().toString()));
        }

        // The server already sends the sounds Java plays for these, so a Bedrock player's
        // defaults would only add a wrong one
        silenceClientSounds(context.bedrockResourcePack(), ids);

        if (entities == null) {
            return;
        }

        context.logger().info("Entities to convert: {} in mod {}", entities.size(), context.mod().id());
        for (CustomEntity entity : entities) {
            this.convertModel(context, entity);
        }
    }

    @NotNull
    @Override
    public List<ConverterPipeline<?, ?>> converters(@NotNull ModInfo mod) {
        List<JavaEntityModel> models = this.entities.getOrDefault(mod.id(), List.of()).stream().map(CustomEntity::model).distinct().toList();
        return models.isEmpty() ? List.of() : List.of(AssetConverters.create(new EntityModelConverter(models)));
    }

    private void convertModel(PackPostProcessContext<EntityPackModule> context, CustomEntity custom) {
        Identifier identifier = custom.id();
        String geometry = EntityModelConverter.identifier(custom.model());
        List<String> variants = custom.textures();
        CompiledAnimation animation = custom.animation();

        Map<String, String> textures = new LinkedHashMap<>();
        for (int i = 0; i < variants.size(); i++) {
            textures.put(textureKey(i), bedrockTexture(context, variants.get(i)));
        }

        if (textures.isEmpty()) {
            textures.put(DEFAULT_TEXTURE, bedrockTexture(context, identifier.getNamespace() + ":textures/entity/" + identifier.getPath() + ".png"));
        }

        String controller = "controller.render." + identifier.getNamespace() + "." + identifier.getPath();
        this.addRenderController(context, controller, identifier, textures.keySet(), animation == null ? Map.of() : animation.partVisibility());

        Entity entity = new Entity();
        entity.formatVersion("1.10.0");

        ClientEntity clientEntity = new ClientEntity();

        Description description = new Description();
        description.identifier(identifier.toString());
        description.materials(Map.of(DEFAULT_TEXTURE, "entity_alphatest"));
        description.textures(textures);
        description.geometry(Map.of(DEFAULT_TEXTURE, geometry));
        description.renderControllers(new String [] { controller });

        if (animation != null && (!animation.channels().isEmpty() || !animation.keyframes().isEmpty())) {
            context.bedrockResourcePack().addAnimation(animation.toAnimation(), "animations/" + identifier.getPath() + ".animation.json");

            Map<String, String> animations = new LinkedHashMap<>();
            List<Map<String, String>> animate = new ArrayList<>();
            if (!animation.channels().isEmpty()) {
                animations.put(SETUP_ANIMATION, animation.name());
                animate.add(Map.of(SETUP_ANIMATION, "1"));
            }

            for (KeyframeAnimation keyframe : animation.keyframes()) {
                animations.put(keyframe.key(), keyframe.name());
                animate.add(Map.of(keyframe.key(), keyframe.weight()));
            }

            description.animations(animations);

            Scripts scripts = new Scripts();
            scripts.animate(animate);
            if (!animation.preAnimation().isEmpty()) {
                scripts.preAnimation(animation.preAnimation().toArray(String[]::new));
            }

            if (!animation.initialize().isEmpty()) {
                scripts.initialize(animation.initialize().toArray(String[]::new));
            }

            description.scripts(scripts);
        }

        clientEntity.description(description);

        entity.clientEntity(clientEntity);

        context.bedrockResourcePack().addClientEntity(entity, "entity/" + identifier.getPath() + ".entity.json");
    }

    private void addRenderController(PackPostProcessContext<EntityPackModule> context, String controller, Identifier identifier,
                                     Collection<String> keys, Map<String, String> visibility) {
        var selector = new org.geysermc.pack.bedrock.resource.render_controllers.rendercontrollers.RenderControllers();
        selector.geometry("Geometry." + DEFAULT_TEXTURE);

        if (!visibility.isEmpty()) {
            List<Map<String, String>> parts = new ArrayList<>();
            parts.add(Map.of("*", "1"));
            visibility.forEach((bone, condition) -> parts.add(Map.of(bone, condition)));
            selector.partVisibility(parts);
        }

        if (keys.size() > 1) {
            Arrays arrays = new Arrays();
            arrays.textures(Map.of(VARIANT_ARRAY, keys.stream().map(key -> "Texture." + key).toArray(String[]::new)));
            selector.arrays(arrays);
            selector.textures(new String[] { VARIANT_ARRAY + "[query.property('" + variantProperty(identifier.getNamespace()) + "')]" });
        } else {
            selector.textures(new String[] { "Texture." + DEFAULT_TEXTURE });
        }

        RenderControllers controllers = new RenderControllers();
        controllers.formatVersion("1.10.0");
        controllers.renderControllers(Map.of(controller, selector));

        context.bedrockResourcePack().addRenderController(controllers, "render_controllers/" + identifier.getPath() + ".render_controllers.json");
    }

    @NotNull
    private static org.geysermc.geyser.api.util.Identifier variantProperty(@NotNull String namespace) {
        return org.geysermc.geyser.api.util.Identifier.of(namespace, "variant");
    }

    // Matches where the texture converter writes the texture, without the extension Bedrock leaves off
    private static String bedrockTexture(PackPostProcessContext<EntityPackModule> context, String texture) {
        Key key = Key.key(texture);
        String path = key.value();
        path = path.startsWith(TEXTURES_DIRECTORY) ? path.substring(TEXTURES_DIRECTORY.length()) : path;
        path = path.endsWith(PNG) ? path.substring(0, path.length() - PNG.length()) : path;

        String output = getOutputFromModel(context, Key.key(key.namespace(), path));
        return output.endsWith(PNG) ? output.substring(0, output.length() - PNG.length()) : output;
    }

    private static String textureKey(int variant) {
        return variant == 0 ? DEFAULT_TEXTURE : "variant" + variant;
    }

    @Nullable
    private ConduitContext createContext(@NotNull PackPreProcessContext<EntityPackModule> context) {
        Collection<Path> origin = context.mod().origin();
        if (origin.isEmpty()) {
            context.logger().debug("Mod {} has no jar on disk, skipping entity scan", context.mod().id());
            return null;
        }

        context.logger().debug("Scanning {} for entities", origin);
        ConduitContext conduit = ConduitContext.load(origin.toArray(Path[]::new));

        // A JiJ'd mod reports the jar it is nested inside, so its own classes are in a nested jar.
        for (Path jar : origin) {
            if (Files.isRegularFile(jar)) {
                conduit.addNestedJars(jar, "META-INF/jars");
            }
        }

        // Other mods' jars are resolved against but never scanned, as registrations are often routed through a library.
        for (Path jar : context.hydraulic().modJars()) {
            if (!origin.contains(jar)) {
                conduit.addContextJar(jar);
            }
        }

        return conduit;
    }

    @Override
    public boolean test(@NotNull PackPostProcessContext<EntityPackModule> context) {
        return true; // !context.registryValues(BuiltInRegistries.ENTITY_TYPE).isEmpty();
    }

    private void onDefineEntityProperties(PackEventContext<GeyserDefineEntityPropertiesEvent, EntityPackModule> context) {
        if (this.ticking.compareAndSet(false, true)) {
            context.hydraulic().registerServerTick(this.sync::tick);
        }

        for (CustomEntity entity : this.entities.getOrDefault(context.mod().id(), List.of())) {
            org.geysermc.geyser.api.util.Identifier id = org.geysermc.geyser.api.util.Identifier.of(entity.id().toString());
            this.sync.clear(entity.id());

            if (entity.textures().size() > 1) {
                GeyserIntEntityProperty variant = context.event().registerIntegerProperty(id, variantProperty(entity.id().getNamespace()), 0, entity.textures().size() - 1, 0);
                if (entity.texture() != null) {
                    this.sync.setVariant(entity.id(), variant, entity.texture(), entity.textures());
                }
            }

            if (!entity.clientCalls().isEmpty()) {
                this.sync.addClientCalls(entity.id(), entity.clientCalls());
            }

            if (entity.animation() != null) {
                for (StateProperty property : entity.animation().properties()) {
                    GeyserFloatEntityProperty registered = context.event().registerFloatProperty(id,
                        org.geysermc.geyser.api.util.Identifier.of(property.id()), PropertySync.MIN, PropertySync.MAX, 0f);
                    this.sync.addProperty(entity.id(), registered, property.value());
                }
            }
        }

        this.geckoLib.defineProperties(context.mod().id(), context.event(), this.sync);
    }

    private void onDefineEntities(PackEventContext<GeyserDefineEntitiesEvent, EntityPackModule> context) {
        for (CustomEntity entity : this.entities.getOrDefault(context.mod().id(), List.of())) {
            context.event().register(CustomEntityDefinition.of(entity.id().toString()));
            context.logger().debug("Registered custom entity {} (mob: {})", entity.id(), CustomEntityTypes.isMob(entity.id()));
        }

        this.geckoLib.defineEntities(context.mod().id(), context.event(), context.logger());
    }

    private static void silenceClientSounds(@NotNull BedrockResourcePack pack, @NotNull List<String> ids) {
        if (ids.isEmpty()) {
            return;
        }

        Sounds sounds = pack.sounds();
        if (sounds == null) {
            sounds = new Sounds();
            pack.sounds(sounds);
        }

        EntitySounds entitySounds = sounds.entitySounds();
        if (entitySounds == null) {
            entitySounds = new EntitySounds();
            sounds.entitySounds(entitySounds);
        }

        Map<String, Entities> entities = entitySounds.entities() == null ? new LinkedHashMap<>() : new LinkedHashMap<>(entitySounds.entities());
        for (String id : ids) {
            Map<String, Events> events = new LinkedHashMap<>();
            for (String event : SILENCED_SOUNDS) {
                Events silent = new Events();
                silent.sound("");
                events.put(event, silent);
            }

            Entities entity = new Entities();
            entity.events(events);
            entities.put(id, entity);
        }

        entitySounds.entities(entities);
    }
}
