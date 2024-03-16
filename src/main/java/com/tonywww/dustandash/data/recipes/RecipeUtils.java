package com.tonywww.dustandash.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tonywww.dustandash.item.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.ForgeConfig;

import static net.minecraft.util.GsonHelper.convertToJsonObject;

public class RecipeUtils {

    static final ItemStack EMPTY = ModItems.EMPTY.get().getDefaultInstance();

    public static NonNullList<Ingredient> itemsFromJson(JsonArray array, int size) {
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(size, Ingredient.EMPTY);

        for (int i = 0; i < array.size(); ++i) {
            Ingredient ingredient = Ingredient.fromJson(array.get(i));
            JsonObject obj = convertToJsonObject(array.get(i), "item");
            if (obj.has("item")) {
                Item item = ShapedRecipe.itemFromJson(obj);
                if (item == ModItems.EMPTY.get()) {
                    continue;
                }
            }
            if (!ingredient.isEmpty()) {
                nonnulllist.set(i, ingredient);

            }

        }

        return nonnulllist;
    }
}
