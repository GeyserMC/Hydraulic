package org.geysermc.hydraulic.entity.animation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * The Molang equivalents of the fields vanilla's entity render states hold.
 */
public final class RenderStateQueries {
    private static final Map<String, String> QUERIES = Map.ofEntries(
        Map.entry("walkAnimationPos", "query.modified_distance_moved"),
        Map.entry("walkAnimationSpeed", "query.modified_move_speed"),
        Map.entry("ageInTicks", "(query.life_time * 20)"),
        Map.entry("yRot", "query.target_y_rotation"),
        Map.entry("xRot", "query.target_x_rotation"),
        Map.entry("bodyRot", "query.body_y_rotation"),
        Map.entry("deathTime", "query.death_ticks"),
        Map.entry("isBaby", "query.is_baby"),
        Map.entry("isInWater", "query.is_in_water"),
        Map.entry("isDiscrete", "query.is_sneaking"),
        Map.entry("isInvisible", "query.is_invisible"),
        Map.entry("isPassenger", "query.is_riding"),
        Map.entry("isRiding", "query.is_riding"),
        Map.entry("isFallFlying", "query.is_gliding"),
        Map.entry("swimAmount", "(query.is_swimming ? 1 : 0)"),
        Map.entry("isUsingItem", "query.is_using_item"),
        Map.entry("isCrouching", "query.is_sneaking"),
        Map.entry("ageScale", "(query.is_baby ? 0.5 : 1)"),
        Map.entry("displayFireAnimation", "query.is_on_fire")
    );

    private RenderStateQueries() {
    }

    /**
     * Gets the Molang equivalent of a vanilla render state field.
     *
     * @param field the field
     * @return the Molang, or null if the field isn't vanilla or has no equivalent
     */
    @Nullable
    public static String query(@NotNull String field) {
        return QUERIES.get(field);
    }
}
