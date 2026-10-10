package org.geysermc.hydraulic.entity;

import com.google.auto.service.AutoService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import org.geysermc.conduit.ConduitContext;
import org.geysermc.conduit.Conduits;
import org.geysermc.conduit.VirtualMachine;
import org.geysermc.conduit.VirtualMachineFactory;
import org.geysermc.geyser.api.entity.custom.CustomEntityDefinition;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineEntitiesEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineEntityPropertiesEvent;
import org.geysermc.hydraulic.Constants;
import org.geysermc.hydraulic.entity.conduit.EntityModel;
import org.geysermc.hydraulic.entity.conduit.EntityRendererRegistration;
import org.geysermc.hydraulic.entity.conduit.EntityTypeBuilder;
import org.geysermc.hydraulic.entity.conduit.LivingEntityRenderer;
import org.geysermc.hydraulic.entity.conduit.ModelLayerLocation;
import org.geysermc.hydraulic.pack.PackModule;
import org.geysermc.hydraulic.pack.TexturePackModule;
import org.geysermc.hydraulic.pack.context.PackEventContext;
import org.geysermc.hydraulic.pack.context.PackPostProcessContext;
import org.geysermc.hydraulic.pack.context.PackPreProcessContext;
import org.geysermc.hydraulic.platform.mod.ModInfo;
import org.geysermc.pack.bedrock.resource.entity.ClientEntity;
import org.geysermc.pack.bedrock.resource.entity.Entity;
import org.geysermc.pack.bedrock.resource.entity.cliententity.Description;
import org.geysermc.pack.bedrock.resource.models.entity.ModelEntity;
import org.geysermc.pack.bedrock.resource.render_controllers.RenderControllers;
import org.geysermc.pack.bedrock.resource.render_controllers.rendercontrollers.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@AutoService(PackModule.class)
public class EntityPackModule extends TexturePackModule<EntityPackModule> {
    private static final String ARCHITECTURY_ID = "architectury";
    private static final String DEFAULT_TEXTURE = "default";
    private static final String VARIANT_ARRAY = "Array.variants";

    private final Map<String, List<EntityModel>> models = new HashMap<>();

    private final Map<String, Map<String, List<String>>> textures = new HashMap<>();

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

        ModelLayerRegistryInfo layerInfo = context.hydraulic().modelLayerInfo();

        VirtualMachine vm = VirtualMachineFactory.create(conduit).await();
        vm.intercept(layerInfo.registryClass());

        vm.capture(layerInfo.captureChannel().get(), registration -> {
            ModelLayerLocation location = registration.location();
            EntityModel model = EntityModelExtractor.extract(location.model(), location.layer(), registration.definition().get());
            this.models.computeIfAbsent(context.mod().id(), k -> new ArrayList<>()).add(model);

            context.logger().debug("Extracted {}:{} layer={} texture={}x{} parts={}",
                model.namespace(), model.path(), model.layer(),
                model.textureWidth(), model.textureHeight(), model.parts().size());
        });

        Map<String, String> names = new HashMap<>();
        vm.intercept(EntityTypeBuilder.class);
        vm.capture(EntityTypeBuilder.CAPTURE, build -> {
            String entityClass = build.builder() == null ? null : implementation(build.builder().factory());
            org.geysermc.hydraulic.entity.conduit.Identifier id = build.key() == null ? null : build.key().identifier();
            if (entityClass != null && id != null && id.path() != null) {
                names.putIfAbsent(entityClass, id.path());
            }
        });

        Map<String, List<String>> textures = new HashMap<>();
        EntityRendererRegistryInfo rendererInfo = context.hydraulic().entityRendererInfo();
        vm.intercept(rendererInfo.registryClass());
        vm.capture(rendererInfo.captureChannel().get(), registration -> this.captureTexture(context, registration, textures));

        vm.execute();

