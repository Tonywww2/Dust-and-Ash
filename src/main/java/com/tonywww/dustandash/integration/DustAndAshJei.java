package com.tonywww.dustandash.integration;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.container.IntegratedBlockContainer;
import com.tonywww.dustandash.data.recipes.CentrifugeRecipe;
import com.tonywww.dustandash.data.recipes.IntegratedBlockRecipe;
import com.tonywww.dustandash.data.recipes.MillingMachineRecipe;
import com.tonywww.dustandash.integration.jei.CentrifugeRecipeCategory;
import com.tonywww.dustandash.integration.jei.IntegratedBlockRecipeCategory;
import com.tonywww.dustandash.integration.jei.MillingRecipeCategory;
import com.tonywww.dustandash.screen.CentrifugeScreen;
import com.tonywww.dustandash.screen.IntegratedBlockScreen;
import com.tonywww.dustandash.screen.MillingMachineScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;
import java.util.stream.Collectors;

@JeiPlugin
public class DustAndAshJei implements IModPlugin {

    private static final ResourceLocation PID = new ResourceLocation(DustAndAsh.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new IntegratedBlockRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new MillingRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new CentrifugeRecipeCategory(registration.getJeiHelpers().getGuiHelper())

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

    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(IntegratedBlockScreen.class, 80, 9, 16, 16, IntegratedBlockRecipeCategory.UID);

        registration.addRecipeClickArea(MillingMachineScreen.class, 50, 12, 15, 11, MillingRecipeCategory.UID);

        registration.addRecipeClickArea(CentrifugeScreen.class, 80, 51, 16, 16, CentrifugeRecipeCategory.UID);

    }



    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(IntegratedBlockContainer.class, IntegratedBlockRecipeCategory.UID, 36, 8, 0, 36);
    }


}
