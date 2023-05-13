package org.geysermc.hydraulic.schema.converter;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.CaseUtils;
import org.geysermc.hydraulic.schema.PackSchemaGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This class is used to convert a JSON template to a Java class.
 */
public final class JsonTemplateToClassConverter {

    /**
     * Converts a JSON template to a Java class.
     *
     * @param input the input
     * @param output the output path
     * @throws IOException if an I/O error occurs
     */
    public static void convert(@NotNull String input, @NotNull Path output, @NotNull ConverterOptions options) throws Exception {
        URL source = PackSchemaGenerator.class.getResource(input);

        try (InputStream stream = source.openStream()) {
            JsonObject schema = new JsonObject(new String(stream.readAllBytes(), StandardCharsets.UTF_8));
            String className = CaseUtils.toCamelCase(FilenameUtils.getBaseName(input), true, '_');

            // Not a full schema; usually referenced in other schema
            // files, so we just skip here.
            if (!schema.containsKey("$schema")) {
                return;
            }

            String packageName = options.rootPackage();
            TypeSpec rootClass = createClass(input, packageName, schema, schema, className, className, className, output, options);
            JavaFile javaFile = JavaFile.builder(packageName, rootClass)
                    .build();

            javaFile.writeTo(output);
        }
    }

    private static TypeSpec createClass(@NotNull String input, @NotNull String packageName, @NotNull JsonObject parentSchema, @NotNull JsonObject schema, @NotNull String rootClassName, @NotNull String prevClassName, @NotNull String className, @NotNull Path output, @NotNull ConverterOptions options) {
        // Check resolvers on root first
        ResolvedReference forwarded = resolveConditionals(input, packageName, rootClassName, prevClassName, parentSchema, schema, "<root>", options);
        if (forwarded != null) {
            return createClass(input, packageName, forwarded.parentObject(), forwarded.object(), rootClassName, prevClassName, className, output, options);
        }

        forwarded = flattenReference(
                input,
                packageName,
                rootClassName,
                prevClassName,
                parentSchema,
                schema,
                options
        );

        if (forwarded != null) {
            return createClass(input, packageName, forwarded.parentObject(), forwarded.object(), rootClassName, prevClassName, className, output, options);
        }

        // Replace illegal characters
        className = ConvertUtil.toClassName(className);
        packageName = ConvertUtil.sanitizePackageName(packageName);

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC);

        String title = schema.getString("title");
        if (title != null) {
            classBuilder.addJavadoc(title);
        }

        String description = schema.getString("description");
        if (description != null && !description.contains("UNDOCUMENTED")) {
            if (title != null) {
                classBuilder.addJavadoc("\n");
                classBuilder.addJavadoc("<p>");
                classBuilder.addJavadoc("\n");
            }
            classBuilder.addJavadoc(description);
        }

        if (schema.containsKey("properties")) {
            parseProperties(input, packageName, rootClassName, className, classBuilder, parentSchema, schema, "properties", output, options);
        }

        if (schema.getValue("additionalProperties") instanceof JsonObject value) {
            if (value.containsKey("properties")) {
                parseProperties(input, packageName, rootClassName, className, classBuilder, parentSchema, value, "properties", output, options);
            }  else {
                System.out.println("Unknown additional properties: " + value);
            }
        }

