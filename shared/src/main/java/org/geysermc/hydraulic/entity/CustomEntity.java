package org.geysermc.hydraulic.entity;

import net.minecraft.resources.Identifier;
import org.geysermc.conduit.Value;
import org.geysermc.hydraulic.entity.animation.AnimationCompiler.CompiledAnimation;
import org.geysermc.hydraulic.entity.animation.ModelBinding;
import org.geysermc.hydraulic.entity.conduit.EntityModel;
import org.geysermc.hydraulic.entity.runtime.ClientCall;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A modded entity type, with everything its client-side renderer was found to draw it with.
 *
 * @param id the entity type's identifier
 * @param model the geometry it is drawn with
 * @param textures the textures it may be drawn with, one per variant, possibly empty
 * @param binding the model class and layer its renderer uses, or null if it could not be found
 * @param animation the animation its model's code was compiled to, or null if there is none
 * @param texture computes which of the textures the entity is drawn with, from the entity, or null if
 *                that isn't known
 * @param clientCalls calls the entity only makes on the client that its animations depend on
 */
public record CustomEntity(@NotNull Identifier id, @NotNull EntityModel model, @NotNull List<String> textures,
                           @Nullable ModelBinding binding, @Nullable CompiledAnimation animation, @Nullable Value texture,
                           @NotNull List<ClientCall> clientCalls
) {
}
