package org.geysermc.hydraulic.entity.animation;

import org.geysermc.conduit.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Searches and rewrites summarized values.
 */
public final class Values {
    private Values() {
    }

    /**
     * Checks whether a value or anything it is built from matches.
     *
     * @param value the value
     * @param test the test
     * @return whether anything matches
     */
    public static boolean contains(@Nullable Value value, @NotNull Predicate<Value> test) {
        if (value == null) {
            return false;
        }

        if (test.test(value)) {
            return true;
        }

        for (Value child : children(value)) {
            if (contains(child, test)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Rewrites a value from the leaves up.
     *
     * @param value the value
     * @param mapper applied to every value, after what it is built from has been rewritten
     * @return the rewritten value
     */
    @NotNull
    public static Value map(@NotNull Value value, @NotNull Function<Value, Value> mapper) {
        Value mapped = switch (value) {
            case Value.Operation(Value.Operator operator, List<Value> operands) -> new Value.Operation(operator, mapAll(operands, mapper));
            case Value.Conditional(Value condition, Value then, Value otherwise) ->
                new Value.Conditional(map(condition, mapper), map(then, mapper), map(otherwise, mapper));
            case Value.InstanceCall(String owner, String name, Value receiver, List<Value> args) ->
                new Value.InstanceCall(owner, name, map(receiver, mapper), mapAll(args, mapper));
            case Value.StaticCall(String owner, String name, List<Value> args) -> new Value.StaticCall(owner, name, mapAll(args, mapper));
            case Value.InstanceField(Value receiver, String owner, String field) -> new Value.InstanceField(map(receiver, mapper), owner, field);
            case Value.InstanceOf(Value inner, String type) -> new Value.InstanceOf(map(inner, mapper), type);
            default -> value;
        };
        return mapper.apply(mapped);
    }

    @NotNull
    private static List<Value> mapAll(@NotNull List<Value> values, @NotNull Function<Value, Value> mapper) {
        List<Value> out = new ArrayList<>(values.size());
        for (Value value : values) {
            out.add(map(value, mapper));
        }

        return out;
    }

    @NotNull
    private static List<Value> children(@NotNull Value value) {
        return switch (value) {
            case Value.Operation(Value.Operator _, List<Value> operands) -> operands;
            case Value.Conditional(Value condition, Value then, Value otherwise) -> List.of(condition, then, otherwise);
            case Value.InstanceCall(String _, String _, Value receiver, List<Value> args) -> {
                List<Value> all = new ArrayList<>(args);
                all.add(receiver);
                yield all;
            }
            case Value.StaticCall(String _, String _, List<Value> args) -> args;
            case Value.InstanceField(Value receiver, String _, String _) -> List.of(receiver);
            case Value.InstanceOf(Value inner, String _) -> List.of(inner);
            default -> List.of();
        };
    }
}
