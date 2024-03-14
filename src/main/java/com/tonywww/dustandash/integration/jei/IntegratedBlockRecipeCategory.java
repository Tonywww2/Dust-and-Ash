package com.tonywww.dustandash.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.data.recipes.IntegratedBlockRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

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
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends IntegratedBlockRecipe> getRecipeClass() {
        return IntegratedBlockRecipe.class;
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
    public void setIngredients(IntegratedBlockRecipe integratedBlockRecipe, IIngredients iIngredients) {
        NonNullList<Ingredient> inputs = NonNullList.create();
        for (Ingredient i : integratedBlockRecipe.getIngredients()) {
            inputs.add(i);

        }
        switch (integratedBlockRecipe.getLevel()) {
            case 1:
                inputs.add(Ingredient.of(ModBlocks.INTEGRATED_FRAME_1.get()));
                break;

            case 2:
                inputs.add(Ingredient.of(ModBlocks.INTEGRATED_FRAME_2.get()));
                break;

            case 3:
                inputs.add(Ingredient.of(ModBlocks.INTEGRATED_FRAME_3.get()));
                break;
        }

        iIngredients.setInputIngredients(inputs);
        iIngredients.setOutput(VanillaTypes.ITEM, integratedBlockRecipe.getResultItem());

    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, IntegratedBlockRecipe recipe, IIngredients ingredients) {

        IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();

        itemStacks.init(0, true, 35, 41);
        itemStacks.init(1, true, 123, 41);
        itemStacks.init(2, true, 57, 30);
        itemStacks.init(3, true, 79, 30);
        itemStacks.init(4, true, 101, 30);
        itemStacks.init(5, true, 57, 52);
        itemStacks.init(6, true, 79, 52);
        itemStacks.init(7, true, 101, 52);
//        itemStacks.init(8, true, 3, 3);
        itemStacks.set(ingredients);

        itemStacks.init(9, false, 79, 9);
        itemStacks.set(9, recipe.getResultItem());

    }

    @Override
    public void draw(IntegratedBlockRecipe recipe, PoseStack matrixStack, double mouseX, double mouseY) {
        if (recipe.getLevel() > 0) {
            this.lv11.draw(matrixStack, 54, 27);
            this.lv12.draw(matrixStack, 113, 27);
            this.lv13.draw(matrixStack, 54, 64);
            this.lv14.draw(matrixStack, 113, 64);

            if (recipe.getLevel() > 1) {
                this.lv2.draw(matrixStack, 61, 27);

                if (recipe.getLevel() > 2) {
                    this.lv3.draw(matrixStack, 32, 33);

                }
            }

        }
    }
}
