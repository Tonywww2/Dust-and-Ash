package com.tonywww.dustandash.platform;

import net.minecraft.world.Container;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
//? if forge {
public interface PlatformRecipe extends net.minecraft.world.item.crafting.Recipe<Container> {
//?} else {
/*public interface PlatformRecipe extends net.minecraft.world.item.crafting.Recipe<PlatformRecipeInput> {
    boolean matches(Container inventory, Level level);
    ItemStack assemble(Container inventory, RegistryAccess registries);
    ItemStack getResultItem(RegistryAccess registries);
    @Override default boolean matches(PlatformRecipeInput input, Level level) { return matches(input.container(),level); }
    @Override default ItemStack assemble(PlatformRecipeInput input, net.minecraft.core.HolderLookup.Provider registries) { return assemble(input.container(), registries instanceof RegistryAccess access ? access : null); }
    @Override default ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider registries) { return getResultItem(registries instanceof RegistryAccess access ? access : null); }
*///?}

    ResourceLocation getId();
}
