package com.tonywww.dustandash.platform;

//? if forge {

//?} else {
/*import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
public record PlatformRecipeInput(Container container) implements RecipeInput {
    public ItemStack getItem(int index) { return container.getItem(index); }
    public int size() { return container.getContainerSize(); }
}
*///?}
