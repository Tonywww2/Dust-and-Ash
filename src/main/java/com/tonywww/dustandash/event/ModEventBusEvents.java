package com.tonywww.dustandash.event;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.data.recipes.CentrifugeRecipe;
import com.tonywww.dustandash.data.recipes.IntegratedBlockRecipe;
import com.tonywww.dustandash.data.recipes.IonizerRecipe;
import com.tonywww.dustandash.data.recipes.MillingMachineRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = DustAndAsh.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

//    @SubscribeEvent
//    public static void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
//        Registry.register(Registry.RECIPE_TYPE, MillingMachineRecipe.MillingRecipeType.ID, MillingMachineRecipe.MillingRecipeType.INSTANCE);
//        Registry.register(Registry.RECIPE_TYPE, IonizerRecipe.IonizerRecipeType.ID, IonizerRecipe.IonizerRecipeType.INSTANCE);
//        Registry.register(Registry.RECIPE_TYPE, IntegratedBlockRecipe.IntegrateRecipeType.ID, IntegratedBlockRecipe.IntegrateRecipeType.INSTANCE);
//        Registry.register(Registry.RECIPE_TYPE, CentrifugeRecipe.CentrifugeRecipeType.ID, CentrifugeRecipe.CentrifugeRecipeType.INSTANCE);
//
//    }
}
