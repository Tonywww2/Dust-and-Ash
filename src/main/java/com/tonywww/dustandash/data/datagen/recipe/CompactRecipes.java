package com.tonywww.dustandash.data.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.registry.DAAItems;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * Converts data/dustandash/recipes/compact/**.json: each is a {@code forge:conditional} recipe
 * gated on the target addon mod being loaded. The recipe results reference items from mods that
 * are not present at datagen time, so their "result" is written as a raw id (no Item lookup).
 */
public final class CompactRecipes {
    private CompactRecipes() {
    }

    private static TagKey<Item> forgeTag(String path) {
        return ItemTags.create(new ResourceLocation("forge", path));
    }

    public static void buildRecipes(Consumer<FinishedRecipe> writer) {
        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition("ae2"))
                .addRecipe(consumer -> consumer.accept(rawSmithingTransform("compact/ae2/certus_quartz_crystal",
                        Ingredient.of(DAAItems.ORDER_DUST.get()), Ingredient.of(forgeTag("gems/quartz")),
                        Ingredient.of(DAAItems.ELECTRON.get()), "ae2:certus_quartz_crystal")))
                .build(writer, new ResourceLocation(DustAndAsh.MOD_ID, "compact/ae2/certus_quartz_crystal"));

        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition("ars_nouveau"))
                .addRecipe(consumer -> consumer.accept(rawSmithingTransform("compact/ars/blue_archwood_sapling",
                        Ingredient.of(DAAItems.ORDER_DUST.get()), Ingredient.of(forgeTag("sapling")),
                        Ingredient.of(DAAItems.SEEP_DUST.get()), "ars_nouveau:blue_archwood_sapling")))
                .build(writer, new ResourceLocation(DustAndAsh.MOD_ID, "compact/ars/blue_archwood_sapling"));

        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition("ars_nouveau"))
                .addRecipe(consumer -> consumer.accept(rawSmithingTransform("compact/ars/green_archwood_sapling",
                        Ingredient.of(DAAItems.ORDER_DUST.get()), Ingredient.of(forgeTag("sapling")),
                        Ingredient.of(DAAItems.REPRODUCE_DUST.get()), "ars_nouveau:green_archwood_sapling")))
                .build(writer, new ResourceLocation(DustAndAsh.MOD_ID, "compact/ars/green_archwood_sapling"));

        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition("ars_nouveau"))
                .addRecipe(consumer -> consumer.accept(rawSmithingTransform("compact/ars/mana_gem",
                        Ingredient.of(DAAItems.ORDER_DUST.get()), Ingredient.of(forgeTag("gems/lapis")),
                        Ingredient.of(DAAItems.PURE_ENERGY.get()), "ars_nouveau:mana_gem")))
                .build(writer, new ResourceLocation(DustAndAsh.MOD_ID, "compact/ars/mana_gem"));

        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition("ars_nouveau"))
                .addRecipe(consumer -> consumer.accept(rawSmithingTransform("compact/ars/purple_archwood_sapling",
                        Ingredient.of(DAAItems.ORDER_DUST.get()), Ingredient.of(forgeTag("sapling")),
                        Ingredient.of(DAAItems.EXTINGUISH_DUST.get()), "ars_nouveau:purple_archwood_sapling")))
                .build(writer, new ResourceLocation(DustAndAsh.MOD_ID, "compact/ars/purple_archwood_sapling"));

        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition("ars_nouveau"))
                .addRecipe(consumer -> consumer.accept(rawSmithingTransform("compact/ars/red_archwood_sapling",
                        Ingredient.of(DAAItems.ORDER_DUST.get()), Ingredient.of(forgeTag("sapling")),
                        Ingredient.of(DAAItems.SMELT_DUST.get()), "ars_nouveau:red_archwood_sapling")))
                .build(writer, new ResourceLocation(DustAndAsh.MOD_ID, "compact/ars/red_archwood_sapling"));

        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition("integrateddynamics"))
                .addRecipe(consumer -> consumer.accept(rawIntegrate("compact/integrateddynamics/menril_sapling", 1,
                        List.of(Ingredient.of(forgeTag("sapling")), Ingredient.of(net.minecraft.world.item.Items.SPIDER_EYE),
                                Ingredient.of(DAAItems.ORDER_DUST.get()), Ingredient.of(DAAItems.CRYSTALLIZE_DUST.get()),
                                Ingredient.of(DAAItems.ORDER_DUST.get())),
                        "integrateddynamics:menril_sapling", 1)))
                .build(writer, new ResourceLocation(DustAndAsh.MOD_ID, "compact/integrateddynamics/menril_sapling"));

        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition("umapyoi"))
                .addRecipe(consumer -> consumer.accept(rawShapeless("compact/umapyoi/jewel",
                        List.of(Ingredient.of(forgeTag("crops/carrot")), Ingredient.of(DAAItems.DUST_WITH_ENERGY.get()),
                                Ingredient.of(DAAItems.CRYSTALLIZE_DUST.get())),
                        "umapyoi:jewel", 1)))
                .build(writer, new ResourceLocation(DustAndAsh.MOD_ID, "compact/umapyoi/jewel"));
    }

    private static FinishedRecipe rawSmithingTransform(String path, Ingredient template, Ingredient base,
                                                        Ingredient addition, String resultId) {
        return new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject json) {
                json.add("template", template.toJson());
                json.add("base", base.toJson());
                json.add("addition", addition.toJson());
                JsonObject result = new JsonObject();
                result.addProperty("item", resultId);
                json.add("result", result);
            }

            @Override
            public ResourceLocation getId() {
                return new ResourceLocation(DustAndAsh.MOD_ID, path);
            }

            @Override
            public RecipeSerializer<?> getType() {
                return RecipeSerializer.SMITHING_TRANSFORM;
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
        };
    }

    private static FinishedRecipe rawShapeless(String path, List<Ingredient> ingredients, String resultId, int count) {
        return new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject json) {
                JsonArray ingredientsArray = new JsonArray();
                ingredients.forEach(ingredient -> ingredientsArray.add(ingredient.toJson()));
                json.add("ingredients", ingredientsArray);

                JsonObject result = new JsonObject();
                result.addProperty("item", resultId);
                result.addProperty("count", count);
                json.add("result", result);
            }

            @Override
            public ResourceLocation getId() {
                return new ResourceLocation(DustAndAsh.MOD_ID, path);
            }

            @Override
            public RecipeSerializer<?> getType() {
                return RecipeSerializer.SHAPELESS_RECIPE;
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
        };
    }

    private static FinishedRecipe rawIntegrate(String path, int level, List<Ingredient> ingredients, String resultId,
                                                int count) {
        return new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject json) {
                json.addProperty("level", level);
                JsonArray ingredientsArray = new JsonArray();
                ingredients.forEach(ingredient -> ingredientsArray.add(ingredient.toJson()));
                json.add("ingredients", ingredientsArray);

                JsonObject output = new JsonObject();
                output.addProperty("item", resultId);
                output.addProperty("count", count);
                json.add("output", output);
            }

            @Override
            public ResourceLocation getId() {
                return new ResourceLocation(DustAndAsh.MOD_ID, path);
            }

            @Override
            public RecipeSerializer<?> getType() {
                return com.tonywww.dustandash.registry.DAARecipe.INTEGRATE_SERIALIZER.get();
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
        };
    }
}
