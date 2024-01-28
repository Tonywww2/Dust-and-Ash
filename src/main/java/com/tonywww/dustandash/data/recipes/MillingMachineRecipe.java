package com.tonywww.dustandash.data.recipes;

import com.google.common.collect.Maps;
import com.google.gson.*;
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
import java.util.Map;

public class MillingMachineRecipe implements IMillingMachineRecipe {


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

    private boolean isWorkPlaceEmpty(IInventory inv) {
        for (int i = 3; i <= 27; i++) {
            if (inv.getItem(i).getCount() > 0) {
                return false;
            }

        }
        return true;
    }

    @Override
    public boolean matches(IInventory inv, World pLevel) {
        // step1 only check slot 0
        if (isStep1 && isWorkPlaceEmpty(inv)) {
            ItemStack itemStack = inv.getItem(0);
            return recipeItems.get(0).test(itemStack);

        } else {
            // otherwise
            // check slot 1
            if (!recipeItems.get(0).test(inv.getItem(1))) {
                return false;
            }

            // check workspace
            for (int i = 1; i < MAX_SLOTS; i++) {
                ItemStack itemStack = inv.getItem(i + 2);
                if (!recipeItems.get(i).test(itemStack)) {
                    return false;
                }

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
        return ModRecipeTypes.MILLING_SERIALIZER.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.MILLING_MACHINE.get());
    }

    public boolean isStep1() {
        return isStep1;
    }

    public static class MillingRecipeType implements IRecipeType<MillingMachineRecipe> {
        @Override
        public String toString() {
            return MillingMachineRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MillingMachineRecipe> {

        @Override
        public MillingMachineRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "output"));

//            JsonArray ingredients = JSONUtils.getAsJsonArray(json, "ingredients");
            boolean step1 = JSONUtils.getAsBoolean(json, "step1");
            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);

            if (step1) {
                JsonArray ingredients = JSONUtils.getAsJsonArray(json, "ingredients");
                Ingredient temp = Ingredient.fromJson(ingredients.get(0));
                inputs.set(0, temp);

            } else {
                Map<String, Ingredient> map = keyFromJson(JSONUtils.getAsJsonObject(json, "key"));
                JsonArray jArray = JSONUtils.getAsJsonArray(json, "pattern");

                String[] astring = new String[jArray.size()];
                for(int i = 0; i < astring.length; ++i) {
                    astring[i] = JSONUtils.convertToString(jArray.get(i), "pattern[" + i + "]");

                }

                inputs.set(0, map.get(CATALYST));

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
        public MillingMachineRecipe fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
            System.out.println(pRecipeId + " start");
            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);
            // 1 readBoolean
            boolean step1 = pBuffer.readBoolean();
            if (step1) {
                // 2 1 fromNetwork
                Ingredient temp = Ingredient.fromNetwork(pBuffer);
                inputs.set(0, temp);

            } else {

                // 01 readVarInt
                int inputSize = pBuffer.readVarInt();
                for (int i = 0; i < inputSize; i++) {
                    // 2 2 fromNetwork
                    Ingredient temp = Ingredient.fromNetwork(pBuffer);
                    if (!temp.getItems()[0].isEmpty() && temp.getItems()[0].getItem() != Items.AIR) {
                        inputs.set(i, temp);

                    }

                }

            }

            // 3 readItem
            ItemStack output = pBuffer.readItem();

            return new MillingMachineRecipe(pRecipeId, output, inputs, step1);
        }

        @Override
        public void toNetwork(PacketBuffer pBuffer, MillingMachineRecipe pRecipe) {
            // 1 writeBoolean
            pBuffer.writeBoolean(pRecipe.isStep1());

            if (pRecipe.isStep1()) {
                // 2 1 toNetwork
                pRecipe.getIngredients().get(0).toNetwork(pBuffer);

            } else {
                // 01 writeVarInt
                pBuffer.writeVarInt(pRecipe.getIngredients().size());
                for (Ingredient i : pRecipe.getIngredients()) {
                    // 2 2 toNetwork
                    i.toNetwork(pBuffer);

                }

            }

            // 3 writeItem
            pBuffer.writeItem(pRecipe.getResultItem());

        }
    }
}
