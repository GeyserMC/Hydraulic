package org.geysermc.hydraulic.entity;

import org.geysermc.conduit.ConduitContext;
import org.geysermc.conduit.Summary;
import org.geysermc.conduit.Value;
import org.geysermc.conduit.VirtualMachine;
import org.geysermc.conduit.VirtualMachineFactory;
import org.geysermc.hydraulic.entity.animation.BoneMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class BoneMapTest {
    private static VirtualMachine vm;
    private static Map<String, CustomEntity> entities;

    @BeforeAll
    static void scan() {
        ConduitContext context = TestMod.context();
        vm = VirtualMachineFactory.create(context).await();
        entities = TestMod.scan(context);
    }

    @Test
    void fieldsResolveToBones() {
        BoneMap bones = bones("golem");
        BoneMap.Bone antenna = bones.bone(field("GolemModel", "antenna"));
        assertNotNull(antenna);
        assertEquals("antenna", antenna.name());
        assertNull(bones.bone(field("GolemModel", "missing")));
    }

    @Test
    void arrayElementsResolveToBones() {
        BoneMap bones = bones("critter");
        Value leg = new Value.Operation(Value.Operator.INDEX, List.of(field("CritterModel", "legs"), Value.literal(2)));
        BoneMap.Bone bone = bones.bone(leg);
        assertNotNull(bone);
        assertEquals("leg2", bone.part().name());
    }

    @Test
    void everyAnimatedPartResolves() {
        for (CustomEntity entity : entities.values()) {
            Summary setupAnim = vm.summarize(entity.binding().modelClass(), "setupAnim", null);
            assertNotNull(setupAnim, entity.id()::toString);
            BoneMap bones = bones(entity.id().getPath());
            for (Summary.Assignment assignment : setupAnim.assignments()) {
                if (assignment.target() instanceof Value.This) {
                    continue;
                }
                assertNotNull(bones.bone(assignment.target()), () -> entity.id() + " " + assignment.target() + "." + assignment.field());
            }
        }
    }

    private static BoneMap bones(String entity) {
        CustomEntity custom = entities.get(entity);
        Summary constructor = vm.summarize(custom.binding().modelClass(), "<init>", null);
        assertNotNull(constructor);
        return BoneMap.of(constructor, custom.model());
    }

    private static Value field(String model, String name) {
        return new Value.InstanceField(new Value.This(), TestMod.CLIENT + model, name);
    }
}
