package com.tonywww.dustandash.data.recipes;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAItems;
import com.tonywww.dustandash.registry.DAARecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Map;

public class MillingMachineRecipe implements Recipe<Container> {


    private final ResourceLocation id;
    private final ItemStack output;

    private final NonNullList<Ingredient> recipeItems;
    private final boolean isStep1;

    public static final int MAX_SLOTS = 26;
    public static final int MAX_HEIGHT = 5;
    public static final int MAX_WIDTH = 5;

    public static String CATALYST = "catalyst";

    public MillingMachineRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, boolean isStep1) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.isStep1 = isStep1;
    }

    private boolean isWorkPlaceEmpty(Container inv) {
        for (int i = 3; i <= 27; i++) {
            if (inv.getItem(i).getCount() > 0) {
                return false;
            }

        }
        return true;
    }

    @Override
    public boolean matches(Container inv, Level pLevel) {
        // step1 only check slot 0
        if (isStep1 && isWorkPlaceEmpty(inv)) {
            ItemStack itemStack = inv.getItem(0);
            return recipeItems.get(0).test(itemStack);

        } else {
            // otherwise
            // check slot 1
            if ((recipeItems.get(0).test(DAAItems.EMPTY.get().getDefaultInstance()) && inv.getItem(1).isEmpty()) ||
                    !recipeItems.get(0).test(inv.getItem(1))) {
                return false;
            }

            // check workspace
            for (int i = 1; i < MAX_SLOTS; i++) {
                ItemStack itemStack = inv.getItem(i + 2);
                if ((recipeItems.get(i).test(DAAItems.EMPTY.get().getDefaultInstance()) && itemStack.isEmpty()) ||
                        !recipeItems.get(i).test(itemStack)) {
                    return false;
                }

            }

        }

        return true;

    }

    @Override
    public ItemStack assemble(Container pInv, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DAARecipe.MILLING_SERIALIZER.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    public ItemStack getIcon() {
        return new ItemStack(DAABlocks.MILLING_MACHINE.get());
    }

    public boolean isStep1() {
        return isStep1;
    }

    @Override
    public RecipeType<?> getType() {
        return MillingRecipeType.INSTANCE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class MillingRecipeType implements RecipeType<MillingMachineRecipe> {
        public static final MillingRecipeType INSTANCE = new MillingRecipeType();
        public static final String ID = "milling";

    }

    //    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<MillingMachineRecipe> {
    public static class Serializer implements RecipeSerializer<MillingMachineRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(DustAndAsh.MOD_ID, "milling");

        @Override
        public MillingMachineRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

            boolean step1 = GsonHelper.getAsBoolean(json, "step1");
            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);

            if (step1) {
                JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
                if (ingredients.size() != 1) {
                    throw new JsonSyntaxException("Milling recipe " + pRecipeId
                            + " step1 requires exactly one ingredient");
                }
                Ingredient temp = Ingredient.fromJson(ingredients.get(0));
                inputs.set(0, temp);

            } else {
                Map<String, Ingredient> map = keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
                JsonArray jArray = GsonHelper.getAsJsonArray(json, "pattern");
                if (jArray.size() != MAX_HEIGHT) {
                    throw new JsonSyntaxException("Milling recipe " + pRecipeId + " requires exactly "
                            + MAX_HEIGHT + " pattern rows");
                }

                String[] astring = new String[jArray.size()];
                for (int i = 0; i < astring.length; ++i) {
                    astring[i] = GsonHelper.convertToString(jArray.get(i), "pattern[" + i + "]");
                    if (astring[i].length() != MAX_WIDTH) {
                        throw new JsonSyntaxException("Milling recipe " + pRecipeId + " pattern row " + i
                                + " must contain exactly " + MAX_WIDTH + " symbols");
                    }

                }

                Ingredient catalyst = map.get(CATALYST);
                if (catalyst == null) {
                    throw new JsonSyntaxException("Milling recipe " + pRecipeId + " is missing catalyst key");
                }
                inputs.set(0, catalyst);

                for (int i = 0; i < 5; i++) {
                    String temp = astring[i];
                    for (int j = 0; j < 5; j++) {
                        // 1 - 25
                        Ingredient ig = map.get(temp.substring(j, j + 1));

                        if (ig == null) {
                            throw new JsonSyntaxException("Pattern references symbol '" + temp + "' but it's not defined in the key");
                        }

                        inputs.set((i * 5) + j + 1, ig);

                    }

                }

            }
            return new MillingMachineRecipe(pRecipeId, output, inputs, step1);
        }

        /**
         * Returns a key json object as a Java HashMap.
         */
        private static Map<String, Ingredient> keyFromJson(JsonObject json) {
            Map<String, Ingredient> map = Maps.newHashMap();

            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));

            }

            map.put(" ", Ingredient.EMPTY);
            return map;
        }


        @Nullable
        @Override
        public MillingMachineRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.of(DAAItems.EMPTY.get()));
            boolean step1 = pBuffer.readBoolean();
            if (step1) {
                inputs.set(0, Ingredient.fromNetwork(pBuffer));
            } else {
                inputs = RecipeIo.readIngredients(pBuffer, pRecipeId, "ingredients", MAX_SLOTS);
            }

            ItemStack output = pBuffer.readItem();

            return new MillingMachineRecipe(pRecipeId, output, inputs, step1);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, MillingMachineRecipe pRecipe) {
            pBuffer.writeBoolean(pRecipe.isStep1());

            if (pRecipe.isStep1()) {
                pRecipe.getIngredients().get(0).toNetwork(pBuffer);
            } else {
                RecipeIo.writeIngredients(pBuffer, pRecipe.getIngredients());
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }
    }


}
