package com.tonywww.dustandash.data.recipes;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipe {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_TYPE = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DustAndAsh.MOD_ID);

    public static void register(IEventBus eventBus) {
        RECIPE_TYPE.register(eventBus);

    }

    public static RecipeType<IntegratedBlockRecipe> INTEGRATE_RECIPE = new IntegratedBlockRecipe.IntegrateRecipeType();

    public static RecipeType<MillingMachineRecipe> MILLING_RECIPE = new MillingMachineRecipe.MillingRecipeType();

    public static RecipeType<CentrifugeRecipe> CENTRIFUGE_RECIPE = new CentrifugeRecipe.CentrifugeRecipeType();

    public static RecipeType<IonizerRecipe> IONIZER_RECIPE = new IonizerRecipe.IonizerRecipeType();

    public static final RegistryObject<IntegratedBlockRecipe.Serializer> INTEGRATE_SERIALIZER =
            RECIPE_TYPE.register("integrate", IntegratedBlockRecipe.Serializer::new);

    public static final RegistryObject<MillingMachineRecipe.Serializer> MILLING_SERIALIZER =
            RECIPE_TYPE.register("milling", () -> MillingMachineRecipe.Serializer.INSTANCE);

    public static final RegistryObject<CentrifugeRecipe.Serializer> CENTRIFUGE_SERIALIZER =
            RECIPE_TYPE.register("centrifuge", CentrifugeRecipe.Serializer::new);

    public static final RegistryObject<IonizerRecipe.Serializer> IONIZER_SERIALIZER =
            RECIPE_TYPE.register("ionizer", IonizerRecipe.Serializer::new);

}
