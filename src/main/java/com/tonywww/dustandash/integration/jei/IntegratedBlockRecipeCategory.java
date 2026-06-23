package com.tonywww.dustandash.integration.jei;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.integration.DustAndAshRecipeTypes;
import com.tonywww.dustandash.registeries.ModBlocks;
import com.tonywww.dustandash.data.recipes.IntegratedBlockRecipe;
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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class IntegratedBlockRecipeCategory implements IRecipeCategory<IntegratedBlockRecipe> {

//    public static final RecipeType<IntegratedBlockRecipe> RECIPE_TYPE = RecipeType.create(DustAndAsh.MOD_ID, "integrate", IntegratedBlockRecipe.class);
    public final static ResourceLocation TEXTURE = DustAndAsh.prefix("textures/gui/integrated_block_gui.png");

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
        var level = Minecraft.getInstance().level;

        assert level != null;

        var inputs = recipe.getIngredients();
        var output = recipe.getResultItem(level.registryAccess());

        builder.addSlot(RecipeIngredientRole.INPUT, 36, 42).addIngredients(inputs.get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 124, 42).addIngredients(inputs.get(1));

        builder.addSlot(RecipeIngredientRole.INPUT, 58, 31).addIngredients(inputs.get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 31).addIngredients(inputs.get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 102, 31).addIngredients(inputs.get(4));

        builder.addSlot(RecipeIngredientRole.INPUT, 58, 53).addIngredients(inputs.get(5));
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 53).addIngredients(inputs.get(6));
        builder.addSlot(RecipeIngredientRole.INPUT, 102, 53).addIngredients(inputs.get(7));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 10).addItemStack(output);

    }

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
