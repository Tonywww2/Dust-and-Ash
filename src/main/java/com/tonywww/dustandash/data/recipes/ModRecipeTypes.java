package com.tonywww.dustandash.data.recipes;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeTypes {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DustAndAsh.MOD_ID);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZER.register(eventBus);

        Registry.register(Registry.RECIPE_TYPE, IntegratedBlockRecipe.TYPE_ID, INTEGRATE_RECIPE);
    }

    public static final RegistryObject<IntegratedBlockRecipe.Serializer> INTEGRATE_SERIALIZER = RECIPE_SERIALIZER.register("integrate", IntegratedBlockRecipe.Serializer::new);

    public static IRecipeType<IntegratedBlockRecipe> INTEGRATE_RECIPE = new IntegratedBlockRecipe.IntegrateRecipeType();
}
