package org.geysermc.hydraulic.entity.animation;

import org.geysermc.conduit.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Compiles summarized Java expressions into Molang. Subexpressions shared between the compiled
 * expressions are computed once, as variables set before the animation runs.
 */
public final class MolangCompiler {
    static final double DEGREES = 180 / Math.PI;
    private static final double RADIANS = Math.PI / 180;
    private static final int HOIST_SIZE = 4;
    private static final double EPSILON = 1e-6;
    private static final MathContext PRECISION = new MathContext(9);

    private final Inputs inputs;
    private final String prefix;
    private final Map<Value, Integer> references = new IdentityHashMap<>();
    private final Map<Value, Molang> compiled = new IdentityHashMap<>();
    private final List<String> variables = new ArrayList<>();

    /**
     * Creates a compiler.
     *
     * @param inputs resolves the values an expression reads
     * @param prefix the prefix of the variables shared subexpressions are stored in
     */
    public MolangCompiler(@NotNull Inputs inputs, @NotNull String prefix) {
        this.inputs = inputs;
        this.prefix = prefix;
    }

    /**
     * Counts the references an expression makes, so subexpressions it shares with others are
     * computed once. Every expression must be added before any is compiled.
     *
     * @param value the expression
     */
    public void add(@NotNull Value value) {
        if (this.references.merge(value, 1, Integer::sum) > 1) {
            return;
        }

        for (Value child : children(value)) {
            this.add(child);
        }
    }

    /**
     * Compiles an expression.
     *
     * @param value the expression
     * @return the Molang, or null if the expression reads something with no Molang equivalent
     */
    @Nullable
    public String compile(@NotNull Value value) {
        Molang molang = this.molang(value);
        return molang == null ? null : molang.text();
    }

    /**
     * Gets the statements setting the variables compiled expressions read.
     *
     * @return the statements, in the order they must run
     */
    @NotNull
    public List<String> variables() {
        return List.copyOf(this.variables);
    }

    @Nullable
    private Molang molang(@NotNull Value value) {
        if (this.compiled.containsKey(value)) {
            return this.compiled.get(value);
        }

        Molang molang = this.translate(value);
        if (molang == null) {
            String property = this.inputs.property(value);
            molang = property == null ? null : Molang.of(property);
        }

        if (molang != null && molang.constant() == null
                && this.references.getOrDefault(value, 0) > 1 && size(value) >= HOIST_SIZE) {
            String variable = "v." + this.prefix + this.variables.size();
            this.variables.add(variable + " = " + molang.text() + ";");
            molang = Molang.of(variable);
        }

        this.compiled.put(value, molang);
        return molang;
    }

    @Nullable
    private Molang translate(@NotNull Value value) {
        return switch (value) {
            case Value.Literal(Object literal) -> literal(literal);
            case Value.InstanceField(Value.Parameter(int index), String _, String field) when index == 0 -> {
                String state = this.inputs.state(field);
                yield state == null ? null : Molang.of(state);
            }

            case Value.InstanceField(Value receiver, String _, String field) -> {
                Double rest = this.inputs.rest(receiver, field);
                yield rest == null ? null : number(rest);
            }

            case Value.Conditional(Value condition, Value then, Value otherwise) -> {
                Molang test = this.molang(condition);
                if (test != null && test.constant() != null) {
                    yield this.molang(test.constant() != 0 ? then : otherwise);
                }

                Molang a = this.molang(then);
                Molang b = this.molang(otherwise);
                yield test == null || a == null || b == null ? null : Molang.of("(" + test.text() + " ? " + a.text() + " : " + b.text() + ")");
            }

            case Value.Operation(Value.Operator operator, List<Value> operands) -> this.operation(operator, operands);
            case Value.StaticCall(String owner, String name, List<Value> args) -> this.call(owner, name, args);
            default -> null;
        };
    }

