package com.tonywww.dustandash.integration;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.menu.IntegratedBlockContainer;
import com.tonywww.dustandash.data.recipes.*;
import com.tonywww.dustandash.integration.jei.CentrifugeRecipeCategory;
import com.tonywww.dustandash.integration.jei.IntegratedBlockRecipeCategory;
import com.tonywww.dustandash.integration.jei.IonizerCategory;
import com.tonywww.dustandash.integration.jei.MillingRecipeCategory;
import com.tonywww.dustandash.screen.CentrifugeScreen;
import com.tonywww.dustandash.screen.IntegratedBlockScreen;
import com.tonywww.dustandash.screen.IonizerScreen;
import com.tonywww.dustandash.screen.MillingMachineScreen;
import mezz.jei.api.registration.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;


import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.stream.Collectors;

@JeiPlugin
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("unused")
public class DustAndAshJei implements IModPlugin {

    private static final ResourceLocation PID = new ResourceLocation(DustAndAsh.MOD_ID, "jei_plugin");
    private static final Minecraft MC = Minecraft.getInstance();

    @Override
    public ResourceLocation getPluginUid() {
        return PID;
    }

//    // From Farmer Delight
//    private static List<Recipe<?>> findRecipesByType(RecipeType<?> type) {
//        return MC.level
//                .getRecipeManager()
//                .getRecipes()
//                .stream()
//                .filter(r -> r.getType() == type)
//                .collect(Collectors.toList());
//    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new IntegratedBlockRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new MillingRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new CentrifugeRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new IonizerCategory(registration.getJeiHelpers().getGuiHelper())

        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        registration.addRecipes(rm.getRecipes().stream()
                        .filter(r -> r instanceof IntegratedBlockRecipe).collect(Collectors.toList()),
                IntegratedBlockRecipeCategory.UID
        );

        registration.addRecipes(rm.getRecipes().stream()
                        .filter(r -> r instanceof MillingMachineRecipe).collect(Collectors.toList()),
                MillingRecipeCategory.UID
        );

        registration.addRecipes(rm.getRecipes().stream()
                        .filter(r -> r instanceof CentrifugeRecipe).collect(Collectors.toList()),
                CentrifugeRecipeCategory.UID
        );

        registration.addRecipes(rm.getRecipes().stream()
                        .filter(r -> r instanceof IonizerRecipe).collect(Collectors.toList()),
                IonizerCategory.UID
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.INTEGRATED_BLOCK.get()),
                IntegratedBlockRecipeCategory.UID
        );

        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.MILLING_MACHINE.get()),
                MillingRecipeCategory.UID
        );

        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.CENTRIFUGE.get()),
                CentrifugeRecipeCategory.UID
        );

        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.IONIZER.get()),
                IonizerCategory.UID
        );

    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(IntegratedBlockScreen.class, 80, 9, 16, 16, IntegratedBlockRecipeCategory.UID);

        registration.addRecipeClickArea(MillingMachineScreen.class, 50, 12, 15, 11, MillingRecipeCategory.UID);

        registration.addRecipeClickArea(CentrifugeScreen.class, 80, 51, 16, 16, CentrifugeRecipeCategory.UID);

        registration.addRecipeClickArea(IonizerScreen.class, 73, 28, 32, 11, IonizerCategory.UID);

    }



    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(IntegratedBlockContainer.class, IntegratedBlockRecipeCategory.UID, 36, 8, 0, 36);

    }


}
