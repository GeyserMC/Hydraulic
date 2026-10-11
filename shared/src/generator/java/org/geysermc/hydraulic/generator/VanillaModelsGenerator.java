package org.geysermc.hydraulic.generator;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.geysermc.conduit.ConduitContext;
import org.geysermc.conduit.Summary;
import org.geysermc.conduit.Trace;
import org.geysermc.conduit.VirtualMachine;
import org.geysermc.conduit.VirtualMachineFactory;
import org.geysermc.hydraulic.entity.EntityModelExtractor;
import org.geysermc.hydraulic.entity.conduit.EntityModel;
import org.geysermc.hydraulic.entity.vanilla.SummaryCodec;
import org.geysermc.hydraulic.entity.vanilla.VanillaModels;

import java.io.IOException;
import java.io.InputStream;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.MethodModel;
import java.lang.reflect.AccessFlag;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Reads vanilla's entity model layers and model code from the client jar into the bundle Hydraulic
 * ships, for mods that reuse them on a server where the client jar isn't present.
 */
public final class VanillaModelsGenerator {
    private static final String MODELS = "net/minecraft/client/model/";
    private static final String GEOMETRY = "net/minecraft/client/model/geom/";
    private static final String LAYER_DEFINITION = "Lnet/minecraft/client/model/geom/builders/LayerDefinition;";
    private static final String FROM_ROOT = "(Lnet/minecraft/client/model/geom/ModelPart;)V";
    private static final String ITEM_TEXTURES = "assets/minecraft/textures/item/";
    private static final String TEXTURES = "assets/minecraft/textures/";

    // Kept as calls, as they are what the converter reads, or what Molang replaces.
    private static final String[] OPAQUE = {
        "net.minecraft.util.Mth",
        "net.minecraft.client.model.geom.",
        "net.minecraft.client.animation.",
        "net.minecraft.client.model.Model",
        "net.minecraft.client.model.EntityModel"
    };

    private VanillaModelsGenerator() {
    }

    public static void main(String[] args) throws IOException {
        Path output = Path.of(args[0]);
        List<Path> jars = Arrays.stream(args).skip(1).map(Path::of).toList();
        VirtualMachine vm = VirtualMachineFactory.create(ConduitContext.load(jars.toArray(Path[]::new))).await().opaque(OPAQUE);

        JsonObject layers = new JsonObject();
        JsonObject models = new JsonObject();
        int textures = 0;
        for (Path jar : jars) {
            try (JarFile file = new JarFile(jar.toFile())) {
                Enumeration<JarEntry> entries = file.entries();
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    // Item textures are tiny, and mods reuse them, such as tinting the vanilla egg for spawn eggs.
                    if (name.startsWith(ITEM_TEXTURES) && name.endsWith(".png")) {
                        Path texture = output.getParent().resolve(VanillaModels.VANILLA_TEXTURES + name.substring(TEXTURES.length()));
                        Files.createDirectories(texture.getParent());
                        try (InputStream in = file.getInputStream(file.getEntry(name))) {
                            Files.write(texture, in.readAllBytes());
                        }
                        textures++;
                        continue;
                    }

                    if (!name.startsWith(MODELS) || name.startsWith(GEOMETRY) || !name.endsWith(".class") || name.contains("$")) {
                        continue;
                    }

                    String owner = name.substring(0, name.length() - ".class".length());
                    ClassModel model;
                    try (InputStream in = file.getInputStream(file.getEntry(name))) {
                        model = ClassFile.of().parse(in.readAllBytes());
                    }

                    for (MethodModel method : model.methods()) {
                        if (method.flags().has(AccessFlag.STATIC) && method.methodTypeSymbol().parameterCount() == 0
                            && method.methodTypeSymbol().returnType().descriptorString().equals(LAYER_DEFINITION)) {
                            layer(vm, owner, method.methodName().stringValue(), layers);
                        }
                    }
                    model(vm, owner, models);
                }
            }
        }

        JsonObject bundle = new JsonObject();
        bundle.add("layers", layers);
        bundle.add("models", models);
        Files.createDirectories(output.getParent());
        Files.writeString(output, new Gson().toJson(bundle));
        System.out.println("Wrote " + layers.size() + " vanilla layers, " + models.size() + " vanilla models and " + textures + " vanilla item textures to " + output.getParent());
    }

    private static void layer(VirtualMachine vm, String owner, String method, JsonObject layers) {
        try {
            Trace trace = vm.trace(owner, method, List.of());
            EntityModelExtractor.Layer unwrapped = EntityModelExtractor.unwrap(trace.returned(), _ -> {
            });
            String key = VanillaModels.layerKey(owner, method, List.of());
            EntityModel model = EntityModelExtractor.extract("minecraft", key, null, unwrapped, trace);
            if (!model.parts().isEmpty()) {
                layers.add(key, new Gson().toJsonTree(model));
            }
        } catch (RuntimeException e) {
            System.err.println("Skipping layer " + owner + "." + method + ": " + e);
        }
    }

    private static void model(VirtualMachine vm, String owner, JsonObject models) {
        try {
            Summary setupAnim = vm.summarize(owner, "setupAnim", null);
            if (setupAnim == null) {
                return;
            }

            Summary constructor = vm.summarize(owner, "<init>", FROM_ROOT);
            if (constructor == null) {
                constructor = vm.summarize(owner, "<init>", null);
            }
            if (constructor == null) {
                return;
            }

            JsonObject model = new JsonObject();
            model.add("constructor", SummaryCodec.write(constructor));
            model.add("setupAnim", SummaryCodec.write(setupAnim));
            models.add(owner, model);
        } catch (RuntimeException e) {
            System.err.println("Skipping model " + owner + ": " + e);
        }
    }
}
