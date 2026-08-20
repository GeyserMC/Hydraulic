package org.geysermc.hydraulic.pack.dump;

import org.geysermc.geyser.api.block.custom.CustomBlockData;
import org.geysermc.geyser.api.block.custom.CustomBlockPermutation;
import org.geysermc.geyser.api.block.custom.component.BoxComponent;
import org.geysermc.geyser.api.block.custom.component.CustomBlockComponents;
import org.geysermc.geyser.api.block.custom.component.GeometryComponent;
import org.geysermc.geyser.api.block.custom.component.MaterialInstance;
import org.geysermc.geyser.api.block.custom.component.TransformationComponent;
import org.geysermc.geyser.api.block.custom.property.CustomBlockProperty;
import org.geysermc.geyser.api.item.custom.v2.CustomItemBedrockOptions;
import org.geysermc.geyser.api.item.custom.v2.NonVanillaCustomItemDefinition;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Plain-Java models used to serialize Hydraulic's registered custom items/blocks to JSON so a
 * Geyser-Standalone extension (or any other Geyser environment without the server-side mod) can
 * re-register them. Kept deliberately simple so both sides only need Gson.
 */
public final class DumpModels {

    private DumpModels() {
    }

    public static class DumpItem {
        public String identifier;
        public String bedrockIdentifier;
        public int javaId;
        public String displayName;
        public String translationString;
        public String icon;
        public String creativeCategory;
        public String creativeGroup;
        public boolean displayHandheld;
        public boolean allowOffhand;

        public static DumpItem of(NonVanillaCustomItemDefinition def) {
            DumpItem item = new DumpItem();
            item.identifier = def.identifier().toString();
            item.bedrockIdentifier = def.bedrockIdentifier().toString();
            item.javaId = def.javaId();
            item.displayName = def.displayName();
            item.translationString = def.translationString();
            CustomItemBedrockOptions options = def.bedrockOptions();
            if (options != null) {
                item.icon = options.icon();
                item.creativeCategory = options.creativeCategory() != null ? options.creativeCategory().name() : null;
                item.creativeGroup = options.creativeGroup();
                item.displayHandheld = options.displayHandheld();
                item.allowOffhand = options.allowOffhand();
            }
            return item;
        }
    }

    public static class DumpBlock {
        public String name;
        public String identifier;
        public boolean includedInCreativeInventory;
        public List<DumpProperty> properties = new ArrayList<>();
        public List<DumpPermutation> permutations = new ArrayList<>();
        public DumpComponents components = new DumpComponents();
        public Map<String, List<String>> blockStates = new LinkedHashMap<>();

        public static DumpBlock of(CustomBlockData data) {
            DumpBlock block = new DumpBlock();
            block.name = data.name();
            block.identifier = data.identifier();
            block.includedInCreativeInventory = data.includedInCreativeInventory();
            for (CustomBlockProperty<?> property : data.properties().values()) {
                DumpProperty dumpProperty = new DumpProperty();
                dumpProperty.name = property.name();
                dumpProperty.type = property.getClass().getSimpleName();
                dumpProperty.values = property.values().stream().map(String::valueOf).toList();
                block.properties.add(dumpProperty);
            }
            for (CustomBlockPermutation permutation : data.permutations()) {
                DumpPermutation dumpPermutation = new DumpPermutation();
                dumpPermutation.condition = permutation.condition();
                dumpPermutation.components = DumpComponents.of(permutation.components());
                block.permutations.add(dumpPermutation);
            }
            block.components = DumpComponents.of(data.components());
            return block;
        }
    }

    public static class DumpProperty {
        public String name;
        public String type;
        public List<String> values;
    }

    public static class DumpPermutation {
        public String condition;
        public DumpComponents components = new DumpComponents();
    }

    public static class DumpComponents {
        public DumpBox selectionBox;
        public List<DumpBox> collisionBoxes = new ArrayList<>();
        public String displayName;
        public DumpGeometry geometry;
        public Map<String, DumpMaterial> materialInstances = new LinkedHashMap<>();
        public Float destructibleByMining;
        public Float friction;
        public Integer lightEmission;
        public Integer lightDampening;
        public DumpTransform transformation;
        public boolean unitCube;
        public boolean placeAir;
        public Set<String> tags;

        public static DumpComponents of(CustomBlockComponents components) {
            DumpComponents dump = new DumpComponents();
            if (components.selectionBox() != null) {
                dump.selectionBox = DumpBox.of(components.selectionBox());
            }
            for (BoxComponent box : components.collisionBoxes()) {
                dump.collisionBoxes.add(DumpBox.of(box));
            }
            dump.displayName = components.displayName();
            if (components.geometry() != null) {
                dump.geometry = new DumpGeometry();
                dump.geometry.identifier = components.geometry().identifier();
                dump.geometry.boneVisibility = components.geometry().boneVisibility();
            }
            for (Map.Entry<String, MaterialInstance> entry : components.materialInstances().entrySet()) {
                MaterialInstance material = entry.getValue();
                DumpMaterial dumpMaterial = new DumpMaterial();
                dumpMaterial.texture = material.texture();
                dumpMaterial.renderMethod = material.renderMethod();
                dumpMaterial.faceDimming = material.faceDimming();
                dumpMaterial.ambientOcclusion = material.ambientOcclusion();
                dump.materialInstances.put(entry.getKey(), dumpMaterial);
            }
            dump.destructibleByMining = components.destructibleByMining();
            dump.friction = components.friction();
            dump.lightEmission = components.lightEmission();
            dump.lightDampening = components.lightDampening();
            if (components.transformation() != null) {
                TransformationComponent t = components.transformation();
                dump.transformation = new DumpTransform();
                dump.transformation.rotationX = t.rx();
                dump.transformation.rotationY = t.ry();
                dump.transformation.rotationZ = t.rz();
                dump.transformation.scaleX = t.sx();
                dump.transformation.scaleY = t.sy();
                dump.transformation.scaleZ = t.sz();
                dump.transformation.translationX = t.tx();
                dump.transformation.translationY = t.ty();
                dump.transformation.translationZ = t.tz();
            }
            try {
                dump.unitCube = components.unitCube();
            } catch (Exception ignored) {
                // Geyser's unitCube getter lazily resolves the geometry and NPEs when it is unset
            }
            dump.placeAir = components.placeAir();
            dump.tags = components.tags();
            return dump;
        }
    }

    public static class DumpBox {
        public float originX;
        public float originY;
        public float originZ;
        public float sizeX;
        public float sizeY;
        public float sizeZ;

        public static DumpBox of(BoxComponent box) {
            DumpBox dump = new DumpBox();
            dump.originX = box.originX();
            dump.originY = box.originY();
            dump.originZ = box.originZ();
            dump.sizeX = box.sizeX();
            dump.sizeY = box.sizeY();
            dump.sizeZ = box.sizeZ();
            return dump;
        }
    }

    public static class DumpGeometry {
        public String identifier;
        public Map<String, String> boneVisibility;
    }

    public static class DumpMaterial {
        public String texture;
        public String renderMethod;
        public boolean faceDimming;
        public boolean ambientOcclusion;
    }

    public static class DumpTransform {
        public float rotationX;
        public float rotationY;
        public float rotationZ;
        public float scaleX;
        public float scaleY;
        public float scaleZ;
        public float translationX;
        public float translationY;
        public float translationZ;
    }
}
