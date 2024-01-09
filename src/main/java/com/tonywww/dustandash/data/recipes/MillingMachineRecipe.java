package com.tonywww.dustandash.data.recipes;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import com.tonywww.dustandash.block.ModBlocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
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
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public class MillingMachineRecipe implements IMillingMachineRecipe {


    private final ResourceLocation id;
    private final ItemStack output;

    private final NonNullList<Ingredient> recipeItems;
    private final boolean isStep1;

    public static final int MAX_SLOTS = 26;
    public static final int MAX_HEIGHT = 5;
    public static final int MAX_WIDTH = 5;

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
        // step1 only check index 0
        if (isStep1 && isWorkPlaceEmpty(inv)) {
            ItemStack itemStack = inv.getItem(0);
            return recipeItems.get(0).test(itemStack);

        } else {
            // otherwise
            if (!recipeItems.get(0).test(inv.getItem(1))) {
                return false;
            }

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

            JsonArray ingredients = JSONUtils.getAsJsonArray(json, "ingredients");
            boolean step1 = JSONUtils.getAsBoolean(json, "step1");
            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);;

            if (step1) {
                Ingredient temp = Ingredient.fromJson(ingredients.get(0));
                inputs.set(0, temp);

            } else {
//                Map<String, Ingredient> map = keyFromJson(JSONUtils.getAsJsonObject(json, "key"));
//                String[] astring = shrink(patternFromJson(JSONUtils.getAsJsonArray(json, "pattern")));
//
//                int i = astring[0].length();
//                int j = astring.length;

//                inputs = dissolvePattern(astring, map, i, j);

                for (int i = 0; i < ingredients.size(); i++) {
                    Ingredient temp = Ingredient.fromJson(ingredients.get(i));
                    if (temp.getItems()[0].getItem() != Items.AIR) {
                        inputs.set(i, temp);

                    }

                }

            }


            return new MillingMachineRecipe(pRecipeId, output, inputs, step1);
        }

        private static String[] patternFromJson(JsonArray pPatternArray) {
            String[] astring = new String[pPatternArray.size()];
            if (astring.length > MAX_HEIGHT) {
                throw new JsonSyntaxException("Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum");
            } else if (astring.length == 0) {
                throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
            } else {
                for (int i = 0; i < astring.length; ++i) {
                    String s = JSONUtils.convertToString(pPatternArray.get(i), "pattern[" + i + "]");
                    if (s.length() > MAX_WIDTH) {
                        throw new JsonSyntaxException("Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum");
                    }

                    if (i > 0 && astring[0].length() != s.length()) {
                        throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                    }

                    astring[i] = s;
                }

                return astring;
            }
        }

        /**
         * Returns a key json object as a Java HashMap.
         */
        private static Map<String, Ingredient> keyFromJson(JsonObject pKeyEntry) {
            Map<String, Ingredient> map = Maps.newHashMap();

            for (Map.Entry<String, JsonElement> entry : pKeyEntry.entrySet()) {
                if (entry.getKey().length() != 1) {
                    throw new JsonSyntaxException("Invalid key entry: '" + (String) entry.getKey() + "' is an invalid symbol (must be 1 character only).");
                }

                if (" ".equals(entry.getKey())) {
                    throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
                }

                map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
            }

            map.put(" ", Ingredient.EMPTY);
            return map;
        }

        private static NonNullList<Ingredient> dissolvePattern(String[] pPattern, Map<String, Ingredient> pKeys, int pPatternWidth, int pPatternHeight) {
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(pPatternWidth * pPatternHeight, Ingredient.EMPTY);
            Set<String> set = Sets.newHashSet(pKeys.keySet());
            set.remove(" ");

            for(int i = 0; i < pPattern.length; ++i) {
                for(int j = 0; j < pPattern[i].length(); ++j) {
                    String s = pPattern[i].substring(j, j + 1);
                    Ingredient ingredient = pKeys.get(s);
                    if (ingredient == null) {
                        throw new JsonSyntaxException("Pattern references symbol '" + s + "' but it's not defined in the key");
                    }

                    set.remove(s);
                    nonnulllist.set(j + pPatternWidth * i, ingredient);
                }
            }

            if (!set.isEmpty()) {
                throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
            } else {
                return nonnulllist;
            }
        }

        @VisibleForTesting
        static String[] shrink(String... pToShrink) {
            int i = Integer.MAX_VALUE;
            int j = 0;
            int k = 0;
            int l = 0;

            for(int i1 = 0; i1 < pToShrink.length; ++i1) {
                String s = pToShrink[i1];
                i = Math.min(i, firstNonSpace(s));
                int j1 = lastNonSpace(s);
                j = Math.max(j, j1);
                if (j1 < 0) {
                    if (k == i1) {
                        ++k;
                    }

                    ++l;
                } else {
                    l = 0;
                }
            }

            if (pToShrink.length == l) {
                return new String[0];
            } else {
                String[] astring = new String[pToShrink.length - l - k];

                for(int k1 = 0; k1 < astring.length; ++k1) {
                    astring[k1] = pToShrink[k1 + k].substring(i, j + 1);
                }

                return astring;
            }
        }

        private static int firstNonSpace(String pEntry) {
            int i;
            for(i = 0; i < pEntry.length() && pEntry.charAt(i) == ' '; ++i) {
            }

            return i;
        }

        private static int lastNonSpace(String pEntry) {
            int i;
            for(i = pEntry.length() - 1; i >= 0 && pEntry.charAt(i) == ' '; --i) {
            }

            return i;
        }

        @Nullable
        @Override
        public MillingMachineRecipe fromNetwork(ResourceLocation pRecipeId, PacketBuffer pBuffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);

            boolean step1 = pBuffer.readBoolean();
            if (step1) {
                ItemStack temp = pBuffer.readItem();
                inputs.set(0, Ingredient.of(temp));

            } else {
                for (int i = 0; i < inputs.size(); i++) {
                    ItemStack temp = pBuffer.readItem();
                    if (temp.getItem() != Items.AIR) {
                        inputs.set(i, Ingredient.of(temp));

                    }

                }

            }

            ItemStack output = pBuffer.readItem();

            return new MillingMachineRecipe(pRecipeId, output, inputs, step1);
        }

        @Override
        public void toNetwork(PacketBuffer pBuffer, MillingMachineRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.getIngredients().size());
            for (Ingredient i : pRecipe.getIngredients()) {
                i.toNetwork(pBuffer);
            }
            pBuffer.writeItemStack(pRecipe.getResultItem(), false);

        }
    }
}
