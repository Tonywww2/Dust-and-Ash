package com.tonywww.dustandash.integration.jei;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.data.recipes.CentrifugeRecipe;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

import java.util.Arrays;

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
    public RecipeType<CentrifugeRecipe> getRecipeType() {
        return DustAndAshRecipeTypes.CENTRIFUGE;
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

//    @Override
//    public void setIngredients(CentrifugeRecipe centrifugeRecipe, IIngredients iIngredients) {
//        iIngredients.setInputIngredients(centrifugeRecipe.getIngredients());
//
//        NonNullList<ItemStack> l = NonNullList.create();
//
//        for (ItemStack i : centrifugeRecipe.getResultItemStacks()) {
//            if (!i.isEmpty()) {
//                l.add(i);
//            }
//
//        }
//
//        iIngredients.setOutputs(VanillaTypes.ITEM, l);
//
//    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CentrifugeRecipe recipe, IFocusGroup focusGroup) {

//        IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();

        builder.addSlot(RecipeIngredientRole.INPUT, 80, 6).addItemStacks(Arrays.asList(recipe.getIngredients().get(0).getItems()));
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 27).addItemStacks(Arrays.asList(recipe.getIngredients().get(1).getItems()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 22, 12).addItemStack(recipe.getResultItemStacks().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 42, 30).addItemStack(recipe.getResultItemStacks().get(1));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 22, 48).addItemStack(recipe.getResultItemStacks().get(2));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 42, 66).addItemStack(recipe.getResultItemStacks().get(3));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 138, 12).addItemStack(recipe.getResultItemStacks().get(4));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 118, 30).addItemStack(recipe.getResultItemStacks().get(5));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 138, 48).addItemStack(recipe.getResultItemStacks().get(6));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 118, 66).addItemStack(recipe.getResultItemStacks().get(7));


    }

    @Override
    public void draw(CentrifugeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        guiGraphics.drawString(font, recipe.getTick() + " ticks", 65, 70, 0x555555);

    }
}
