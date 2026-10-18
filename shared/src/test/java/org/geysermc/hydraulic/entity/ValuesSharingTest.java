package org.geysermc.hydraulic.entity;

import org.geysermc.conduit.Value;
import org.geysermc.hydraulic.entity.animation.Values;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValuesSharingTest {
    @Test
    @Timeout(5)
    void sharedValuesAreWalkedOnce() {
        Value state = new Value.Parameter(0);
        Value value = state;
        for (int i = 0; i < 60; i++) {
            value = new Value.Conditional(value, value, new Value.Operation(Value.Operator.ADD, List.of(value, value)));
        }

        Value one = Value.literal(1f);
        Value mapped = Values.map(value, node -> state.equals(node) ? one : node);

        assertTrue(Values.contains(mapped, one::equals));
        assertFalse(Values.contains(mapped, state::equals));
        Value.Conditional top = (Value.Conditional) mapped;
        assertSame(top.condition(), top.then());
    }
}
