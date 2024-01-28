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
public class IntegratedBlockRecipe implements IIntegratedBlockRecipe {


    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final int level;

    public static final int MAX_SLOTS = 8;

    public IntegratedBlockRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, int level) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.level = level;
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
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.INTEGRATE_SERIALIZER.get();
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

    public static class IntegrateRecipeType implements IRecipeType<IntegratedBlockRecipe> {
        @Override
        public String toString() {
            return IntegratedBlockRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<IntegratedBlockRecipe> {

        @Override
        public IntegratedBlockRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "output"));

            JsonArray ingredients = JSONUtils.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);
            int lv = JSONUtils.getAsInt(json, "level");

            for (int i = 0; i < ingredients.size(); i++) {
                Ingredient temp = Ingredient.fromJson(ingredients.get(i));
                if (temp.getItems()[0].getItem() != Items.AIR) {
                    inputs.set(i, temp);

                }

            }

            return new IntegratedBlockRecipe(pRecipeId, output, inputs, lv);
        }

        @Nullable
        @Override
        public IntegratedBlockRecipe fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
            System.out.println(pRecipeId + " start");
            // 01 readVarInt
            int inputSize = pBuffer.readVarInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);
            for (int i = 0; i < inputSize; i++) {
                // 1 fromNetwork
                Ingredient temp = Ingredient.fromNetwork(pBuffer);
                if (!temp.getItems()[0].isEmpty() && temp.getItems()[0].getItem() != Items.AIR) {
                    inputs.set(i, temp);

                }

            }

            // 2 readInt
            int lv = pBuffer.readInt();

            // 3 readItem
            ItemStack output = pBuffer.readItem();

            return new IntegratedBlockRecipe(pRecipeId, output, inputs, lv);
        }

        @Override
        public void toNetwork(PacketBuffer pBuffer, IntegratedBlockRecipe pRecipe) {
            // 01 writeVarInt
            pBuffer.writeVarInt(pRecipe.getIngredients().size());
            for (Ingredient i : pRecipe.getIngredients()) {
                // 1 toNetwork
                i.toNetwork(pBuffer);
            }
            // 2 writeInt
            pBuffer.writeInt(pRecipe.getLevel());
            // 3 writeItem
            pBuffer.writeItem(pRecipe.getResultItem());

        }
    }
}