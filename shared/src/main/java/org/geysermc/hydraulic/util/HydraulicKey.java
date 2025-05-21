package org.geysermc.hydraulic.util;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.geysermc.geyser.api.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HydraulicKey implements Identifier, Key {
    private String namespace;
    private String path;

    public HydraulicKey(@NotNull String namespace, @NotNull String path) {
        this.namespace = namespace;
        this.path = path;
    }

    private HydraulicKey(Identifier id) {
        this.namespace = id.namespace();
        this.path = id.path();
    }

    private HydraulicKey(Key key) {
        this.namespace = key.namespace();
        this.path = key.value();
    }

    private HydraulicKey(ResourceLocation location) {
        this.namespace = location.getNamespace();
        this.path = location.getPath();
    }

    public static HydraulicKey of(@Nullable Identifier id) {
        if (id == null) return null;
        return new HydraulicKey(id);
    }

    public static HydraulicKey of(@Nullable Key key) {
        if (key == null) return null;
        return new HydraulicKey(key);
    }

    public static HydraulicKey of(@Nullable ResourceLocation location) {
        if (location == null) return null;
        return new HydraulicKey(location);
    }

    public static HydraulicKey of(@Nullable ResourceKey<?> key) {
        if (key == null) return null;
        return of(key.location());
    }

    @Override // Adventure
    public @NotNull String asString() {
        return namespace() + path();
    }

    @KeyPattern.Namespace
    @Override // Adventure + Geyser
    public @NotNull String namespace() {
        return this.namespace;
    }

    @KeyPattern.Value
    @Override // Adventure
    public @NotNull String value() {
        return this.path;
    }

    @Override // Geyser
    public String path() {
        return this.path;
    }

    public ResourceLocation location() {
        return ResourceLocation.fromNamespaceAndPath(this.namespace, this.path);
    }

    public void namespace(String namespace) {
        this.namespace = namespace;
    }

    public void path(String path) {
        this.path = path;
    }

    // Messy, but makes everyone's life so much easier
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        else if (other instanceof HydraulicKey key) {
            return key.namespace.equals(this.namespace) && key.path.equals(this.path);
        } else if (other instanceof Identifier id) {
            return id.namespace().equals(this.namespace) && id.path().equals(this.path);
        } else if (other instanceof Key key) {
            return key.namespace().equals(this.namespace) && key.value().equals(this.path);
        } else if (other instanceof ResourceLocation location) {
            return location.getNamespace().equals(this.namespace) && location.getPath().equals(this.path);
        } else if (other instanceof ResourceKey<?> key) {
            ResourceLocation location = key.location();
            return location.getNamespace().equals(this.namespace) && location.getPath().equals(this.path);
        }

        return false;
    }
}
