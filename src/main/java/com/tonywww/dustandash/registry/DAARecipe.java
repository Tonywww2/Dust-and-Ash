package com.tonywww.dustandash.registry;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.data.recipes.CentrifugeRecipe;
import com.tonywww.dustandash.data.recipes.IntegratedBlockRecipe;
import com.tonywww.dustandash.data.recipes.IonizerRecipe;
import com.tonywww.dustandash.data.recipes.MillingMachineRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class DAARecipe {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_TYPE = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, DustAndAsh.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, DustAndAsh.MOD_ID);

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_TYPE.register(eventBus);

    }

    public static final RegistryObject<IntegratedBlockRecipe.IntegrateRecipeType> INTEGRATE_TYPE =
            RECIPE_TYPES.register("integrate", () -> IntegratedBlockRecipe.IntegrateRecipeType.INSTANCE);

    public static final RegistryObject<MillingMachineRecipe.MillingRecipeType> MILLING_TYPE =
            RECIPE_TYPES.register("milling", () -> MillingMachineRecipe.MillingRecipeType.INSTANCE);

    public static final RegistryObject<CentrifugeRecipe.CentrifugeRecipeType> CENTRIFUGE_TYPE =
            RECIPE_TYPES.register("centrifuge", () -> CentrifugeRecipe.CentrifugeRecipeType.INSTANCE);

    public static final RegistryObject<IonizerRecipe.IonizerRecipeType> IONIZER_TYPE =
            RECIPE_TYPES.register("ionizer", () -> IonizerRecipe.IonizerRecipeType.INSTANCE);

    public static final RegistryObject<IntegratedBlockRecipe.Serializer> INTEGRATE_SERIALIZER =
            RECIPE_TYPE.register("integrate", () -> IntegratedBlockRecipe.Serializer.INSTANCE);

    public static final RegistryObject<MillingMachineRecipe.Serializer> MILLING_SERIALIZER =
            RECIPE_TYPE.register("milling", () -> MillingMachineRecipe.Serializer.INSTANCE);

    public static final RegistryObject<CentrifugeRecipe.Serializer> CENTRIFUGE_SERIALIZER =
            RECIPE_TYPE.register("centrifuge", () -> CentrifugeRecipe.Serializer.INSTANCE);

    public static final RegistryObject<IonizerRecipe.Serializer> IONIZER_SERIALIZER =
            RECIPE_TYPE.register("ionizer", () -> IonizerRecipe.Serializer.INSTANCE);

}
