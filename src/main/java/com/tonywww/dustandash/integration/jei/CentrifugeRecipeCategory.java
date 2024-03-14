package com.tonywww.dustandash.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CentrifugeRecipeCategory implements IRecipeCategory<CentrifugeRecipe> {

    public final static ResourceLocation UID = new ResourceLocation(DustAndAsh.MOD_ID, "centrifuge");
    public final static ResourceLocation TEXTURE = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/centrifuge_gui.png");

    private final IDrawable bg;
    private final IDrawable icon;

    public CentrifugeRecipeCategory(IGuiHelper helper) {
        this.bg = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CENTRIFUGE.get()));
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
    public Component getTitle() {
        return ModBlocks.CENTRIFUGE.get().getName();
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

        NonNullList<ItemStack> l = NonNullList.create();

        for (ItemStack i : centrifugeRecipe.getResultItemStacks()) {
            if (!i.isEmpty()) {
                l.add(i);
            }

        }

        iIngredients.setOutputs(VanillaTypes.ITEM, l);

    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, CentrifugeRecipe recipe, IIngredients ingredients) {

        IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();

        itemStacks.init(0, true, 79, 5);
        itemStacks.init(1, true, 79, 26);
        itemStacks.set(ingredients);

        itemStacks.init(2, false, 21, 11);
        itemStacks.init(3, false, 41, 29);
        itemStacks.init(4, false, 21, 47);
        itemStacks.init(5, false, 41, 65);

        itemStacks.init(6, false, 137, 11);
        itemStacks.init(7, false, 117, 29);
        itemStacks.init(8, false, 137, 47);
        itemStacks.init(9, false, 117, 65);


        for (int i = 0; i < 8; i++) {
            if (recipe.getResultItemStacks().get(i) != ItemStack.EMPTY) {
                itemStacks.set(i + 2, recipe.getResultItemStacks().get(i));

            }

        }

    }

    @Override
    public void draw(CentrifugeRecipe recipe, PoseStack matrixStack, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        font.draw(matrixStack, recipe.getTick() + " ticks", 65, 70, 0x555555);
    }
}