        textures.forEach((entityClass, variants) -> {
            String name = names.get(entityClass);
            if (name == null) {
                context.logger().debug("Captured textures for {} but could not find its entity type", entityClass);
                return;
            }

            this.textures.computeIfAbsent(context.mod().id(), _ -> new HashMap<>()).put(name, variants);
            context.logger().info("Captured {} texture(s) for {}: {}", variants.size(), name, variants);
        });
    }

    private void captureTexture(@NotNull PackPreProcessContext<EntityPackModule> context,
                                @NotNull EntityRendererRegistration registration,
                                @NotNull Map<String, List<String>> textures) {
        String entityClass = entityClass(registration.type());
        LivingEntityRenderer renderer = registration.renderer().get();
        if (entityClass == null || renderer == null) {
            context.logger().debug("Renderer registration for {} could not be resolved", registration.type());
            return;
        }

        // A renderer shared between entities only knows its entity as a type variable.
        List<String> variants = parseValidTextures(Conduits.withTypeArgument(renderer, entityClass).getTextureLocation());
        if (variants.isEmpty()) {
            context.logger().debug("Renderer texture for {} is not statically known, skipping", entityClass);
            return;
        }

        textures.put(entityClass, variants);
    }

    @Nullable
    private static String entityClass(@Nullable org.geysermc.hydraulic.entity.conduit.EntityType type) {
        return type == null ? null : implementation(type.factory());
    }

    @Nullable
    private static String implementation(@Nullable org.geysermc.hydraulic.entity.conduit.EntityFactory factory) {
        return factory == null ? null : factory.implementation();
    }

    private void postProcess(@NotNull PackPostProcessContext<EntityPackModule> context) {
        List<EntityModel> models = this.models.get(context.mod().id());
        if (models == null) {
            return;
        }

        context.logger().debug("Models to convert: {} in mod {}", models.size(), context.mod().id());
        for (EntityModel model : models) {
            ModelEntity entity = BedrockEntityModelConverter.convert(List.of(model));
            Identifier identifier = Identifier.fromNamespaceAndPath(model.namespace(), model.path());
            this.convertModel(context, entity, identifier, BedrockEntityModelConverter.identifier(model), this.texturesFor(context.mod().id(), identifier));
        }
    }

    private void convertModel(PackPostProcessContext<EntityPackModule> context, ModelEntity model, Identifier identifier, String geometry, List<String> variants) {
        context.bedrockResourcePack().addEntityModel(
                model,
                identifier.getPath() + ".geo.json"
        );

        Map<String, String> textures = new LinkedHashMap<>();
        for (int i = 0; i < variants.size(); i++) {
            String baseTexturePath = texturePath(variants.get(i));
            String outputLoc = baseTexturePath.substring(0, baseTexturePath.lastIndexOf('/') + 1) + context.mod().id() + "/" + baseTexturePath.substring(baseTexturePath.lastIndexOf('/') + 1);
            textures.put(textureKey(i), outputLoc);
        }

        if (textures.isEmpty()) {
            String outputLoc = String.format(Constants.BEDROCK_TEXTURE_LOCATION, "entity/" + context.mod().id() + "/" + identifier.getPath());
            textures.put(DEFAULT_TEXTURE, outputLoc);
        }

        String controller = "controller.render." + identifier.getNamespace() + "." + identifier.getPath();
        this.addRenderController(context, controller, identifier, geometry, textures.keySet());

        Entity entity = new Entity();
        entity.formatVersion("1.10.0");

        ClientEntity clientEntity = new ClientEntity();

        Description description = new Description();
        description.identifier(identifier.toString());
        description.materials(Map.of(DEFAULT_TEXTURE, "entity_alphatest"));
        description.textures(textures);
        description.geometry(Map.of(DEFAULT_TEXTURE, geometry));
        description.renderControllers(new String [] { controller });

        clientEntity.description(description);

        entity.clientEntity(clientEntity);

        // TODO: Client entity support in pack converter
        context.bedrockResourcePack().addExtraFile(
                prune(Constants.GSON.toJsonTree(entity)),
                "entity/" + identifier.getPath() + ".entity.json"
        );
    }

    private static JsonElement prune(JsonElement element) {
        if (element instanceof JsonObject object) {
            for (String key : new ArrayList<>(object.keySet())) {
                JsonElement value = prune(object.get(key));
                if (value.isJsonObject() && value.getAsJsonObject().isEmpty()
                        || value.isJsonArray() && value.getAsJsonArray().isEmpty()) {
                    object.remove(key);
                }
            }
        }

        return element;
    }

    private void addRenderController(PackPostProcessContext<EntityPackModule> context, String controller, Identifier identifier, String geometry, Collection<String> keys) {
        var selector = new org.geysermc.pack.bedrock.resource.render_controllers.rendercontrollers.RenderControllers();
        selector.geometry("Geometry." + DEFAULT_TEXTURE);

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
    private List<String> texturesFor(@NotNull String mod, @NotNull Identifier identifier) {
        return this.textures.getOrDefault(mod, Map.of()).getOrDefault(identifier.getPath(), List.of());
    }

    @NotNull
    private static org.geysermc.geyser.api.util.Identifier variantProperty(@NotNull String namespace) {
        return org.geysermc.geyser.api.util.Identifier.of(namespace, "variant");
    }

    private static String texturePath(String texture) {
        String path = texture.substring(texture.indexOf(':') + 1);
        return path.endsWith(".png") ? path.substring(0, path.length() - 4) : path;
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

        ConduitContext conduit = ConduitContext.load(origin.toArray(Path[]::new));

        // A JiJ'd mod reports the jar it is nested inside, so its own classes are in a nested jar.
        for (Path jar : origin) {
            if (Files.isRegularFile(jar)) {
                conduit.addNestedJars(jar, "META-INF/jars");
            }
        }

        // Architectury is resolved against but never scanned. Some mods route their registrations through it.
        ModInfo architectury = context.hydraulic().mod(ARCHITECTURY_ID);
        if (architectury != null) {
            for (Path jar : architectury.origin()) {
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
        Map<String, List<String>> captured = this.textures.getOrDefault(context.mod().id(), Map.of());
        for (Map.Entry<String, List<String>> entry : captured.entrySet()) {
            if (entry.getValue().size() < 2) {
                continue;
            }

            org.geysermc.geyser.api.util.Identifier entity = org.geysermc.geyser.api.util.Identifier.of(context.mod().namespace(), entry.getKey());
            context.event().registerIntegerProperty(entity, variantProperty(context.mod().namespace()), 0, entry.getValue().size() - 1, 0);
        }
    }

    private void onDefineEntities(PackEventContext<GeyserDefineEntitiesEvent, EntityPackModule> context) {
        GeyserDefineEntitiesEvent event = context.event();
        List<EntityType<?>> entities = context.registryValues(BuiltInRegistries.ENTITY_TYPE);
        context.logger().debug("Defining custom entities for mod {}", context.mod().id());

        // TODO: Scope to EntityType
        /*
        DefaultedRegistry<EntityType<?>> registry = BuiltInRegistries.ENTITY_TYPE;
        for (EntityType<?> entity : entities) {
            Identifier entityLocation = registry.getKey(entity);
            event.register(CustomEntityDefinition.of(entityLocation.toString()));
        }

         */
        List<EntityModel> models = this.models.get(context.mod().id());
        if (models != null) {
            for (EntityModel model : models) {
                event.register(CustomEntityDefinition.of(model.namespace() + ":" + model.path()));
                context.logger().debug("Registered custom entity {}:{}", model.namespace(), model.path());
            }
        }
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
}
