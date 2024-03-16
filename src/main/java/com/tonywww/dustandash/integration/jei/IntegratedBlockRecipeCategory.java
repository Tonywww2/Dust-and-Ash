package com.tonywww.dustandash.integration.jei;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.data.recipes.IntegratedBlockRecipe;
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

public class IntegratedBlockRecipeCategory implements IRecipeCategory<IntegratedBlockRecipe> {

    public final static ResourceLocation UID = new ResourceLocation(DustAndAsh.MOD_ID, "integrate");
    public final static ResourceLocation TEXTURE = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/integrated_block_gui.png");

    private final IDrawable bg;
    private final IDrawable icon;
    private final IDrawable lv11;
    private final IDrawable lv12;
    private final IDrawable lv13;
    private final IDrawable lv14;
    private final IDrawable lv2;
    private final IDrawable lv3;

    public IntegratedBlockRecipeCategory(IGuiHelper helper) {
        this.bg = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.INTEGRATED_BLOCK.get()));

        this.lv11 = helper.createDrawable(TEXTURE, 176, 0, 9, 9);
        this.lv12 = helper.createDrawable(TEXTURE, 179, 0, 9, 9);
        this.lv13 = helper.createDrawable(TEXTURE, 176, 3, 9, 9);
        this.lv14 = helper.createDrawable(TEXTURE, 179, 3, 9, 9);

        this.lv2 = helper.createDrawable(TEXTURE, 188, 0, 54, 46);

        this.lv3 = helper.createDrawable(TEXTURE, 0, 168, 112, 34);
    }



    @Override
    public RecipeType<IntegratedBlockRecipe> getRecipeType() {
        return DustAndAshRecipeTypes.INTEGRATE;
    }

    @Override
    public Component getTitle() {
        return ModBlocks.INTEGRATED_BLOCK.get().getName();
    }

    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IntegratedBlockRecipe recipe, IFocusGroup focuses) {
//        IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();

        builder.addSlot(RecipeIngredientRole.INPUT, 36, 42).addItemStacks(Arrays.asList(recipe.getIngredients().get(0).getItems()));
        builder.addSlot(RecipeIngredientRole.INPUT, 124, 42).addItemStacks(Arrays.asList(recipe.getIngredients().get(1).getItems()));
        builder.addSlot(RecipeIngredientRole.INPUT, 58, 31).addItemStacks(Arrays.asList(recipe.getIngredients().get(2).getItems()));
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 31).addItemStacks(Arrays.asList(recipe.getIngredients().get(3).getItems()));
        builder.addSlot(RecipeIngredientRole.INPUT, 102, 31).addItemStacks(Arrays.asList(recipe.getIngredients().get(4).getItems()));
        builder.addSlot(RecipeIngredientRole.INPUT, 58, 53).addItemStacks(Arrays.asList(recipe.getIngredients().get(5).getItems()));
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 53).addItemStacks(Arrays.asList(recipe.getIngredients().get(6).getItems()));
        builder.addSlot(RecipeIngredientRole.INPUT, 102, 53).addItemStacks(Arrays.asList(recipe.getIngredients().get(7).getItems()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 10).addItemStack(recipe.getResultItem(null));

    }

//    @Override
//    public void setIngredients(IntegratedBlockRecipe integratedBlockRecipe, IIngredients iIngredients) {
//        NonNullList<Ingredient> inputs = NonNullList.create();
//        for (Ingredient i : integratedBlockRecipe.getIngredients()) {
//            inputs.add(i);
//
//        }
//        switch (integratedBlockRecipe.getLevel()) {
//            case 1:
//                inputs.add(Ingredient.of(ModBlocks.INTEGRATED_FRAME_1.get()));
//                break;
//
//            case 2:
//                inputs.add(Ingredient.of(ModBlocks.INTEGRATED_FRAME_2.get()));
//                break;
//
//            case 3:
//                inputs.add(Ingredient.of(ModBlocks.INTEGRATED_FRAME_3.get()));
//                break;
//        }
//
//        iIngredients.setInputIngredients(inputs);
//        iIngredients.setOutput(VanillaTypes.ITEM, integratedBlockRecipe.getResultItem());
//
//    }

    @Override
    public void draw(IntegratedBlockRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (recipe.getLevel() > 0) {
            this.lv11.draw(guiGraphics, 54, 27);
            this.lv12.draw(guiGraphics, 113, 27);
            this.lv13.draw(guiGraphics, 54, 64);
            this.lv14.draw(guiGraphics, 113, 64);

            if (recipe.getLevel() > 1) {
                this.lv2.draw(guiGraphics, 61, 27);

                if (recipe.getLevel() > 2) {
                    this.lv3.draw(guiGraphics, 32, 33);

                }
            }

        }
    }
}
