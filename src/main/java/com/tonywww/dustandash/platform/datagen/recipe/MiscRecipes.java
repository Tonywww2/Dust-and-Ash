//? if forge {
package com.tonywww.dustandash.platform.datagen.recipe;

import com.tonywww.dustandash.platform.PlatformIdentifier;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/** Converts the one remaining crafting recipe using Patchouli's custom book-crafting type. */
public final class MiscRecipes {
    private MiscRecipes() {
    }

    public static void buildRecipes(Consumer<FinishedRecipe> writer) {
        writer.accept(new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject json) {
                JsonArray ingredients = new JsonArray();
                ingredients.add(Ingredient.of(Items.GRAVEL).toJson());
                ingredients.add(Ingredient.of(Items.BOOK).toJson());
                json.add("ingredients", ingredients);
                json.addProperty("book", "dustandash:dustandash_guide_book");
            }

            @Override
            public ResourceLocation getId() {
                return PlatformIdentifier.id(DustAndAsh.MOD_ID, "crafting/guide_book");
            }

            @Override
            public RecipeSerializer<?> getType() {
                return com.tonywww.dustandash.platform.PlatformRegistries.RECIPE_SERIALIZERS.getValue(
                        PlatformIdentifier.id("patchouli", "shapeless_book_recipe"));
            }

            @Nullable
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }
}

//?}
