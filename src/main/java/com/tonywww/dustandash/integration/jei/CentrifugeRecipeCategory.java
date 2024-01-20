package com.tonywww.dustandash.integration.jei;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.data.recipes.CentrifugeRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CentrifugeRecipeCategory implements IRecipeCategory<CentrifugeRecipe> {

    public final static ResourceLocation UID = new ResourceLocation(DustAndAsh.MOD_ID, "centrifuge");
    public final static ResourceLocation TEXTURE = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/centrifuge_gui.png");

    private final IDrawable bg;
    private final IDrawable icon;

    public CentrifugeRecipeCategory(IGuiHelper helper) {
        this.bg = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.CENTRIFUGE.get()));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends CentrifugeRecipe> getRecipeClass() {
        return CentrifugeRecipe.class;
    }

    @Override
    public String getTitle() {
        return ModBlocks.CENTRIFUGE.get().getName().getString();
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
    public void setIngredients(CentrifugeRecipe centrifugeRecipe, IIngredients iIngredients) {
        iIngredients.setInputIngredients(centrifugeRecipe.getIngredients());
        iIngredients.setOutputs(VanillaTypes.ITEM, centrifugeRecipe.getResultItemStacks());

    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, CentrifugeRecipe recipe, IIngredients ingredients) {

        IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();

        itemStacks.init(0, true, 80, 6);
        itemStacks.init(1, true, 80, 27);

        itemStacks.init(2, false, 22, 12);
        itemStacks.init(3, false, 42, 30);
        itemStacks.init(4, false, 22, 48);
        itemStacks.init(5, false, 42, 66);

        itemStacks.init(6, false, 138, 12);
        itemStacks.init(7, false, 118, 30);
        itemStacks.init(8, false, 138, 48);
        itemStacks.init(9, false, 118, 66);


        itemStacks.set(ingredients);
        itemStacks.set(8, recipe.getResultItemStacks());

    }
}