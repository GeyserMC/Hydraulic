package org.geysermc.hydraulic.neoforge;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.recipe.CraftingRecipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Registers the server's full recipe set with Geyser through the API.
 * <p>
 * Vanilla servers only send recipe *ids* in {@code declare_recipes} and recipe contents only for
 * unlocked recipes ({@code recipe_book_add}). On a fresh player that is a single recipe, so Bedrock
 * players would see an almost empty recipe book. Since Hydraulic runs inside the server, it can read
 * the {@link RecipeManager} directly and register every recipe so Bedrock players get a populated
 * recipe book.
 */
public final class RecipeRegistration {
    private static final Logger LOGGER = LoggerFactory.getLogger("hydraulic/neoforge");

    private RecipeRegistration() {
    }

    public static void register(MinecraftServer server) {
        RecipeManager manager = server.getRecipeManager();
        GeyserApi api = GeyserApi.api();
        int registered = 0;
        int index = 0;
        for (RecipeHolder<?> holder : manager.getRecipes()) {
            Recipe<?> recipe = holder.value();
            for (RecipeDisplay display : recipe.display()) {
                if (display instanceof ShapedCraftingRecipeDisplay shaped) {
                    CraftingRecipe converted = convert(index, shaped);
                    if (converted != null) {
                        api.registerCraftingRecipe(converted);
                        registered++;
                    }
                }
            }
            index++;
        }
        LOGGER.info("Registered {} crafting recipes from server RecipeManager", registered);
    }

    private static CraftingRecipe convert(int id, ShapedCraftingRecipeDisplay display) {
        List<Integer> ingredients = new ArrayList<>(display.ingredients().size());
        for (SlotDisplay slot : display.ingredients()) {
            ingredients.add(toItemId(slot));
        }
        Integer resultId = toItemId(display.result());
        if (resultId == null) {
            return null;
        }
        return new CraftingRecipe(id, display.width(), display.height(), ingredients, resultId, toItemCount(display.result()));
    }

    private static Integer toItemId(SlotDisplay slot) {
        if (slot instanceof SlotDisplay.ItemStackSlotDisplay itemStack) {
            return Item.getId(itemStack.stack().item().value());
        }
        if (slot instanceof SlotDisplay.TagSlotDisplay tag) {
            java.util.Iterator<Holder<Item>> iterator = BuiltInRegistries.ITEM.getTagOrEmpty(tag.tag()).iterator();
            return iterator.hasNext() ? Item.getId(iterator.next().value()) : null;
        }
        return null;
    }

    private static int toItemCount(SlotDisplay slot) {
        if (slot instanceof SlotDisplay.ItemStackSlotDisplay itemStack) {
            return Math.max(1, itemStack.stack().count());
        }
        return 1;
    }
}
