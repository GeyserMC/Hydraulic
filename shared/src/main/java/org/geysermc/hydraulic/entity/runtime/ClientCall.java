package org.geysermc.hydraulic.entity.runtime;

import org.geysermc.conduit.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A call an entity only makes on the client, such as starting an {@code AnimationState}, repeated
 * on the server so that its effect can be sent to Bedrock players.
 *
 * @param call the call, made on the entity as {@link Value.This}
 * @param condition when it is made, with the client check already passed, or null if always
 */
public record ClientCall(@NotNull Value call, @Nullable Value condition) {
}
