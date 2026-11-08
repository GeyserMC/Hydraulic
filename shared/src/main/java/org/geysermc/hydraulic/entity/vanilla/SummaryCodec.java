package org.geysermc.hydraulic.entity.vanilla;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.geysermc.conduit.Summary;
import org.geysermc.conduit.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Writes summaries to JSON and reads them back. Values are stored as a table of nodes, children
 * before the values built from them, so a subexpression shared between values is stored once.
 */
public final class SummaryCodec {
    private final JsonArray nodes = new JsonArray();
    private final Map<Value, Integer> written = new IdentityHashMap<>();

    private SummaryCodec() {
    }

    /**
     * Writes a summary.
     *
     * @param summary the summary
     * @return the summary as JSON
     */
    @NotNull
    public static JsonObject write(@NotNull Summary summary) {
        SummaryCodec codec = new SummaryCodec();
        JsonArray assignments = new JsonArray();
        for (Summary.Assignment assignment : summary.assignments()) {
            JsonObject json = new JsonObject();
            json.addProperty("target", codec.node(assignment.target()));
            json.addProperty("class", assignment.className());
            json.addProperty("field", assignment.field());
            json.addProperty("value", codec.node(assignment.value()));
            assignments.add(json);
        }

        JsonArray invocations = new JsonArray();
        for (Summary.Invocation invocation : summary.invocations()) {
            JsonObject json = new JsonObject();
            json.addProperty("condition", codec.node(invocation.condition()));
            json.addProperty("call", codec.node(invocation.call()));
            invocations.add(json);
        }

        JsonObject json = new JsonObject();
        json.addProperty("returned", codec.node(summary.returned()));
        json.add("assignments", assignments);
        json.add("invocations", invocations);
        json.add("nodes", codec.nodes);
        return json;
    }

    /**
     * Reads a summary written by {@link #write(Summary)}.
     *
     * @param json the summary as JSON
     * @return the summary
     */
    @NotNull
    public static Summary read(@NotNull JsonObject json) {
        List<Value> nodes = new ArrayList<>();
        for (JsonElement node : json.getAsJsonArray("nodes")) {
            nodes.add(value(node.getAsJsonArray(), nodes));
        }

        List<Summary.Assignment> assignments = new ArrayList<>();
        for (JsonElement element : json.getAsJsonArray("assignments")) {
            JsonObject assignment = element.getAsJsonObject();
            assignments.add(new Summary.Assignment(at(nodes, assignment.get("target")), assignment.get("class").getAsString(),
                assignment.get("field").getAsString(), at(nodes, assignment.get("value"))));
        }

        List<Summary.Invocation> invocations = new ArrayList<>();
        for (JsonElement element : json.getAsJsonArray("invocations")) {
            JsonObject invocation = element.getAsJsonObject();
            invocations.add(new Summary.Invocation(at(nodes, invocation.get("condition")), at(nodes, invocation.get("call"))));
        }

        return new Summary(at(nodes, json.get("returned")), List.copyOf(assignments), List.copyOf(invocations));
    }

    private int node(@Nullable Value value) {
        if (value == null) {
            return -1;
        }

        Integer existing = this.written.get(value);
        if (existing != null) {
            return existing;
        }

        JsonArray node = new JsonArray();
        switch (value) {
            case Value.Literal(Object literal) -> {
                node.add("lit");
                literal(node, literal);
            }

            case Value.This _ -> node.add("this");
            case Value.Parameter(int index) -> {
                node.add("param");
                node.add(index);
            }

            case Value.InstanceField(Value receiver, String owner, String field) -> {
                int receiverNode = this.node(receiver);
                node.add("ifield");
                node.add(receiverNode);
                node.add(owner);
                node.add(field);
            }

            case Value.Field(String owner, String field) -> {
                node.add("field");
                node.add(owner);
                node.add(field);
            }

            case Value.Operation(Value.Operator operator, List<Value> operands) -> {
                JsonArray children = this.nodes(operands);
                node.add("op");
                node.add(operator.name());
                node.add(children);
            }

            case Value.Conditional(Value condition, Value then, Value otherwise) -> {
                int a = this.node(condition);
                int b = this.node(then);
                int c = this.node(otherwise);
                node.add("cond");
                node.add(a);
                node.add(b);
                node.add(c);
            }

            case Value.StaticCall(String owner, String name, List<Value> args) -> {
                JsonArray children = this.nodes(args);
                node.add("call");
                node.add(owner);
                node.add(name);
                node.add(children);
            }

            case Value.InstanceCall(String owner, String name, Value receiver, List<Value> args) -> {
                int receiverNode = this.node(receiver);
                JsonArray children = this.nodes(args);
                node.add("icall");
                node.add(owner);
                node.add(name);
                node.add(receiverNode);
                node.add(children);
            }

            case Value.Constructed(String type, List<Value> args) -> {
                JsonArray children = this.nodes(args);
                node.add("new");
                node.add(type);
                node.add(children);
            }

            case Value.InstanceOf(Value inner, String type) -> {
                int innerNode = this.node(inner);
                node.add("instanceof");
                node.add(innerNode);
                node.add(type);
            }

            case Value.Array(String type, List<Value> elements) -> {
                JsonArray children = this.nodes(elements);
                node.add("array");
                node.add(type);
                node.add(children);
            }

            case Value.Multiple(List<Value> values) -> {
                JsonArray children = this.nodes(values);
                node.add("multiple");
                node.add(children);
            }

            case Value.Uninitialized(String type) -> {
                node.add("uninit");
                node.add(type);
            }

            case Value.Lambda lambda -> {
                JsonArray captures = this.nodes(lambda.captures());
                node.add("lambda");
                node.add(lambda.name());
                node.add(lambda.descriptor());
                node.add(lambda.implOwner() == null ? JsonNull.INSTANCE : new JsonPrimitive(lambda.implOwner()));
                node.add(lambda.implMethod() == null ? JsonNull.INSTANCE : new JsonPrimitive(lambda.implMethod()));
                node.add(lambda.implDescriptor() == null ? JsonNull.INSTANCE : new JsonPrimitive(lambda.implDescriptor()));
                node.add(lambda.implStatic());
                node.add(captures);
            }

            case Value.Unknown(String reason) -> {
                node.add("unknown");
                node.add(reason);
            }
        }

        this.nodes.add(node);
        int index = this.nodes.size() - 1;
        this.written.put(value, index);
        return index;
    }

