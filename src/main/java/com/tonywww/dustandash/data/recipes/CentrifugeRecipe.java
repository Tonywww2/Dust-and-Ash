package com.tonywww.dustandash.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tonywww.dustandash.block.ModBlocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class CentrifugeRecipe implements ICentrifugeRecipe {


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
    public boolean matches(IInventory inv, World pLevel) {
        for (int i = 0; i < MAX_SLOTS; i++) {
            ItemStack itemStack = inv.getItem(i);
            if (!recipeItems.get(i).test(itemStack)) {
                return false;
            }

        }

        return true;

    }

    @Override
    public ItemStack assemble(IInventory pInv) {
        return null;
    }

    @Override
    public ItemStack getResultItem() {
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
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.CENTRIFUGE_SERIALIZER.get();
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

    public static class CentrifugeRecipeType implements IRecipeType<CentrifugeRecipe> {
        @Override
        public String toString() {
            return CentrifugeRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CentrifugeRecipe> {

        @Override
        public CentrifugeRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
            JsonArray ingredients = JSONUtils.getAsJsonArray(json, "ingredients");
            int tick = JSONUtils.getAsInt(json, "tick");
            JsonArray outputArr = JSONUtils.getAsJsonArray(json, "output");

            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);
            NonNullList<ItemStack> outputs = NonNullList.withSize(OUTPUT_SLOTS, ItemStack.EMPTY.EMPTY);


            for (int i = 0; i < ingredients.size(); i++) {
                Ingredient temp = Ingredient.fromJson(ingredients.get(i));
                if (temp.getItems()[0].getItem() != Items.AIR) {
                    inputs.set(i, temp);

                }

            }

            for (int i = 0; i < outputs.size(); i++) {
                ItemStack temp = Ingredient.fromJson(outputArr.get(i)).getItems()[0];

                if (temp.getItem() != Items.AIR) {
                    outputs.set(i, temp);

                }

            }

            return new CentrifugeRecipe(pRecipeId, outputs, inputs, tick);
        }

        @Nullable
        @Override
        public CentrifugeRecipe fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);

            NonNullList<ItemStack> output = NonNullList.withSize(OUTPUT_SLOTS, ItemStack.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                ItemStack temp = pBuffer.readItem();
                if (temp.getItem() != Items.AIR) {
                    inputs.set(i, Ingredient.of(temp));

                }

            }

            for (int i = 0; i < output.size(); i++) {
                ItemStack temp = pBuffer.readItem();
                if (temp.getItem() != Items.AIR) {
                    output.set(i, temp);

                }

            }

            int tick = pBuffer.readInt();

            return new CentrifugeRecipe(pRecipeId, output, inputs, tick);
        }

        @Override
        public void toNetwork(PacketBuffer pBuffer, CentrifugeRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.getIngredients().size());
            for (Ingredient i : pRecipe.getIngredients()) {
                i.toNetwork(pBuffer);
            }
            pBuffer.writeItemStack(pRecipe.getResultItem(), false);

        }
    }
}
