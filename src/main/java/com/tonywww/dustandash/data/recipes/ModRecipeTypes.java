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
        Registry.register(Registry.RECIPE_TYPE, MillingMachineRecipe.TYPE_ID, MILLING_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, CentrifugeRecipe.TYPE_ID, CENTRIFUGE_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, IonizerRecipe.TYPE_ID, IONIZER_RECIPE);

    }

    public static final RegistryObject<IntegratedBlockRecipe.Serializer> INTEGRATE_SERIALIZER = RECIPE_SERIALIZER.register("integrate", IntegratedBlockRecipe.Serializer::new);
    public static IRecipeType<IntegratedBlockRecipe> INTEGRATE_RECIPE = new IntegratedBlockRecipe.IntegrateRecipeType();

    public static final RegistryObject<MillingMachineRecipe.Serializer> MILLING_SERIALIZER = RECIPE_SERIALIZER.register("milling", MillingMachineRecipe.Serializer::new);
    public static IRecipeType<MillingMachineRecipe> MILLING_RECIPE = new MillingMachineRecipe.MillingRecipeType();

    public static final RegistryObject<CentrifugeRecipe.Serializer> CENTRIFUGE_SERIALIZER = RECIPE_SERIALIZER.register("centrifuge", CentrifugeRecipe.Serializer::new);
    public static IRecipeType<CentrifugeRecipe> CENTRIFUGE_RECIPE = new CentrifugeRecipe.CentrifugeRecipeType();

    public static final RegistryObject<IonizerRecipe.Serializer> IONIZER_SERIALIZER = RECIPE_SERIALIZER.register("ionizer", IonizerRecipe.Serializer::new);
    public static IRecipeType<IonizerRecipe> IONIZER_RECIPE = new IonizerRecipe.IonizerRecipeType();

}
