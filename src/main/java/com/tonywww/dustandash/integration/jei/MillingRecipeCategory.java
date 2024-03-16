package com.tonywww.dustandash.integration.jei;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.data.recipes.MillingMachineRecipe;
import com.tonywww.dustandash.integration.DustAndAshRecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class MillingRecipeCategory implements IRecipeCategory<MillingMachineRecipe> {

    public final static ResourceLocation UID = new ResourceLocation(DustAndAsh.MOD_ID, "milling");
    public final static ResourceLocation TEXTURE = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/milling_machine_gui.png");

    private final IDrawable bg;
    private final IDrawable icon;
    private final IDrawable wp;

    public MillingRecipeCategory(IGuiHelper helper) {
        this.bg = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.MILLING_MACHINE.get()));

        this.wp = helper.createDrawable(TEXTURE, 0, 173, 97, 82);
    }

    @Override
    public RecipeType<MillingMachineRecipe> getRecipeType() {
        return DustAndAshRecipeTypes.MILLING;
    }

    @Override
    public Component getTitle() {
        return ModBlocks.MILLING_MACHINE.get().getName();
    }

    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

//    @Override
//    public void setIngredients(MillingMachineRecipe millingMachineRecipe, IIngredients iIngredients) {
//        iIngredients.setInputIngredients(millingMachineRecipe.getIngredients());
//        iIngredients.setOutput(VanillaTypes.ITEM_STACK, millingMachineRecipe.getResultItem());
//
//    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MillingMachineRecipe recipe, IFocusGroup focuses) {

//        IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();

        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                if (recipe.isStep1()) {
                    builder.addSlot(RecipeIngredientRole.OUTPUT, 71 + (16 * j), (16 * i) - 12).addItemStack(recipe.getResultItem(null));

                } else {
                    builder.addSlot(RecipeIngredientRole.INPUT, 71 + (16 * j), (16 * i) - 12).addItemStacks(Arrays.asList(recipe.getIngredients().get((5 * i) + j - 5).getItems()));

                }

            }
        }

        if (recipe.isStep1()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 28, 10).addItemStacks(Arrays.asList(recipe.getIngredients().get(0).getItems()));

        } else {
            builder.addSlot(RecipeIngredientRole.INPUT, 28, 38).addItemStacks(Arrays.asList(recipe.getIngredients().get(0).getItems()));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 28, 66).addItemStack(recipe.getResultItem(null));

        }


    }

    @Override
    public void draw(MillingMachineRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.wp.draw(guiGraphics, 71, 3);

    }
}
