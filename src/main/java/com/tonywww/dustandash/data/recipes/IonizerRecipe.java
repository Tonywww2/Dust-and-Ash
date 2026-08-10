package com.tonywww.dustandash.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAItems;
import com.tonywww.dustandash.registry.DAARecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;

public class IonizerRecipe implements Recipe<Container> {


    private final ResourceLocation id;
    private final NonNullList<Ingredient> recipeItems;
    private final Block inputBlock;
    private final NonNullList<ItemStack> outputItemStacks;
    private final int powerCost;
    private final int tick;
    private final boolean costElectrodes;
    private final Block outputBlock;

    public static final int MAX_SLOTS = 5;
    public static final int OUTPUT_SLOTS = 4;

    public IonizerRecipe(ResourceLocation id, NonNullList<Ingredient> recipeItems, Block inputBlock, NonNullList<ItemStack> output, int powerCost, int tick, boolean costElectrodes, Block outputBlock) {
        this.id = Objects.requireNonNull(id, "id");
        this.recipeItems = Objects.requireNonNull(recipeItems, "recipeItems");
        this.inputBlock = Objects.requireNonNull(inputBlock, "inputBlock");
        this.outputItemStacks = Objects.requireNonNull(output, "output");
        this.powerCost = powerCost;
        this.tick = tick;
        this.costElectrodes = costElectrodes;
        this.outputBlock = Objects.requireNonNull(outputBlock, "outputBlock");

        if (recipeItems.size() != MAX_SLOTS) {
            throw new IllegalArgumentException("Ionizer recipe requires exactly " + MAX_SLOTS + " ingredients");
        }
        if (output.size() != OUTPUT_SLOTS) {
            throw new IllegalArgumentException("Ionizer recipe requires exactly " + OUTPUT_SLOTS + " outputs");
        }
    }


    // 0 power 1-3 items 4-5 electrode
    @Override
    public boolean matches(Container inv, Level pLevel) {
        if (inv.getItem(0).getCount() >= powerCost) {
            for (int i = 1; i <= 5; i++) {
                ItemStack itemStack = inv.getItem(i);
                if (recipeItems.get(i - 1).test(DAAItems.EMPTY.get().getDefaultInstance()) && itemStack.isEmpty()
                        || !recipeItems.get(i - 1).test(itemStack)) {
                    return false;
                }

            }
            return true;
        }

        return false;

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

    public int getPowerCost() {
        return powerCost;
    }

    public NonNullList<ItemStack> getResultItemStacks() {
        return outputItemStacks;
    }

    public Block getResultBlock() {
        return outputBlock;
    }

    public Block getInputBlock() {
        return inputBlock;
    }

    public boolean isCostElectrodes() {
        return costElectrodes;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DAARecipe.IONIZER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return IonizerRecipeType.INSTANCE;
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
        return new ItemStack(DAABlocks.IONIZER.get());
    }

    public int getTick() {
        return tick;
    }

    public static class IonizerRecipeType implements RecipeType<IonizerRecipe> {
        public static final IonizerRecipeType INSTANCE = new IonizerRecipeType();
        public static final String ID = "ionizer";
    }

    public static class Serializer implements RecipeSerializer<IonizerRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(DustAndAsh.MOD_ID, "ionizer");

        @Override
        public IonizerRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
            int cost = GsonHelper.getAsInt(json, "cost");
            int tick = GsonHelper.getAsInt(json, "tick");
            boolean costElectrodes = GsonHelper.getAsBoolean(json, "costElectrodes");
            Block inputBlock = readRequiredBlock(pRecipeId, json, "inputBlock");
            Block outputBlock = readRequiredBlock(pRecipeId, json, "outputBlock");

            NonNullList<Ingredient> inputs = RecipeIo.readIngredients(json, pRecipeId, "ingredients", MAX_SLOTS, true);
            NonNullList<ItemStack> outputs = RecipeIo.readOutputs(json, pRecipeId, "outputs", OUTPUT_SLOTS, true);

            return new IonizerRecipe(pRecipeId, inputs, inputBlock, outputs, cost, tick, costElectrodes, outputBlock);
        }

        private static Block readRequiredBlock(ResourceLocation recipeId, JsonObject json, String field) {
            ResourceLocation blockId = new ResourceLocation(GsonHelper.getAsString(json, field));
            Block block = ForgeRegistries.BLOCKS.getValue(blockId);
            if (block == null) {
                throw new JsonSyntaxException("Ionizer recipe " + recipeId + " references unknown " + field
                        + " " + blockId);
            }
            return block;
        }

        @Nullable
        @Override
        public IonizerRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            NonNullList<Ingredient> inputs = RecipeIo.readIngredients(pBuffer, pRecipeId, "ingredients", MAX_SLOTS);

            Block inputBlock = readRequiredBlock(pRecipeId, "inputBlock", pBuffer.readResourceLocation());

            NonNullList<ItemStack> outputs = RecipeIo.readOutputs(pBuffer, pRecipeId, "outputs", OUTPUT_SLOTS);

            int tick = pBuffer.readVarInt();
            int cost = pBuffer.readVarInt();
            boolean costElectrodes = pBuffer.readBoolean();
            Block outputBlock = readRequiredBlock(pRecipeId, "outputBlock", pBuffer.readResourceLocation());

            return new IonizerRecipe(pRecipeId, inputs, inputBlock, outputs, cost, tick, costElectrodes, outputBlock);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, IonizerRecipe pRecipe) {
            RecipeIo.writeIngredients(pBuffer, pRecipe.getIngredients());

            pBuffer.writeResourceLocation(getBlockId(pRecipe.id, "inputBlock", pRecipe.inputBlock));

            RecipeIo.writeOutputs(pBuffer, pRecipe.getResultItemStacks());

            pBuffer.writeVarInt(pRecipe.tick);
            pBuffer.writeVarInt(pRecipe.powerCost);
            pBuffer.writeBoolean(pRecipe.costElectrodes);
            pBuffer.writeResourceLocation(getBlockId(pRecipe.id, "outputBlock", pRecipe.outputBlock));
        }

        private static Block readRequiredBlock(ResourceLocation recipeId, String field, ResourceLocation blockId) {
            Block block = ForgeRegistries.BLOCKS.getValue(blockId);
            if (block == null) {
                throw new IllegalArgumentException("Ionizer recipe " + recipeId
                        + " network payload references unknown " + field + " " + blockId);
            }
            return block;
        }

        private static ResourceLocation getBlockId(ResourceLocation recipeId, String field, Block block) {
            ResourceLocation blockId = ForgeRegistries.BLOCKS.getKey(block);
            if (blockId == null) {
                throw new IllegalStateException("Ionizer recipe " + recipeId + " has unregistered " + field);
            }
            return blockId;
        }
    }
}
