package org.geysermc.hydraulic.entity.runtime;

import org.geysermc.conduit.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Evaluates a summarized value against a live object, calling the methods and reading the fields it
 * names through reflection.
 */
public final class ValueEvaluator {
    private final Map<String, Optional<Executable>> executables = new ConcurrentHashMap<>();
    private final Map<String, Optional<Field>> fields = new ConcurrentHashMap<>();

    /**
     * Evaluates a value.
     *
     * @param value the value, reading {@code subject} as {@link Value.Parameter} zero or {@link Value.This}
     * @param subject the object the value is computed from
     * @return the result, boxed
     * @throws UnsupportedOperationException if the value can't be evaluated
     */
    @Nullable
    public Object evaluate(@NotNull Value value, @NotNull Object subject) {
        return switch (value) {
            case Value.Literal(Object literal) -> literal;
            case Value.Parameter(int index) when index == 0 -> subject;
            case Value.This _ -> subject;
            case Value.InstanceField(Value receiver, String _, String name) -> {
                Object target = this.evaluate(receiver, subject);
                yield get(this.field(target == null ? null : target.getClass(), name), target);
            }
            case Value.Field(String owner, String name) -> get(this.field(type(owner, subject), name), null);
            case Value.InstanceCall(String _, String name, Value receiver, List<Value> args) -> {
                Object target = this.evaluate(receiver, subject);
                Object[] arguments = this.evaluateAll(args, subject);
                yield invoke(this.executable(target == null ? null : target.getClass(), name, arguments.length, false), target, arguments);
            }
            case Value.StaticCall(String owner, String name, List<Value> args) -> {
                Object[] arguments = this.evaluateAll(args, subject);
                yield invoke(this.executable(type(owner, subject), name, arguments.length, true), null, arguments);
            }
            case Value.Constructed(String type, List<Value> args) -> {
                Object[] arguments = this.evaluateAll(args, subject);
                Class<?> constructed = type(type, subject);
                // Enum constants appear as their construction in the static initializer, so they are looked up by name instead.
                Class<?> enumType = constructed.isEnum() ? constructed : constructed.getSuperclass() != null && constructed.getSuperclass().isEnum() ? constructed.getSuperclass() : null;
                if (enumType != null && arguments.length > 0 && arguments[0] instanceof String name) {
                    yield enumConstant(enumType, name);
                }
                yield invoke(this.executable(constructed, "<init>", arguments.length, false), null, arguments);
            }
            case Value.InstanceOf(Value inner, String type) -> type(type, subject).isInstance(this.evaluate(inner, subject));
            case Value.Conditional(Value condition, Value then, Value otherwise) ->
                truth(this.evaluate(condition, subject)) ? this.evaluate(then, subject) : this.evaluate(otherwise, subject);
            case Value.Operation(Value.Operator operator, List<Value> operands) -> this.operation(operator, operands, subject);
            default -> throw new UnsupportedOperationException("Can't evaluate " + value);
        };
    }

    private Object operation(@NotNull Value.Operator operator, @NotNull List<Value> operands, @NotNull Object subject) {
        switch (operator) {
            case LOGICAL_AND, LOGICAL_OR -> {
                boolean and = operator == Value.Operator.LOGICAL_AND;
                for (Value operand : operands) {
                    if (truth(this.evaluate(operand, subject)) != and) {
                        return !and;
                    }
                }
                return and;
            }
            case LOGICAL_NOT -> {
                return !truth(this.evaluate(operands.getFirst(), subject));
            }
            case CONCAT -> {
                StringBuilder joined = new StringBuilder();
                for (Value operand : operands) {
                    joined.append(this.evaluate(operand, subject));
                }
                return joined.toString();
            }
            default -> {
            }
        }

        Object a = this.evaluate(operands.getFirst(), subject);
        if (operands.size() == 1) {
            return switch (operator) {
                case NEGATE -> -number(a);
                case TRUNCATE -> (double) (long) number(a);
                case LENGTH -> (double) java.lang.reflect.Array.getLength(a);
                default -> throw new UnsupportedOperationException(operator.name());
            };
        }

        Object b = this.evaluate(operands.get(1), subject);
        if ((operator == Value.Operator.EQUAL || operator == Value.Operator.NOT_EQUAL) && !(isNumber(a) && isNumber(b))) {
            return (a == b) == (operator == Value.Operator.EQUAL);
        }

        double x = number(a);
        double y = number(b);
        return switch (operator) {
            case ADD -> x + y;
            case SUBTRACT -> x - y;
            case MULTIPLY -> x * y;
            case DIVIDE -> x / y;
            case REMAINDER -> x % y;
            case COMPARE -> (double) Double.compare(x, y);
            case EQUAL -> x == y;
            case NOT_EQUAL -> x != y;
            case LESS -> x < y;
            case LESS_OR_EQUAL -> x <= y;
            case GREATER -> x > y;
            case GREATER_OR_EQUAL -> x >= y;
            case AND -> (double) ((long) x & (long) y);
            case OR -> (double) ((long) x | (long) y);
            case XOR -> (double) ((long) x ^ (long) y);
            case SHIFT_LEFT -> (double) ((long) x << (long) y);
            case SHIFT_RIGHT -> (double) ((long) x >> (long) y);
            case UNSIGNED_SHIFT_RIGHT -> (double) ((long) x >>> (long) y);
            case INDEX -> java.lang.reflect.Array.get(a, (int) y);
            default -> throw new UnsupportedOperationException(operator.name());
        };
    }

