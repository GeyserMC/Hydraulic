package org.geysermc.hydraulic.schema.converter;

import java.util.HashMap;
import java.util.Map;

public class SchemaConfig {

    private final Map<String, Object> conditionals = new HashMap<>();
    private final Map<String, Object> types = new HashMap<>();

    public Map<String, Object> getConditionals() {
        return conditionals;
    }

    public Map<String, Object> getTypes() {
        return types;
    }
}
