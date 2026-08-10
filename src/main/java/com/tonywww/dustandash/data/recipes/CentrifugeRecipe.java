package com.tonywww.dustandash.data.recipes;

import com.google.gson.JsonObject;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAARecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class CentrifugeRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final NonNullList<ItemStack> outputItemStacks;
    private final NonNullList<Ingredient> recipeItems;
    private final int tick;

    public static final int MAX_SLOTS = 2;
    public static final int OUTPUT_SLOTS = 8;

    public CentrifugeRecipe(ResourceLocation id, NonNullList<ItemStack> output, NonNullList<Ingredient> recipeItems, int tick) {
        this.id = id;
        this.outputItemStacks = output;
        this.recipeItems = recipeItems;
        this.tick = tick;

    }


    @Override
    public boolean matches(Container inv, Level pLevel) {
        for (int i = 0; i < MAX_SLOTS; i++) {
            ItemStack itemStack = inv.getItem(i);
            if (!recipeItems.get(i).test(itemStack)) {
                return false;

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
        return ItemStack.EMPTY;
    }

    public NonNullList<ItemStack> getResultItemStacks() {
        return outputItemStacks;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DAARecipe.CENTRIFUGE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CentrifugeRecipeType.INSTANCE;
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
        return new ItemStack(DAABlocks.CENTRIFUGE.get());
    }

    public int getTick() {
        return tick;
    }

    public static class CentrifugeRecipeType implements RecipeType<CentrifugeRecipe> {
        public static final CentrifugeRecipeType INSTANCE = new CentrifugeRecipeType();
        public static final String ID = "centrifuge";

    }

    public static class Serializer implements RecipeSerializer<CentrifugeRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(DustAndAsh.MOD_ID, "centrifuge");

        @Override
        public CentrifugeRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
            int tick = GsonHelper.getAsInt(json, "tick");
            NonNullList<Ingredient> inputs = RecipeIo.readIngredients(json, pRecipeId, "ingredients", MAX_SLOTS, true);
            NonNullList<ItemStack> outputs = RecipeIo.readOutputs(json, pRecipeId, "outputs", OUTPUT_SLOTS, true);

            return new CentrifugeRecipe(pRecipeId, outputs, inputs, tick);
        }


        @Nullable
        @Override
        public CentrifugeRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int tick = pBuffer.readInt();
            NonNullList<Ingredient> inputs = RecipeIo.readIngredients(pBuffer, pRecipeId, "ingredients", MAX_SLOTS);
            NonNullList<ItemStack> output = RecipeIo.readOutputs(pBuffer, pRecipeId, "outputs", OUTPUT_SLOTS);

            return new CentrifugeRecipe(pRecipeId, output, inputs, tick);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, CentrifugeRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.getTick());
            RecipeIo.writeIngredients(pBuffer, pRecipe.getIngredients());
            RecipeIo.writeOutputs(pBuffer, pRecipe.getResultItemStacks());
        }
    }
}
