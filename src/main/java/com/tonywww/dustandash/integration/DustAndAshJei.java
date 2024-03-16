package com.tonywww.dustandash.integration;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.menu.IntegratedBlockContainer;
import com.tonywww.dustandash.data.recipes.*;
import com.tonywww.dustandash.integration.jei.CentrifugeRecipeCategory;
import com.tonywww.dustandash.integration.jei.IntegratedBlockRecipeCategory;
import com.tonywww.dustandash.integration.jei.IonizerCategory;
import com.tonywww.dustandash.integration.jei.MillingRecipeCategory;
import com.tonywww.dustandash.menu.ModMenus;
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

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new IntegratedBlockRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(
                new MillingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(
                new CentrifugeRecipeCategory(registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(
                new IonizerCategory(registration.getJeiHelpers().getGuiHelper()));

    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        registration.addRecipes(DustAndAshRecipeTypes.INTEGRATE,
                rm.getAllRecipesFor(IntegratedBlockRecipe.IntegrateRecipeType.INSTANCE).stream().toList());

        registration.addRecipes(DustAndAshRecipeTypes.MILLING,
                rm.getAllRecipesFor(MillingMachineRecipe.MillingRecipeType.INSTANCE).stream().toList());

        registration.addRecipes(DustAndAshRecipeTypes.CENTRIFUGE,
                rm.getAllRecipesFor(CentrifugeRecipe.CentrifugeRecipeType.INSTANCE).stream().toList());

        registration.addRecipes(DustAndAshRecipeTypes.IONIZER,
                rm.getAllRecipesFor(IonizerRecipe.IonizerRecipeType.INSTANCE).stream().toList());

//        registration.addRecipes(rm.getRecipes().stream()
//                        .filter(r -> r instanceof IntegratedBlockRecipe).collect(Collectors.toList()),
//                IntegratedBlockRecipeCategory.UID
//        );
//
//        registration.addRecipes(rm.getRecipes().stream()
//                        .filter(r -> r instanceof MillingMachineRecipe).collect(Collectors.toList()),
//                MillingRecipeCategory.UID
//        );
//
//        registration.addRecipes(rm.getRecipes().stream()
//                        .filter(r -> r instanceof CentrifugeRecipe).collect(Collectors.toList()),
//                CentrifugeRecipeCategory.UID
//        );
//
//        registration.addRecipes(rm.getRecipes().stream()
//                        .filter(r -> r instanceof IonizerRecipe).collect(Collectors.toList()),
//                IonizerCategory.UID
//        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.INTEGRATED_BLOCK.get()),
                DustAndAshRecipeTypes.INTEGRATE
        );

        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.MILLING_MACHINE.get()),
                DustAndAshRecipeTypes.MILLING
        );

        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.CENTRIFUGE.get()),
                DustAndAshRecipeTypes.CENTRIFUGE
        );

        registration.addRecipeCatalyst(
                new ItemStack(ModBlocks.IONIZER.get()),
                DustAndAshRecipeTypes.IONIZER
        );

    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(IntegratedBlockScreen.class, 80, 9, 16, 16, DustAndAshRecipeTypes.INTEGRATE);

        registration.addRecipeClickArea(MillingMachineScreen.class, 50, 12, 15, 11, DustAndAshRecipeTypes.MILLING);

        registration.addRecipeClickArea(CentrifugeScreen.class, 80, 51, 16, 16, DustAndAshRecipeTypes.CENTRIFUGE);

        registration.addRecipeClickArea(IonizerScreen.class, 73, 28, 32, 11, DustAndAshRecipeTypes.IONIZER);

    }


    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(IntegratedBlockContainer.class, ModMenus.INTEGRATED_BLOCK_CONTAINER.get(), DustAndAshRecipeTypes.INTEGRATE, 36, 8, 0, 36);

    }


}
