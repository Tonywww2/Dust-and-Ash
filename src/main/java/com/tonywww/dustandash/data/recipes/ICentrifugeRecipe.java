package com.tonywww.dustandash.data.recipes;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;


public interface ICentrifugeRecipe extends IRecipe<IInventory> {

    ResourceLocation TYPE_ID = new ResourceLocation(DustAndAsh.MOD_ID, "centrifuge");

    @Override
    default IRecipeType<?> getType(){
        return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
    }

    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    default boolean isSpecial() {
        return false;
    }
}