    @Nullable
    private Molang operation(@NotNull Value.Operator operator, @NotNull List<Value> operands) {
        List<Molang> args = this.molangs(operands);
        if (args == null) {
            return null;
        }

        return switch (operator) {
            case ADD -> add(args.get(0), args.get(1));
            case SUBTRACT -> add(args.get(0), args.get(1).scale(-1));
            case MULTIPLY -> multiply(args.get(0), args.get(1));
            case DIVIDE -> args.get(1).constant() != null && args.get(1).constant() != 0
                ? args.get(0).scale(1 / args.get(1).constant())
                : Molang.of("(" + args.get(0).text() + " / " + args.get(1).text() + ")");
            case REMAINDER -> function("math.mod", args);
            case NEGATE -> args.getFirst().scale(-1);
            case TRUNCATE -> function("math.trunc", args);
            case EQUAL -> compare("==", args, (a, b) -> a.equals(b));
            case NOT_EQUAL -> compare("!=", args, (a, b) -> !a.equals(b));
            case LESS -> compare("<", args, (a, b) -> a < b);
            case LESS_OR_EQUAL -> compare("<=", args, (a, b) -> a <= b);
            case GREATER -> compare(">", args, (a, b) -> a > b);
            case GREATER_OR_EQUAL -> compare(">=", args, (a, b) -> a >= b);
            case LOGICAL_AND -> join(" && ", args);
            case LOGICAL_OR -> join(" || ", args);
            case LOGICAL_NOT -> args.getFirst().constant() != null ? number(args.getFirst().constant() == 0 ? 1 : 0) : Molang.of("!" + args.getFirst().text());
            default -> null;
        };
    }

    @Nullable
    private Molang call(@NotNull String owner, @NotNull String name, @NotNull List<Value> operands) {
        if (!owner.equals("net/minecraft/util/Mth") && !owner.equals("java/lang/Math") && !owner.equals("java/lang/StrictMath")) {
            return null;
        }

        List<Molang> args = this.molangs(operands);
        if (args == null) {
            return null;
        }

        return switch (name) {
            case "sin", "cos" -> function("math." + name, List.of(args.getFirst().scale(DEGREES)));
            case "abs", "sqrt", "floor", "ceil", "round", "exp", "min", "max", "pow", "clamp" -> function("math." + name, args);
            case "lerp" -> args.size() == 3 ? function("math.lerp", List.of(args.get(1), args.get(2), args.get(0))) : null;
            case "atan2" -> function("math.atan2", args).scale(RADIANS);
            case "toRadians" -> args.getFirst().scale(RADIANS);
            case "toDegrees" -> args.getFirst().scale(DEGREES);
            case "square" -> multiply(args.getFirst(), args.getFirst());
            default -> null;
        };
    }

    @Nullable
    private List<Molang> molangs(@NotNull List<Value> values) {
        List<Molang> molangs = new ArrayList<>(values.size());
        for (Value value : values) {
            Molang molang = this.molang(value);
            if (molang == null) {
                return null;
            }

            molangs.add(molang);
        }

        return molangs;
    }

    @NotNull
    private static Molang add(@NotNull Molang a, @NotNull Molang b) {
        if (a.constant() != null) {
            return b.shift(a.constant());
        }

        if (b.constant() != null) {
            return a.shift(b.constant());
        }

        if (a.base().equals(b.base())) {
            return new Molang(a.base(), a.factor() + b.factor(), a.offset() + b.offset());
        }

        return Molang.of("(" + a.text() + " + " + b.text() + ")");
    }

    @NotNull
    private static Molang multiply(@NotNull Molang a, @NotNull Molang b) {
        if (a.constant() != null) {
            return b.scale(a.constant());
        }

        if (b.constant() != null) {
            return a.scale(b.constant());
        }

        if (Math.abs(a.offset()) < EPSILON && Math.abs(b.offset()) < EPSILON) {
            return new Molang("(" + a.base() + " * " + b.base() + ")", a.factor() * b.factor(), 0);
        }

        return Molang.of("(" + a.text() + " * " + b.text() + ")");
    }

    @NotNull
    private static Molang compare(@NotNull String symbol, @NotNull List<Molang> args, @NotNull Comparison comparison) {
        Molang a = args.get(0);
        Molang b = args.get(1);
        if (a.constant() != null && b.constant() != null) {
            return number(comparison.test(a.constant(), b.constant()) ? 1 : 0);
        }

        return Molang.of("(" + a.text() + " " + symbol + " " + b.text() + ")");
    }

