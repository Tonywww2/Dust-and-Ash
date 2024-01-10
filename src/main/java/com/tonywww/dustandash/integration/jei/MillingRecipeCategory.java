package com.tonywww.dustandash.integration.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.data.recipes.IntegratedBlockRecipe;
import com.tonywww.dustandash.data.recipes.MillingMachineRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.SlotItemHandler;

public class MillingRecipeCategory implements IRecipeCategory<MillingMachineRecipe> {

    public final static ResourceLocation UID = new ResourceLocation(DustAndAsh.MOD_ID, "milling");
    public final static ResourceLocation TEXTURE = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/milling_machine_gui.png");

    private final IDrawable bg;
    private final IDrawable icon;
    private final IDrawable wp;

    public MillingRecipeCategory(IGuiHelper helper) {
        this.bg = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.MILLING_MACHINE.get()));

        this.wp = helper.createDrawable(TEXTURE, 0, 173, 97, 82);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends MillingMachineRecipe> getRecipeClass() {
        return MillingMachineRecipe.class;
    }

    @Override
    public String getTitle() {
        return ModBlocks.MILLING_MACHINE.get().getName().getString();
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
    public void setIngredients(MillingMachineRecipe millingMachineRecipe, IIngredients iIngredients) {
        iIngredients.setInputIngredients(millingMachineRecipe.getIngredients());
        iIngredients.setOutput(VanillaTypes.ITEM, millingMachineRecipe.getResultItem());

    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, MillingMachineRecipe recipe, IIngredients ingredients) {

        IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();

        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                itemStacks.init((5 * i) + j - 3, true, 70 + (16 * j), (16 * i) - 13);

            }
        }

        if (recipe.isStep1()) {
            itemStacks.init(0, true, 27, 9);
            itemStacks.set(0, recipe.getIngredients().get(0).getItems()[0]);

            for (int i = 3; i <= 27; i++) {
                itemStacks.set(i, recipe.getResultItem());

            }

        } else {
//            itemStacks.set(1, recipe.getIngredients().get(0).getItems()[0]);
//            for (int i = 3; i <= 27; i++) {
//                itemStacks.set(i, recipe.getIngredients().get(i - 2).getItems()[0]);
//
//            }
            itemStacks.init(1, true, 27, 37);
            itemStacks.set(ingredients);

            itemStacks.init(2, false, 27, 65);
            itemStacks.set(2, recipe.getResultItem());

        }


    }

    @Override
    public void draw(MillingMachineRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        this.wp.draw(matrixStack, 71, 3);

    }
}
