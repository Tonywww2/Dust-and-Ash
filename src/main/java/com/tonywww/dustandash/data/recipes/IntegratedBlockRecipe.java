package com.tonywww.dustandash.data.recipes;

import com.google.gson.JsonObject;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAItems;
import com.tonywww.dustandash.registry.DAARecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

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
            if ((recipeItems.get(i).test(DAAItems.EMPTY.get().getDefaultInstance()) && itemStack.isEmpty()) ||
                    !recipeItems.get(i).test(itemStack)) {
                return false;
            }

        }

        return true;

    }

    @Override
    public ItemStack assemble(Container pInv, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DAARecipe.INTEGRATE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return IntegrateRecipeType.INSTANCE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    public ItemStack getIcon() {
        return new ItemStack(DAABlocks.INTEGRATED_BLOCK.get());
    }

    public int getLevel() {
        return level;
    }

    public static class IntegrateRecipeType implements RecipeType<IntegratedBlockRecipe> {
        public static final IntegrateRecipeType INSTANCE = new IntegrateRecipeType();
        public static final String ID = "integrate";

    }

    public static class Serializer implements RecipeSerializer<IntegratedBlockRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(DustAndAsh.MOD_ID, "integrate");

        @Override
        public IntegratedBlockRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
            NonNullList<Ingredient> inputs = RecipeIo.readIngredients(json, pRecipeId, "ingredients", MAX_SLOTS, false);
            int lv = GsonHelper.getAsInt(json, "level");

            return new IntegratedBlockRecipe(pRecipeId, inputs, output, lv);
        }

        @Nullable
        @Override
        public IntegratedBlockRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int lv = pBuffer.readInt();
            NonNullList<Ingredient> inputs = RecipeIo.readIngredients(pBuffer, pRecipeId, "ingredients", MAX_SLOTS);
            ItemStack output = pBuffer.readItem();

            return new IntegratedBlockRecipe(pRecipeId, inputs, output, lv);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, IntegratedBlockRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.getLevel());
            RecipeIo.writeIngredients(pBuffer, pRecipe.getIngredients());
            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }
    }
}