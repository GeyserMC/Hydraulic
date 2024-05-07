package org.geysermc.hydraulic.fabric;

import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import com.llamalad7.mixinextras.utils.MixinInternals;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import org.spongepowered.asm.util.Annotations;

import java.util.ArrayList;
import java.util.List;

/**
 * Extension to relocate classes in mixins to the Geyser shaded package.
 */
public class GeyserRelocationMixinExtension implements IExtension {
    // List of namespaces that have been relocated
    // If you want to add more look at the `relocate` tasks in https://github.com/GeyserMC/Geyser/blob/master/bootstrap/mod/fabric/build.gradle.kts
    private static final String[] RELOCATED_NAMESPACES = {
        "org/cloudburstmc/protocol"
    };

    @Override
    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    @Override
    public void preApply(ITargetClassContext context) {
        // Ignore non Geyser classes
        if (!context.getClassInfo().getName().startsWith("org/geysermc/geyser/")) return;

        for (Pair<IMixinInfo, ClassNode> pair : MixinInternals.getMixinsFor(context)) {
            for (MethodNode method : pair.getRight().methods) {
                // Replace the original classes with the relocated classes
//                for (String relocatedNamespace : RELOCATED_NAMESPACES) {
//                    method.desc = method.desc.replace("L" + relocatedNamespace, "Lorg/geysermc/geyser/shaded/" + relocatedNamespace);
//                }

                AnnotationNode original = Annotations.get(method.visibleAnnotations, "Lorg/spongepowered/asm/mixin/injection/Inject;");

                if (original == null) {
                    // Get the SugarWrapper annotation that MixinExtras adds
                    AnnotationNode sugarWrapper = Annotations.get(method.visibleAnnotations, "Lcom/llamalad7/mixinextras/sugar/impl/SugarWrapper;");
                    if (sugarWrapper == null) continue;

                    // Get the original annotation
                    original = Annotations.getValue(sugarWrapper, "original");
                    if (original == null) continue;
                }

                // Get the method annotation
                List<String> methods = Annotations.getValue(original, "method");
                if (!(methods == null || methods.isEmpty())) {
                    List<String> newMethods = new ArrayList<>();
                    // Loop over the method annotations and replace the value
                    for (String methodValue : methods) {
                        // Replace the original classes with the relocated classes
                        for (String relocatedNamespace : RELOCATED_NAMESPACES) {
                            methodValue = methodValue.replace("L" + relocatedNamespace, "Lorg/geysermc/geyser/shaded/" + relocatedNamespace);
                        }

                        newMethods.add(methodValue);
                    }

                    Annotations.setValue(original, "method", newMethods);
                }

                // Get the at annotation
                List<AnnotationNode> at = Annotations.getValue(original, "at");
                if (!(at == null || at.isEmpty())) {
                    // Loop over the at annotations and replace the target value
                    for (AnnotationNode annotationNode : at) {
                        String value = Annotations.getValue(annotationNode, "target");
                        if (value == null) continue;

                        // Replace the original classes with the relocated classes
                        for (String relocatedNamespace : RELOCATED_NAMESPACES) {
                            value = value.replace("L" + relocatedNamespace, "Lorg/geysermc/geyser/shaded/" + relocatedNamespace);
                        }

                        Annotations.setValue(annotationNode, "target", value);
                    }
                }
            }
        }
    }

    @Override
    public void postApply(ITargetClassContext context) {
    }

    @Override
    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {
    }
}
