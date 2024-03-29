package com.tonywww.dustandash.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.item.ModItems;
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

import static com.tonywww.dustandash.data.recipes.RecipeUtils.itemsFromJson;

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
    public boolean matches(Container inv, Level pLevel) {
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
    public ItemStack assemble(Container pInv, RegistryAccess pRegistryAccess) {
        return null;
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
        return ModRecipe.IONIZER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return IonizerRecipeType.INSTANCE;
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

    public static class IonizerRecipeType implements RecipeType<IonizerRecipe> {
        public static final IonizerRecipeType INSTANCE = new IonizerRecipeType();
        public static final String ID = "ionizer";
    }

    public static class Serializer implements RecipeSerializer<IonizerRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(DustAndAsh.MOD_ID, "ionizer");

        @Override
        public IonizerRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
//            System.out.println(pRecipeId + " start from json");
//            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            ResourceLocation inputBlockStr = new ResourceLocation(GsonHelper.getAsString(json, "inputBlock"));

            JsonArray outputArr = GsonHelper.getAsJsonArray(json, "outputs");
            int cost = GsonHelper.getAsInt(json, "cost");
            int tick = GsonHelper.getAsInt(json, "tick");
            boolean costElectrodes = GsonHelper.getAsBoolean(json, "costElectrodes");
            ResourceLocation outputBlockStr = new ResourceLocation(GsonHelper.getAsString(json, "outputBlock"));

//            NonNullList<Ingredient> inputs = NonNullList.withSize(MAX_SLOTS, Ingredient.EMPTY);
            NonNullList<Ingredient> inputs = itemsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"), MAX_SLOTS);
            Block inputBlock = null;

            NonNullList<ItemStack> outputs = NonNullList.withSize(OUTPUT_SLOTS, ItemStack.EMPTY);
            Block outputBlock = null;


//            for (int i = 0; i < ingredients.size(); i++) {
//                Ingredient temp = Ingredient.fromJson(ingredients.get(i));
//
//                if (temp.getItems()[0].getItem() != ModItems.EMPTY.get()) {
//                    inputs.set(i, temp);
//
//                }
//
//            }

            if (inputBlockStr != ModItems.EMPTY.getId()) {
                inputBlock = ForgeRegistries.BLOCKS.getValue(inputBlockStr);

            }

            for (int i = 0; i < outputs.size(); i++) {
                ItemStack temp = Ingredient.fromJson(outputArr.get(i)).getItems()[0];

                if (temp.getItem() != ModItems.EMPTY.get()) {
                    outputs.set(i, temp);

                }

            }

            if (outputBlockStr != ModItems.EMPTY.getId()) {
                outputBlock = ForgeRegistries.BLOCKS.getValue(outputBlockStr);

            }

            return new IonizerRecipe(pRecipeId, inputs, inputBlock, outputs, cost, tick, costElectrodes, outputBlock);
        }

        @Nullable
        @Override
        public IonizerRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
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
            Block inputBlock = ForgeRegistries.BLOCKS.getValue(pBuffer.readResourceLocation());
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
            Block outputBlock = ForgeRegistries.BLOCKS.getValue(pBuffer.readResourceLocation());
            System.out.println("outputBlock: " + outputBlock);

            return new IonizerRecipe(pRecipeId, inputs, inputBlock, outputs, cost, tick, costElectrodes, outputBlock);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, IonizerRecipe pRecipe) {
//            System.out.println(pRecipe.id + " start to network");
            // 1 ingredients
            pBuffer.writeVarInt(pRecipe.getIngredients().size());
            for (Ingredient i : pRecipe.getIngredients()) {
                // 1 1 toNetwork
                i.toNetwork(pBuffer);
            }
            // 2 inputBlock
//            pBuffer.writeResourceLocation(pRecipe.inputBlock.getRegistryName());
            pBuffer.writeResourceLocation(ForgeRegistries.BLOCKS.getKey(pRecipe.inputBlock));

            // 3 outputs
            pBuffer.writeVarInt(pRecipe.getResultItemStacks().size());
            for (ItemStack i : pRecipe.getResultItemStacks()) {
                // 3 1 writeItem
                pBuffer.writeItemStack(i, false);
            }

            // 4 tick
            pBuffer.writeVarInt(pRecipe.tick);
            // 5 cost
            pBuffer.writeVarInt(pRecipe.powerCost);
            // 5.5 costElectrodes
            pBuffer.writeBoolean(pRecipe.costElectrodes);

            // 6 outputBlock
//            pBuffer.writeResourceLocation(pRecipe.outputBlock.getRegistryName());
            pBuffer.writeResourceLocation(ForgeRegistries.BLOCKS.getKey(pRecipe.outputBlock));

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
