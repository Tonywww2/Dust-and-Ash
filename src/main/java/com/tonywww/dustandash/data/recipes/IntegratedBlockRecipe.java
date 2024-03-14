package com.tonywww.dustandash.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.item.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

import static com.tonywww.dustandash.data.recipes.RecipeUtils.itemsFromJson;

public class IntegratedBlockRecipe implements Recipe<Container> {


    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final int level;

    public static final int MAX_SLOTS = 8;

    public IntegratedBlockRecipe(ResourceLocation id, NonNullList<Ingredient> inputs, ItemStack output, int level) {
        this.id = id;
        this.output = output;
        this.recipeItems = inputs;
        this.level = level;
    }


    @Override
    public boolean matches(Container inv, Level pLevel) {
        for (int i = 0; i < MAX_SLOTS; i++) {
            ItemStack itemStack = inv.getItem(i);
            if ((recipeItems.get(i).test(ModItems.EMPTY.get().getDefaultInstance()) && itemStack.isEmpty()) ||
                    !recipeItems.get(i).test(itemStack)) {
                return false;
            }

        }

        return true;

    }

    @Override
    public ItemStack assemble(Container pInv) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipe.INTEGRATE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return IntegrateRecipeType.INSTANCE;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.INTEGRATED_BLOCK.get());
    }

    public int getLevel() {
        return level;
    }

    public static class IntegrateRecipeType implements RecipeType<IntegratedBlockRecipe> {
        public static final IntegrateRecipeType INSTANCE = new IntegrateRecipeType();
        public static final String ID = "integrate";

    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<IntegratedBlockRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(DustAndAsh.MOD_ID, "integrate");

        @Override
        public IntegratedBlockRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
//            System.out.println(pRecipeId + " start from json");
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));

//            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
//            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);
            NonNullList<Ingredient> inputs = itemsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"), MAX_SLOTS);

            int lv = GsonHelper.getAsInt(json, "level");

//            for (int i = 0; i < ingredients.size(); i++) {
//                Ingredient temp = Ingredient.fromJson(ingredients.get(i));
//
//                if (!ingredients.isEmpty() && temp.getItems()[0].getItem() != ModItems.EMPTY.get()) {
//                    inputs.set(i, temp);
//
//                }
//
//            }

            return new IntegratedBlockRecipe(pRecipeId, inputs, output, lv);
        }

        @Nullable
        @Override
        public IntegratedBlockRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
//            System.out.println(pRecipeId + " start from network");
            // 2 readInt
            int lv = pBuffer.readInt();

            // 01 readVarInt
            int inputSize = pBuffer.readVarInt();

            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);
            for (int i = 0; i < inputSize; i++) {
                // 1 fromNetwork
                Ingredient temp = Ingredient.fromNetwork(pBuffer);
                inputs.set(i, temp);

            }
            // 3 readItem
            ItemStack output = pBuffer.readItem();

            return new IntegratedBlockRecipe(pRecipeId, inputs, output, lv);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, IntegratedBlockRecipe pRecipe) {
//            System.out.println(pRecipe.id + " start to network");
            // 2 writeInt
            pBuffer.writeInt(pRecipe.getLevel());

            // 01 writeVarInt
            pBuffer.writeVarInt(pRecipe.getIngredients().size());

            for (Ingredient i : pRecipe.getIngredients()) {
                // 1 toNetwork
                i.toNetwork(pBuffer);

            }
            // 3 writeItem
            pBuffer.writeItemStack(pRecipe.getResultItem(), false);

        }

//        @Override
//        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
//            return INSTANCE;
//        }
//
//        @org.jetbrains.annotations.Nullable
//        @Override
//        public ResourceLocation getRegistryName() {
//            return ID;
//        }
//
//        @Override
//        public Class<RecipeSerializer<?>> getRegistryType() {
//            return Serializer.castClass(RecipeSerializer.class);
//        }
//
//        private static <G> Class<G> castClass(Class<?> cls) {
//            return (Class<G>) cls;
//        }
    }
}