    private Object[] evaluateAll(@NotNull List<Value> values, @NotNull Object subject) {
        List<Object> out = new ArrayList<>(values.size());
        for (Value value : values) {
            out.add(this.evaluate(value, subject));
        }
        return out.toArray();
    }

    /**
     * Converts a result to a number, as the JVM represents booleans and characters.
     *
     * @param value the result
     * @return the number
     * @throws UnsupportedOperationException if it isn't numeric
     */
    public static double number(@Nullable Object value) {
        return switch (value) {
            case Number n -> n.doubleValue();
            case Boolean b -> b ? 1 : 0;
            case Character c -> c;
            case null, default -> throw new UnsupportedOperationException("Not a number: " + value);
        };
    }

    private static boolean isNumber(@Nullable Object value) {
        return value instanceof Number || value instanceof Boolean || value instanceof Character;
    }

    private static boolean truth(@Nullable Object value) {
        return number(value) != 0;
    }

    private static Class<?> type(@NotNull String internalName, @NotNull Object subject) {
        try {
            return Class.forName(internalName.replace('/', '.'), false, subject.getClass().getClassLoader());
        } catch (ClassNotFoundException | LinkageError e) {
            throw new UnsupportedOperationException("Missing class " + internalName, e);
        }
    }

    @NotNull
    private Field field(@Nullable Class<?> owner, @NotNull String name) {
        if (owner == null) {
            throw new UnsupportedOperationException("Field " + name + " read from null");
        }

        return this.fields.computeIfAbsent(owner.getName() + "." + name, _ -> {
            for (Class<?> type = owner; type != null; type = type.getSuperclass()) {
                try {
                    Field field = type.getDeclaredField(name);
                    field.setAccessible(true);
                    return Optional.of(field);
                } catch (NoSuchFieldException | RuntimeException ignored) {
                }
            }
            return Optional.empty();
        }).orElseThrow(() -> new UnsupportedOperationException("No field " + owner.getName() + "." + name));
    }

    @NotNull
    private Executable executable(@Nullable Class<?> owner, @NotNull String name, int arity, boolean isStatic) {
        if (owner == null) {
            throw new UnsupportedOperationException("Method " + name + " called on null");
        }

        return this.executables.computeIfAbsent(owner.getName() + "." + name + "/" + arity, _ -> {
            if (name.equals("<init>")) {
                for (Constructor<?> constructor : owner.getDeclaredConstructors()) {
                    if (constructor.getParameterCount() == arity) {
                        constructor.setAccessible(true);
                        return Optional.of(constructor);
                    }
                }
                return Optional.empty();
            }

            for (Class<?> type = owner; type != null; type = type.getSuperclass()) {
                for (Method method : type.getDeclaredMethods()) {
                    if (method.getName().equals(name) && method.getParameterCount() == arity
                        && Modifier.isStatic(method.getModifiers()) == isStatic && !method.isBridge()) {
                        method.setAccessible(true);
                        return Optional.of(method);
                    }
                }
            }

            for (Method method : owner.getMethods()) {
                if (method.getName().equals(name) && method.getParameterCount() == arity) {
                    return Optional.of(method);
                }
            }

            return Optional.empty();
        }).orElseThrow(() -> new UnsupportedOperationException("No method " + owner.getName() + "." + name + "/" + arity));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object enumConstant(@NotNull Class<?> type, @NotNull String name) {
        try {
            return Enum.valueOf((Class) type, name);
        } catch (IllegalArgumentException e) {
            throw new UnsupportedOperationException("No constant " + name + " in " + type.getName(), e);
        }
    }

    private static Object get(@NotNull Field field, @Nullable Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    private static Object invoke(@NotNull Executable executable, @Nullable Object target, Object[] arguments) {
        try {
            Object[] coerced = coerce(executable.getParameterTypes(), arguments);
            return executable instanceof Constructor<?> constructor
                ? constructor.newInstance(coerced)
                : ((Method) executable).invoke(target, coerced);
        } catch (ReflectiveOperationException | IllegalArgumentException e) {
            throw new UnsupportedOperationException("Failed to call " + executable, e);
        }
    }

    // Summarized arithmetic is done in doubles, so numbers are narrowed back to what each parameter takes.
    private static Object[] coerce(Class<?>[] types, Object[] arguments) {
        Object[] out = arguments.clone();
        for (int i = 0; i < out.length; i++) {
            Class<?> type = types[i];
            if (out[i] instanceof Number || out[i] instanceof Boolean) {
                double n = number(out[i]);
                if (type == int.class || type == Integer.class) {
                    out[i] = (int) n;
                } else if (type == float.class || type == Float.class) {
                    out[i] = (float) n;
                } else if (type == long.class || type == Long.class) {
                    out[i] = (long) n;
                } else if (type == double.class || type == Double.class) {
                    out[i] = n;
                } else if (type == boolean.class || type == Boolean.class) {
                    out[i] = n != 0;
                } else if (type == short.class || type == Short.class) {
                    out[i] = (short) n;
                } else if (type == byte.class || type == Byte.class) {
                    out[i] = (byte) n;
                }
            }
        }

        return out;
    }
}
