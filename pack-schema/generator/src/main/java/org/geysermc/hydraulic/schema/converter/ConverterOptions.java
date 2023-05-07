package org.geysermc.hydraulic.schema.converter;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class ConverterOptions {
    private final MultiConvertType multiConvertType;
    private final String collisionPrefix;
    private final SchemaConfig schemaConfig;

    private ConverterOptions(MultiConvertType multiConvertType, String collisionPrefix, String schemaConfig) {
        this.multiConvertType = multiConvertType;
        this.collisionPrefix = collisionPrefix;

        InputStream schemaResource = ConverterOptions.class.getResourceAsStream("/" + schemaConfig);
        try {
            this.schemaConfig = new ObjectMapper().readValue(schemaResource, SchemaConfig.class);
        } catch (IOException ex) {
            throw new RuntimeException("An error occurred reading schema config " + schemaConfig, ex);
        }
    }

    public MultiConvertType multiConvertType() {
        return this.multiConvertType;
    }

    public String collisionPrefix() {
        return this.collisionPrefix;
    }

    public SchemaConfig schemaConfig() {
        return this.schemaConfig;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MultiConvertType multiConvertType = MultiConvertType.SIMPLE;
        private String collisionPrefix = "Json";
        private String schemaConfig = "schema-config.json";

        public Builder multiConvertType(MultiConvertType multiConvertType) {
            this.multiConvertType = multiConvertType;
            return this;
        }

        public Builder collisionPrefix(String collisionPrefix) {
            this.collisionPrefix = collisionPrefix;
            return this;
        }

        public Builder schemaConfig(String schemaConfig) {
            this.schemaConfig = schemaConfig;
            return this;
        }

        public ConverterOptions build() {
            return new ConverterOptions(this.multiConvertType, this.collisionPrefix, this.schemaConfig);
        }
    }
}