        return classBuilder.build();
    }

    private static void parseProperties(@NotNull String input, @NotNull String packageName, @NotNull String rootClassName, @NotNull String prevClassName, @NotNull TypeSpec.Builder classBuilder, @NotNull JsonObject parentSchema, @NotNull JsonObject schema, @NotNull String propertiesName, @NotNull Path output, @NotNull ConverterOptions options) {
        // Replace illegal characters
        packageName = ConvertUtil.sanitizePackageName(packageName);

        JsonObject properties = schema.getJsonObject(propertiesName);
        if (properties != null) {
            for (Map.Entry<String, Object> property : properties) {
                if (!(property.getValue() instanceof JsonObject propertyValue)) {
                    continue;
                }

                String newPackageName = packageName;
                if (!packageName.endsWith(prevClassName.toLowerCase(Locale.ROOT))) {
                    newPackageName += "." + prevClassName.toLowerCase(Locale.ROOT);
                }

                parseProperty(
                        property.getKey(),
                        newPackageName,
                        propertyValue,
                        input,
                        rootClassName,
                        prevClassName,
                        classBuilder,
                        parentSchema,
                        output,
                        options
                );
            }
        }
    }

    private static void parseProperty(@NotNull String propertyName, @NotNull String packageName, @NotNull JsonObject propertyValue, @NotNull String input, @NotNull String rootClassName, @NotNull String prevClassName, @NotNull TypeSpec.Builder classBuilder, @NotNull JsonObject parentSchema, @NotNull Path output, @NotNull ConverterOptions options) {
        String fieldName = ConvertUtil.sanitizeFieldName(propertyName);
        packageName = ConvertUtil.sanitizePackageName(packageName);

        ResolvedReference flatRef = flattenReference(
                input,
                packageName,
                rootClassName,
                prevClassName,
                parentSchema,
                propertyValue,
                options
        );

        if (flatRef != null) {
            propertyValue = flatRef.object();
            parentSchema = flatRef.parentObject();
        }

        String propertyType = propertyValue.getString("type");
        if (propertyType == null) {
            if (propertyValue.containsKey("properties") || propertyValue.containsKey("items")) {
                propertyType = "object";
            }
        }

        // Handle default Java param
        if (fieldName.equals("default")) {
            fieldName = "defaultValue";
        }

        if (propertyType != null) {
            FieldSpec.Builder spec = switch (propertyType) {
                case "string" -> FieldSpec.builder(String.class, fieldName, Modifier.PUBLIC);
                case "integer" -> FieldSpec.builder(int.class, fieldName, Modifier.PUBLIC);
                case "number" -> FieldSpec.builder(float.class, fieldName, Modifier.PUBLIC);
                case "boolean" -> FieldSpec.builder(boolean.class, fieldName, Modifier.PUBLIC);
                case "object" -> FieldSpec.builder(ClassName.get(packageName, StringUtils.capitalize(fieldName)), fieldName, Modifier.PUBLIC);
                default -> null;
            };

            if (propertyType.equals("array")) {
                FieldSpec.Builder altSpec = createArrayField(
                        input,
                        packageName,
                        fieldName,
                        rootClassName,
                        prevClassName,
                        parentSchema,
                        propertyValue,
                        output,
                        options
                );

                if (altSpec != null) {
                    spec = altSpec;
                }
            }

            if (propertyType.equals("object")) {
                FieldSpec.Builder altSpec = createObjectField(
                        packageName,
                        input,
                        fieldName,
                        rootClassName,
                        prevClassName,
                        propertyValue,
                        parentSchema,
                        output,
                        options
                );
                if (altSpec != null) {
                    spec = altSpec;
                }
            }

            if (spec != null) {
                if (!fieldName.equals(propertyName)) {
                    AnnotationSpec annotation = AnnotationSpec.builder(ClassName.get("com.fasterxml.jackson.annotation", "JsonProperty"))
                            .addMember("value", CodeBlock.of("\"" + propertyName + "\""))
                            .build();

                    spec.addAnnotation(annotation);
                }

                // TODO: Why does this happen?
                FieldSpec newSpec = spec.build();
                if (classBuilder.fieldSpecs.contains(newSpec)) {
                    // System.err.println("Tried to add duplicate field: " + newSpec.name + " to class: " + rootClassName);
                    return;
                }

                classBuilder.addField(newSpec);

                // Add getter
                MethodSpec.Builder getterBuilder = MethodSpec.methodBuilder("get" + StringUtils.capitalize(fieldName))
                        .addModifiers(Modifier.PUBLIC)
                        .returns(spec.build().type)
                        .addStatement("return this.$L", fieldName);

                String propertyDescription = propertyValue.getString("description");
                if (propertyDescription != null && !propertyDescription.contains("UNDOCUMENTED")) {
                    getterBuilder.addJavadoc(propertyDescription);
                }

                if (propertyValue.containsKey("title")) {
                    if (propertyDescription != null && !propertyDescription.contains("UNDOCUMENTED")) {
                        getterBuilder.addJavadoc("\n\n");
                    }

                    getterBuilder.addJavadoc("@return " + propertyValue.getString("title"));
                }

                classBuilder.addMethod(getterBuilder.build());

                // Add setter
                MethodSpec.Builder setterBuilder = MethodSpec.methodBuilder("set" + StringUtils.capitalize(fieldName))
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(spec.build().type, fieldName)
                        .addStatement("this.$L = $L", fieldName, fieldName);

                if (propertyDescription != null && !propertyDescription.contains("UNDOCUMENTED")) {
                    setterBuilder.addJavadoc(propertyDescription);
                }

                if (propertyValue.containsKey("title")) {
                    if (propertyDescription != null && !propertyDescription.contains("UNDOCUMENTED")) {
                        setterBuilder.addJavadoc("\n\n");
                    }

                    setterBuilder.addJavadoc("@param " + fieldName + " " + propertyValue.getString("title"));
                }

                classBuilder.addMethod(setterBuilder.build());
            }
        }

        String ref = propertyValue.getString("$ref");
        if (ref != null) {
            ResolvedReference refSchema = parseRef(input, parentSchema, propertyValue);
            if (refSchema != null) {
                String newPackageName = packageName;
                if (!packageName.endsWith(prevClassName.toLowerCase(Locale.ROOT))) {
                    newPackageName += "." + prevClassName.toLowerCase(Locale.ROOT);
                }

                parseProperty(
                        propertyName,
                        newPackageName,
                        refSchema.object(),
                        input,
                        rootClassName,
                        prevClassName,
                        classBuilder,
                        parentSchema,
                        output,
                        options
                );
            }
        }
    }

    private static FieldSpec.Builder createArrayField(@NotNull String input, @NotNull String packageName, @NotNull String fieldName, @NotNull String rootClassName, @NotNull String prevClassName, @NotNull JsonObject parentSchema, @NotNull JsonObject propertyValue, @NotNull Path output, @NotNull ConverterOptions options) {
        FieldSpec.Builder spec = null;
        if (propertyValue.getValue("items") instanceof JsonObject items) {
            ResolvedReference resolvedReference = flattenReference(
                    input,
                    packageName,
                    rootClassName,
                    prevClassName,
                    parentSchema,
                    items,
                    options
            );

            if (resolvedReference != null) {
                items = resolvedReference.object();
                parentSchema = resolvedReference.parentObject();
            }

            if (items.containsKey("type")) {
                String type = items.getString("type");
                switch (type) {
                    case "string" -> spec = FieldSpec.builder(String[].class, fieldName, Modifier.PUBLIC);
                    case "integer" -> spec = FieldSpec.builder(int[].class, fieldName, Modifier.PUBLIC);
                    case "number" -> spec = FieldSpec.builder(float[].class, fieldName, Modifier.PUBLIC);
                    case "boolean" -> spec = FieldSpec.builder(boolean[].class, fieldName, Modifier.PUBLIC);
                    case "array" -> {
                        if (propertyValue.getValue("items") instanceof JsonObject itemsObj && itemsObj.containsKey("type")) {
                            FieldSpec.Builder arraySpec = createArrayField(
                                    input,
                                    packageName,
                                    fieldName,
                                    rootClassName,
                                    prevClassName,
                                    parentSchema,
                                    itemsObj,
                                    output,
                                    options);

                            ArrayTypeName arrayType = ArrayTypeName.of(arraySpec.build().type);
                            spec = FieldSpec.builder(arrayType, fieldName, Modifier.PUBLIC);
                        }
                    }
                    case "object" -> {
                        ParameterizedTypeName mainType = ParameterizedTypeName.get(ClassName.get(List.class),
                                ClassName.get(packageName, StringUtils.capitalize(fieldName)));

                        spec = FieldSpec.builder(mainType, fieldName, Modifier.PUBLIC)
                                .initializer(CodeBlock.of("new $T<>()", ArrayList.class));

                        FieldSpec.Builder altSpec = createObjectField(
                                packageName,
                                input,
                                fieldName,
                                rootClassName,
                                prevClassName,
                                items,
                                parentSchema,
                                output,
                                options
                        );

                        if (altSpec != null) {
                            spec = altSpec;
                        }
                    }
                }
            }
        } else {
            // See if there is an item array we can pull the type from
            if (propertyValue.getValue("items") instanceof JsonArray array && array.size() > 0) {
                if (array.getValue(0) instanceof JsonObject object) {
                    String type = object.getString("type");
                    if (type == null && object.getString("$ref") != null) {
                        ResolvedReference resolvedReference = parseRef(input, parentSchema, object);
                        if (resolvedReference != null) {
                            JsonObject resolvedObject = resolvedReference.object();
                            ResolvedReference flatReference = flattenReference(
                                    input,
                                    packageName,
                                    rootClassName,
                                    prevClassName,
                                    resolvedReference.parentObject(),
                                    resolvedReference.object(),
                                    options
                            );

                            if (flatReference != null) {
                                resolvedObject = flatReference.object();
                            }

                            type = resolvedObject.getString("type");
                        }
                    }

                    if (type != null) {
                        switch (type) {
                            case "string" -> spec = FieldSpec.builder(String[].class, fieldName, Modifier.PUBLIC);
                            case "integer" -> spec = FieldSpec.builder(int[].class, fieldName, Modifier.PUBLIC);
                            case "number" -> spec = FieldSpec.builder(float[].class, fieldName, Modifier.PUBLIC);
                            case "boolean" -> spec = FieldSpec.builder(boolean[].class, fieldName, Modifier.PUBLIC);
                        }
                    }
                }
            } else {
                spec = FieldSpec.builder(String[].class, fieldName, Modifier.PUBLIC);
            }
        }

        return spec;
    }

    private static FieldSpec.Builder createObjectField(@NotNull String packageName, @NotNull String input, @NotNull String fieldName, @NotNull String rootClassName, @NotNull String prevClassName, @NotNull JsonObject propertyValue, @NotNull JsonObject parentSchema, @NotNull Path output, @NotNull ConverterOptions options) {
        String className = ConvertUtil.toClassName(fieldName);
        packageName = ConvertUtil.sanitizePackageName(packageName);

        // Check if we need to create a new class or just a HashMap.
        // If the class has an additionalProperties key without any
        // properties, the types are not explicitly defined.
        if (propertyValue.getValue("additionalProperties") instanceof JsonObject object && !object.containsKey("properties")) {
            ResolvedReference resolvedReference = flattenReference(
                    input,
                    packageName,
                    rootClassName,
                    prevClassName,
                    parentSchema,
                    object,
                    options
            );

            if (resolvedReference != null) {
                object = resolvedReference.object();
                parentSchema = resolvedReference.parentObject();
            }

            String type = object.getString("type") == null ? "object" : object.getString("type");
            TypeName classType = switch (type) {
                case "string" -> ClassName.get(String.class);
                case "integer" -> ClassName.get(Integer.class);
                case "number" -> ClassName.get(Float.class);
                case "boolean" -> ClassName.get(Boolean.class);
                case "array" -> ArrayTypeName.of(String.class);
                default -> ClassName.get(Object.class);
            };

            if (type.equals("object")) {
                TypeSpec.Builder refClass = createClass(
                        input,
                        packageName,
                        parentSchema,
                        object,
                        rootClassName,
                        prevClassName,
                        ConvertUtil.toClassName(fieldName),
                        output,
                        options
                ).toBuilder();

                JavaFile javaFile = JavaFile.builder(packageName, refClass.build())
                        .build();

                try {
                    javaFile.writeTo(output);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                classType = ClassName.get(packageName, ConvertUtil.toClassName(fieldName));
            }

            String ref = object.getString("$ref");
            if (object.getString("type") == null && ref != null) {
                ResolvedReference refSchema = parseRef(input, parentSchema, object);
                if (refSchema != null) {
                    String refClassName = ConvertUtil.toClassName(fieldName);
                    TypeSpec.Builder refClass = createClass(
                            input,
                            packageName,
                            refSchema.parentObject(),
                            refSchema.object(),
                            rootClassName,
                            prevClassName,
                            refClassName,
                            output,
                            options
                    ).toBuilder();

                    JavaFile javaFile = JavaFile.builder(packageName, refClass.build())
                            .build();

                    try {
                        javaFile.writeTo(output);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    parseProperty(
                            fieldName,
                            packageName,
                            refSchema.object(),
                            input,
                            rootClassName,
                            prevClassName,
                            refClass,
                            refSchema.parentObject(),
                            output,
                            options);

                    classType = ClassName.get(packageName, refClassName);
                }
            }

            ParameterizedTypeName mainType = ParameterizedTypeName.get(ClassName.get(Map.class),
                    ClassName.get(String.class),
                    classType);

            return FieldSpec.builder(mainType,
                            fieldName,
                            Modifier.PRIVATE)
                    .initializer(CodeBlock.of("new $T<>()", HashMap.class));
        } else if (propertyValue.getValue("items") instanceof JsonObject object && !object.containsKey("properties")) {
            ResolvedReference resolvedReference = flattenReference(
                    input,
                    packageName,
                    rootClassName,
                    prevClassName,
                    parentSchema,
                    object,
                    options
            );

            if (resolvedReference != null) {
                object = resolvedReference.object();
                parentSchema = resolvedReference.parentObject();
            }

            String ref = object.getString("$ref");
            if (ref != null) {
                ResolvedReference refSchema = parseRef(input, parentSchema, object);
                if (refSchema != null) {
                    object = refSchema.object();
                    parentSchema = refSchema.parentObject();
                }
            }

            String refClassName = ConvertUtil.toClassName(fieldName);
            TypeSpec.Builder refClass = createClass(
                    input,
                    packageName,
                    parentSchema,
                    object,
                    rootClassName,
                    prevClassName,
                    refClassName,
                    output,
                    options
            ).toBuilder();

            JavaFile javaFile = JavaFile.builder(packageName, refClass.build())
                    .build();

            try {
                javaFile.writeTo(output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ParameterizedTypeName mainType = ParameterizedTypeName.get(ClassName.get(List.class),
                    ClassName.get(packageName, StringUtils.capitalize(fieldName)));

            return FieldSpec.builder(mainType,
                            fieldName,
                            Modifier.PRIVATE)
                    .initializer(CodeBlock.of("new $T<>()", ArrayList.class));
        } else {
            TypeSpec.Builder propertyClass = createClass(
                    input,
                    packageName,
                    parentSchema,
                    propertyValue,
                    rootClassName,
                    prevClassName,
                    className,
                    output,
                    options
            ).toBuilder();

            // Field will be a map
            FieldSpec.Builder spec = null;
            if (propertyValue.getValue("additionalProperties") instanceof JsonObject object && object.containsKey("properties")) {
                ParameterizedTypeName mainType = ParameterizedTypeName.get(ClassName.get(Map.class),
                        TypeName.get(String.class),
                        ClassName.get(packageName, className));

                parseProperties(
                        input,
                        packageName,
                        rootClassName,
                        className,
                        propertyClass,
                        parentSchema,
                        object,
                        "properties",
                        output,
                        options
                );

                spec = FieldSpec.builder(mainType,
                                fieldName,
                                Modifier.PRIVATE)
                        .initializer(CodeBlock.of("new $T<>()", HashMap.class));
            }

            JavaFile javaFile = JavaFile.builder(packageName, propertyClass.build())
                    .build();

            try {
                javaFile.writeTo(output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return spec;
        }
    }

    private static ResolvedReference resolveConditionals(@NotNull String input, @NotNull String packageName, @NotNull String rootClassName, @NotNull String prevClassName, @NotNull JsonObject parentSchema, @NotNull JsonObject schema, @NotNull String type, @NotNull ConverterOptions options) {
        ResolvedReference rootResolved = resolveIf(input, schema, parentSchema, rootClassName, options);
        if (rootResolved != null) {
            return rootResolved;
        }

        if (type.equals("allOf")) {
            JsonArray array = schema.getJsonArray("allOf");
            for (Object object : array) {
                if (!(object instanceof JsonObject jsonObject)) {
                    continue;
                }

                ResolvedReference resolved = resolveIf(input, jsonObject, parentSchema, rootClassName, options);
                if (resolved != null) {
                    return resolved;
                }
            }
        } else if (type.equals("anyOf") || type.equals("oneOf")) {
            JsonArray array = schema.getJsonArray(type);
            List<String> errors = new ArrayList<>();
            int i = 0;

            // TODO: Add an option to combine objecs
            ResolvedReference resolvedRef = null;
            for (Object object : array) {
                if (!(object instanceof JsonObject jsonObject)) {
                    continue;
                }

                String title = jsonObject.getString("title", "main")
                        .toLowerCase(Locale.ROOT)
                        .replace(" ", "_");

                String relativePackage = packageName.replace(options.rootPackage(), "");
                if (!relativePackage.isEmpty()) {
                    relativePackage = relativePackage.substring(1);
                }

                String name = relativePackage + "." + rootClassName;
                if (!prevClassName.equals(rootClassName)) {
                    name += "." + prevClassName;
                }

                name += "." + title + "." + type;
                if (type.equals("oneOf")) {
                    name += "." + i++;
                }

                String anyType = jsonObject.getString("type", jsonObject.containsKey("$ref") ? "ref" : "unknown");

                Object configuredOption = options.schemaConfig().getTypes().get(name);
                if (anyType.equals(configuredOption)) {
                    return new ResolvedReference(jsonObject, parentSchema);
                } else {
                    if (jsonObject.containsKey("$ref")) {
                        resolvedRef = new ResolvedReference(jsonObject, parentSchema);
                    } else {
                        errors.add("Could not find " + name);
                        errors.add("Found " + configuredOption + " but expected " + anyType);
                    }
                }
            }

            if (resolvedRef != null) {
                return resolvedRef;
            }

            errors.forEach(System.err::println);
        }

        return null;
    }

    private static ResolvedReference resolveIf(@NotNull String input, @NotNull JsonObject jsonObject, @NotNull JsonObject parentSchema, @NotNull String rootClassName, @NotNull ConverterOptions options) {
        JsonObject ifStatement = jsonObject.getJsonObject("if");
        if (ifStatement != null) {
            Pair<String, JsonObject> ifConst = ifConst(ifStatement, rootClassName);
            if (ifConst != null) {
                Object constValue = ifConst.getValue().getValue("const");
                Object configuredOption = options.schemaConfig().getConditionals().get(ifConst.getKey());
                if (constValue.equals(configuredOption)) {
                    return parseRef(input, parentSchema, jsonObject.getJsonObject("then"));
                } else if (configuredOption == null) {
                    System.err.println("Could not find " + ifConst.getKey() + " in " + options.schemaConfig().getConditionals());
                } else {
                    if (jsonObject.getJsonObject("else") != null) {
                        return parseRef(input, parentSchema, jsonObject.getJsonObject("else"));
                    }

                    System.out.println("Skipping " + ifConst.getKey() + " because " + constValue + " != " + configuredOption);
                }
            }
        }

        return null;
    }

    @Nullable
    private static ResolvedReference parseRef(@NotNull String input, @NotNull JsonObject parentJson, @NotNull JsonObject object) {
        String ref = object.getString("$ref");

        // Definition is inside this JSON file
        if (ref.startsWith("#")) {
            JsonObject obj = parentJson;
            for (String s : ref.split("/")) {
                if (s.equals("#")) {
                    continue;
                }

                if (!obj.containsKey(s)) {
                    System.err.println("Ref: " + ref);
                    System.err.println("Could not find " + s + " in " + obj + " when parsing reference");
                    return null;
                }

                obj = obj.getJsonObject(s);
            }

            // Parse any additional references
            if (obj.containsKey("$ref")) {
                return parseRef(input, parentJson, obj);
            }

            return new ResolvedReference(obj, parentJson);
        }

        String location = FilenameUtils.separatorsToUnix(FilenameUtils.concat(FilenameUtils.getFullPathNoEndSeparator(input), ref));

        URL source = PackSchemaGenerator.class.getResource(location);
        if (source == null) {
            // No clue why this happens, since inside the JSON file
            // everything is just fine. Somewhere, somehow, an extra
            // relative path is being added for a few cases, so this
            // works around that.
            if (ref.startsWith("../")) {
                location = FilenameUtils.separatorsToUnix(FilenameUtils.concat(FilenameUtils.getFullPathNoEndSeparator(input), ref.substring(3)));
            }

            source = PackSchemaGenerator.class.getResource(location);
            if (source == null) {
                System.err.println("Could not find schema: " + location);
                return null;
            }
        }

        try (InputStream stream = source.openStream()) {
            JsonObject reference = new JsonObject(new String(stream.readAllBytes(), StandardCharsets.UTF_8));
            return new ResolvedReference(reference, reference);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private static Pair<String, JsonObject> ifConst(@NotNull JsonObject object, @NotNull String level) {
        if (object.containsKey("const")) {
            return Pair.of(level, object);
        }

        for (Map.Entry<String, Object> entry : object) {
            if (entry.getValue() instanceof JsonObject value) {
                Pair<String, JsonObject> constantValue = ifConst(value, level + "." + entry.getKey());
                if (constantValue != null) {
                    return constantValue;
                }
            }
        }

        return null;
    }

    @Nullable
    private static ResolvedReference flattenReference(@NotNull String input, @NotNull String packageName, @NotNull String rootClassName, @NotNull String prevClassName, @NotNull JsonObject parentSchema, @NotNull JsonObject schema, @NotNull ConverterOptions options) {
        ResolvedReference forwarded = null;
        if (schema.fieldNames().contains("allOf")) {
            forwarded = resolveConditionals(input, packageName, rootClassName, prevClassName, parentSchema, schema, "allOf", options);
        } else if (schema.fieldNames().contains("anyOf")) {
            forwarded = resolveConditionals(input, packageName, rootClassName, prevClassName, parentSchema, schema, "anyOf", options);
        } else if (schema.fieldNames().contains("oneOf")) {
            forwarded = resolveConditionals(input, packageName, rootClassName, prevClassName, parentSchema, schema, "oneOf", options);
        }

        return forwarded;
    }

    public record ResolvedReference(@NotNull JsonObject object, @NotNull JsonObject parentObject) {
    }
}