    private JsonArray nodes(@NotNull List<Value> values) {
        JsonArray array = new JsonArray();
        for (Value value : values) {
            array.add(this.node(value));
        }

        return array;
    }

    private static void literal(@NotNull JsonArray node, @Nullable Object literal) {
        switch (literal) {
            case null -> node.add("n");
            case String s -> {
                node.add("s");
                node.add(s);
            }

            case Integer i -> {
                node.add("i");
                node.add(i);
            }

            case Long l -> {
                node.add("l");
                node.add(l);
            }

            case Float f -> {
                node.add("f");
                node.add(f);
            }

            case Double d -> {
                node.add("d");
                node.add(d);
            }

            case Boolean b -> {
                node.add("z");
                node.add(b);
            }

            case Character c -> {
                node.add("c");
                node.add((int) c);
            }

            default -> {
                node.add("s");
                node.add(literal.toString());
            }
        }
    }

    @NotNull
    private static Value value(@NotNull JsonArray node, @NotNull List<Value> nodes) {
        return switch (node.get(0).getAsString()) {
            case "lit" -> Value.literal(switch (node.get(1).getAsString()) {
                case "n" -> null;
                case "s" -> node.get(2).getAsString();
                case "i" -> node.get(2).getAsInt();
                case "l" -> node.get(2).getAsLong();
                case "f" -> node.get(2).getAsFloat();
                case "d" -> node.get(2).getAsDouble();
                case "z" -> node.get(2).getAsBoolean();
                case "c" -> (char) node.get(2).getAsInt();
                default -> throw new IllegalArgumentException("Unknown literal " + node);
            });
            case "this" -> new Value.This();
            case "param" -> new Value.Parameter(node.get(1).getAsInt());
            case "ifield" -> new Value.InstanceField(nodes.get(node.get(1).getAsInt()), node.get(2).getAsString(), node.get(3).getAsString());
            case "field" -> new Value.Field(node.get(1).getAsString(), node.get(2).getAsString());
            case "op" -> new Value.Operation(Value.Operator.valueOf(node.get(1).getAsString()), all(node.get(2), nodes));
            case "cond" -> new Value.Conditional(nodes.get(node.get(1).getAsInt()), nodes.get(node.get(2).getAsInt()), nodes.get(node.get(3).getAsInt()));
            case "call" -> new Value.StaticCall(node.get(1).getAsString(), node.get(2).getAsString(), all(node.get(3), nodes));
            case "icall" -> new Value.InstanceCall(node.get(1).getAsString(), node.get(2).getAsString(),
                nodes.get(node.get(3).getAsInt()), all(node.get(4), nodes));
            case "new" -> new Value.Constructed(node.get(1).getAsString(), all(node.get(2), nodes));
            case "instanceof" -> new Value.InstanceOf(nodes.get(node.get(1).getAsInt()), node.get(2).getAsString());
            case "array" -> new Value.Array(node.get(1).getAsString(), new ArrayList<>(all(node.get(2), nodes)));
            case "multiple" -> new Value.Multiple(all(node.get(1), nodes));
            case "uninit" -> new Value.Uninitialized(node.get(1).getAsString());
            case "lambda" -> new Value.Lambda(node.get(1).getAsString(), node.get(2).getAsString(), string(node.get(3)),
                string(node.get(4)), string(node.get(5)), node.get(6).getAsBoolean(), all(node.get(7), nodes));
            case "unknown" -> new Value.Unknown(node.get(1).getAsString());
            default -> throw new IllegalArgumentException("Unknown node " + node);
        };
    }

    @Nullable
    private static Value at(@NotNull List<Value> nodes, @NotNull JsonElement index) {
        int i = index.getAsInt();
        return i < 0 ? null : nodes.get(i);
    }

    @NotNull
    private static List<Value> all(@NotNull JsonElement indices, @NotNull List<Value> nodes) {
        List<Value> values = new ArrayList<>();
        for (JsonElement index : indices.getAsJsonArray()) {
            values.add(nodes.get(index.getAsInt()));
        }

        return List.copyOf(values);
    }

    @Nullable
    private static String string(@NotNull JsonElement element) {
        return element.isJsonNull() ? null : element.getAsString();
    }
}
