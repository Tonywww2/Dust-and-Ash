package com.tonywww.dustandash.platform;

import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.RecipeManager;
import java.util.List;
import java.util.Optional;
public final class PlatformRecipes {
    private PlatformRecipes() {}
    private static final java.util.Map<PlatformRecipe, net.minecraft.resources.ResourceLocation> IDS = java.util.Collections.synchronizedMap(new java.util.WeakHashMap<>());
    public static net.minecraft.resources.ResourceLocation id(PlatformRecipe recipe, net.minecraft.resources.ResourceLocation fallback) {
        return IDS.getOrDefault(recipe, fallback);
    }
    public interface CachedCheck<R extends PlatformRecipe> { Optional<R> getRecipeFor(Container input, Level level); }
    public static <R extends PlatformRecipe> CachedCheck<R> createCheck(RecipeType<R> type) {
//? if forge {
RecipeManager.CachedCheck<Container,R> check=RecipeManager.createCheck(type);
return check::getRecipeFor;
//?} else {
/*RecipeManager.CachedCheck<PlatformRecipeInput,R> check=RecipeManager.createCheck(type);
return (input,level)->check.getRecipeFor(new PlatformRecipeInput(input),level).map(PlatformRecipes::bind);
*///?}
    }
    public static <R extends PlatformRecipe> List<R> all(RecipeManager manager, RecipeType<R> type) {
//? if forge {
return manager.getAllRecipesFor(type);
//?} else {
/*return manager.getAllRecipesFor(type).stream().map(PlatformRecipes::bind).toList();
*///?}
    }
    //? if neoforge {
    /*private static <R extends PlatformRecipe> R bind(net.minecraft.world.item.crafting.RecipeHolder<R> holder) {
        IDS.put(holder.value(), holder.id());
        return holder.value();
    }
    *///?}
}