    @NotNull
    private static Molang join(@NotNull String symbol, @NotNull List<Molang> args) {
        return Molang.of("(" + texts(args, symbol) + ")");
    }

    @NotNull
    private static Molang function(@NotNull String name, @NotNull List<Molang> args) {
        return Molang.of(name + "(" + texts(args, ", ") + ")");
    }

    @NotNull
    private static String texts(@NotNull List<Molang> args, @NotNull String separator) {
        List<String> texts = new ArrayList<>(args.size());
        for (Molang arg : args) {
            texts.add(arg.text());
        }

        return String.join(separator, texts);
    }

    @Nullable
    private static Molang literal(@Nullable Object literal) {
        return switch (literal) {
            case Boolean b -> number(b ? 1 : 0);
            case Number n -> Double.isFinite(n.doubleValue()) ? number(n instanceof Float f ? Double.parseDouble(f.toString()) : n.doubleValue()) : null;
            case null, default -> null;
        };
    }

    @NotNull
    static Molang number(double value) {
        return new Molang(null, 0, value);
    }

    @NotNull
    private static String format(double value) {
        String text = BigDecimal.valueOf(value).round(PRECISION).stripTrailingZeros().toPlainString();
        return text.equals("-0") ? "0" : text;
    }

    private static int size(@NotNull Value value) {
        int size = 1;
        for (Value child : children(value)) {
            size += size(child);
            if (size >= HOIST_SIZE) {
                return size;
            }
        }

        return size;
    }

    @NotNull
    private static List<Value> children(@NotNull Value value) {
        return switch (value) {
            case Value.Operation(Value.Operator _, List<Value> operands) -> operands;
            case Value.Conditional(Value condition, Value then, Value otherwise) -> List.of(condition, then, otherwise);
            case Value.StaticCall(String _, String _, List<Value> args) -> args;
            default -> List.of();
        };
    }

    /**
     * Resolves the values an expression reads.
     */
    public interface Inputs {
        /**
         * Gets the Molang for a field of the state passed to the summarized method.
         *
         * @param field the field
         * @return the Molang, or null if it has no equivalent
         */
        @Nullable
        String state(@NotNull String field);

        /**
         * Gets the value a field held before the summarized method ran.
         *
         * @param receiver the object the field belongs to
         * @param field the field
         * @return the value, or null if it isn't known
         */
        @Nullable
        Double rest(@NotNull Value receiver, @NotNull String field);

        /**
         * Gets the Molang reading a value the client can't compute itself, once it has been sent as
         * an entity property.
         *
         * @param value an expression with no Molang equivalent
         * @return the Molang, or null if it can't be sent either
         */
        @Nullable
        String property(@NotNull Value value);
    }

    // base * factor + offset, or the constant offset alone when base is null.
    record Molang(@Nullable String base, double factor, double offset) {
        static Molang of(@NotNull String text) {
            return new Molang(text, 1, 0);
        }

        @Nullable
        Double constant() {
            return this.base == null || this.factor == 0 ? this.offset : null;
        }

        Molang scale(double by) {
            return this.base == null ? number(this.offset * by) : new Molang(this.base, this.factor * by, this.offset * by);
        }

        Molang shift(double by) {
            return new Molang(this.base, this.factor, this.offset + by);
        }

        @NotNull
        String text() {
            Double constant = this.constant();
            if (constant != null) {
                return format(constant);
            }

            String term;
            if (Math.abs(this.factor - 1) < EPSILON) {
                term = this.base;
            } else if (Math.abs(this.factor + 1) < EPSILON) {
                term = "-" + this.base;
            } else {
                term = "(" + this.base + " * " + format(this.factor) + ")";
            }

            if (Math.abs(this.offset) < EPSILON) {
                return term;
            }

            return "(" + term + (this.offset < 0 ? " - " + format(-this.offset) : " + " + format(this.offset)) + ")";
        }
    }

    @FunctionalInterface
    private interface Comparison {
        boolean test(Double a, Double b);
    }
}
