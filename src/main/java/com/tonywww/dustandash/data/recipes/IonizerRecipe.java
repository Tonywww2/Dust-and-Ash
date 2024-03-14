package com.tonywww.dustandash.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class IonizerRecipe implements IIonizerRecipe {


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
        this.id = id;
        this.recipeItems = recipeItems;
        this.inputBlock = inputBlock;
        this.outputItemStacks = output;
        this.powerCost = powerCost;
        this.tick = tick;
        this.costElectrodes = costElectrodes;
        this.outputBlock = outputBlock;

    }


    // 0 power 1-3 items 4-5 electrode
    @Override
    public boolean matches(IInventory inv, World pLevel) {
        if (inv.getItem(0).getCount() >= powerCost) {
            for (int i = 1; i <= 5; i++) {
                ItemStack itemStack = inv.getItem(i);
                if (recipeItems.get(i - 1).test(ModItems.EMPTY.get().getDefaultInstance()) && itemStack.isEmpty()
                        || !recipeItems.get(i - 1).test(itemStack)) {
                    return false;
                }

            }
            return true;
        }

        return false;

    }

    @Override
    public ItemStack assemble(IInventory pInv) {
        return null;
    }

    @Override
    public ItemStack getResultItem() {
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
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.IONIZER_SERIALIZER.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.CENTRIFUGE.get());
    }

    public int getTick() {
        return tick;
    }

    public static class IonizerRecipeType implements IRecipeType<IonizerRecipe> {
        @Override
        public String toString() {
            return IonizerRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<IonizerRecipe> {

        @Override
        public IonizerRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
//            System.out.println(pRecipeId + " start from json");
            JsonArray ingredients = JSONUtils.getAsJsonArray(json, "ingredients");
            ResourceLocation inputBlockStr = new ResourceLocation(JSONUtils.getAsString(json, "inputBlock"));

            JsonArray outputArr = JSONUtils.getAsJsonArray(json, "outputs");
            int cost = JSONUtils.getAsInt(json, "cost");
            int tick = JSONUtils.getAsInt(json, "tick");
            boolean costElectrodes = JSONUtils.getAsBoolean(json, "costElectrodes");
            ResourceLocation outputBlockStr = new ResourceLocation(JSONUtils.getAsString(json, "outputBlock"));

            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);
            Block inputBlock = null;

            NonNullList<ItemStack> outputs = NonNullList.withSize(OUTPUT_SLOTS, ItemStack.EMPTY);
            Block outputBlock = null;


            for (int i = 0; i < ingredients.size(); i++) {
                Ingredient temp = Ingredient.fromJson(ingredients.get(i));
                if (temp.getItems()[0].getItem() != ModItems.EMPTY.get()) {
                    inputs.set(i, temp);

                }

            }

            if (inputBlockStr != ModItems.EMPTY.getId()) {
                inputBlock = Registry.BLOCK.get(inputBlockStr);

            }

            for (int i = 0; i < outputs.size(); i++) {
                ItemStack temp = Ingredient.fromJson(outputArr.get(i)).getItems()[0];

                if (temp.getItem() != ModItems.EMPTY.get()) {
                    outputs.set(i, temp);

                }

            }

            if (outputBlockStr != ModItems.EMPTY.getId()) {
                outputBlock = Registry.BLOCK.get(outputBlockStr);

            }
//            System.out.println("inputBlock:" + inputBlock.getRegistryName());
//            System.out.println("outputBlock:" + outputBlock.getRegistryName());

            return new IonizerRecipe(pRecipeId, inputs, inputBlock, outputs, cost, tick, costElectrodes, outputBlock);
        }

        @Nullable
        @Override
        public IonizerRecipe fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
            System.out.println(pRecipeId + " start from network");
            // 1 ingredients
            int inputSize = pBuffer.readVarInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);
            for (int i = 0; i < inputSize; i++) {
                // 1 1 fromNetwork
                Ingredient temp = Ingredient.fromNetwork(pBuffer);
                inputs.set(i, temp);

            }
            // 2 inputBlock
            Block inputBlock = Registry.BLOCK.get(pBuffer.readResourceLocation());
            System.out.println("inputBlock: " + inputBlock);

            // 3 outputs
            int outputSize = pBuffer.readVarInt();
            NonNullList<ItemStack> outputs = NonNullList.withSize(OUTPUT_SLOTS, ItemStack.EMPTY);
            for (int i = 0; i < outputSize; i++) {
                // 3 1 readItem
                ItemStack temp = pBuffer.readItem();
                outputs.set(i, temp);

            }

            // 4 tick
            int tick = pBuffer.readVarInt();
            // 5 cost
            int cost = pBuffer.readVarInt();
            // 5.5
            boolean costElectrodes = pBuffer.readBoolean();

            // 6 outputBlock
            Block outputBlock = Registry.BLOCK.get(pBuffer.readResourceLocation());
            System.out.println("outputBlock: " + outputBlock);

            return new IonizerRecipe(pRecipeId, inputs, inputBlock, outputs, cost, tick, costElectrodes, outputBlock);
        }

        @Override
        public void toNetwork(PacketBuffer pBuffer, IonizerRecipe pRecipe) {
//            System.out.println(pRecipe.id + " start to network");
            // 1 ingredients
            pBuffer.writeVarInt(pRecipe.getIngredients().size());
            for (Ingredient i : pRecipe.getIngredients()) {
                // 1 1 toNetwork
                i.toNetwork(pBuffer);
            }
            // 2 inputBlock
            pBuffer.writeResourceLocation(pRecipe.inputBlock.getRegistryName());

            // 3 outputs
            pBuffer.writeVarInt(pRecipe.getResultItemStacks().size());
            for (ItemStack i : pRecipe.getResultItemStacks()) {
                // 3 1 writeItem
                pBuffer.writeItem(i);
            }

            // 4 tick
            pBuffer.writeVarInt(pRecipe.tick);
            // 5 cost
            pBuffer.writeVarInt(pRecipe.powerCost);
            // 5.5 costElectrodes
            pBuffer.writeBoolean(pRecipe.costElectrodes);

            // 6 outputBlock
            pBuffer.writeResourceLocation(pRecipe.outputBlock.getRegistryName());

        }
    }
}
