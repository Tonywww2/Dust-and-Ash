package com.tonywww.dustandash.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.item.ModItems;
import net.minecraft.core.NonNullList;
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
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

import static com.tonywww.dustandash.data.recipes.RecipeUtils.itemsFromJson;

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
//                if (!(recipeItems.get(i).test(RecipeUtils.EMPTY) && itemStack.isEmpty())
//                        || !recipeItems.get(i).test(itemStack)) {
                return false;

            }

        }

        return true;

    }

    @Override
    public ItemStack assemble(Container pInv) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
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
    public RecipeSerializer<?> getSerializer() {
        return ModRecipe.CENTRIFUGE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CentrifugeRecipeType.INSTANCE;
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

    public static class CentrifugeRecipeType implements RecipeType<CentrifugeRecipe> {
        public static final CentrifugeRecipeType INSTANCE = new CentrifugeRecipeType();
        public static final String ID = "centrifuge";

    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<CentrifugeRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(DustAndAsh.MOD_ID, "centrifuge");

        @Override
        public CentrifugeRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
//            System.out.println(pRecipeId + " start from json");
//            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            int tick = GsonHelper.getAsInt(json, "tick");
            JsonArray outputArr = GsonHelper.getAsJsonArray(json, "outputs");

//            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);
//            NonNullList<ItemStack> outputs = NonNullList.withSize(OUTPUT_SLOTS, ItemStack.EMPTY);
            NonNullList<Ingredient> inputs = itemsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"), MAX_SLOTS);
            NonNullList<ItemStack> outputs = NonNullList.withSize(OUTPUT_SLOTS, ItemStack.EMPTY);


//            for (int i = 0; i < ingredients.size(); i++) {
//                Ingredient temp = Ingredient.fromJson(ingredients.get(i));
//
//                if (temp.getItems()[0].getItem() != ModItems.EMPTY.get()) {
//                    inputs.set(i, temp);
//
//                }
//
//            }

            for (int i = 0; i < outputs.size(); i++) {
                ItemStack temp = Ingredient.fromJson(outputArr.get(i)).getItems()[0];

                if (temp.getItem() != ModItems.EMPTY.get()) {
                    outputs.set(i, temp);

                }

            }

            return new CentrifugeRecipe(pRecipeId, outputs, inputs, tick);
        }


        @Nullable
        @Override
        public CentrifugeRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
//            System.out.println(pRecipeId + " start from network");
            // 3 readInt
            int tick = pBuffer.readInt();
            // 01 readVarInt
            int inputSize = pBuffer.readVarInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);
            for (int i = 0; i < inputSize; i++) {
                // 1 fromNetwork
                Ingredient temp = Ingredient.fromNetwork(pBuffer);
                inputs.set(i, temp);

            }

            // 02 readVarInt
            int outputSize = pBuffer.readVarInt();
            NonNullList<ItemStack> output = NonNullList.withSize(OUTPUT_SLOTS, ItemStack.EMPTY);
            for (int i = 0; i < outputSize; i++) {
                // 2 readItem
                ItemStack temp = pBuffer.readItem();
                output.set(i, temp);

            }

            return new CentrifugeRecipe(pRecipeId, output, inputs, tick);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, CentrifugeRecipe pRecipe) {
//            System.out.println(pRecipe.id + " start to network");
            // 3 writeInt
            pBuffer.writeInt(pRecipe.getTick());

            // 01 writeVarInt
            pBuffer.writeVarInt(pRecipe.getIngredients().size());

            for (Ingredient i : pRecipe.getIngredients()) {
                // 1 toNetwork
                i.toNetwork(pBuffer);

            }

            // 02 writeVarInt
            pBuffer.writeVarInt(pRecipe.getResultItemStacks().size());

            for (ItemStack i : pRecipe.getResultItemStacks()) {
                // 2 writeItem
                pBuffer.writeItemStack(i, false);

            }

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
