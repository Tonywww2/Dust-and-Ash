package com.tonywww.dustandash.integration.jei;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.integration.DustAndAshRecipeTypes;
import com.tonywww.dustandash.registeries.ModBlocks;
import com.tonywww.dustandash.data.recipes.MillingMachineRecipe;
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


public class MillingRecipeCategory implements IRecipeCategory<MillingMachineRecipe> {

//    public static final RecipeType<MillingMachineRecipe> RECIPE_TYPE = RecipeType.create(DustAndAsh.MOD_ID, "milling", MillingMachineRecipe.class);
    public final static ResourceLocation TEXTURE = DustAndAsh.prefix("textures/gui/milling_machine_jei.png");

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
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MillingMachineRecipe recipe, IFocusGroup focuses) {

        var level = Minecraft.getInstance().level;

        assert level != null;

        var inputs = recipe.getIngredients();
        var output = recipe.getResultItem(level.registryAccess());

        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                if (recipe.isStep1()) {
                    builder.addSlot(RecipeIngredientRole.OUTPUT, 71 + (16 * j), (16 * i) - 12).addItemStack(output);

                } else {
                    builder.addSlot(RecipeIngredientRole.INPUT, 71 + (16 * j), (16 * i) - 12).addIngredients(inputs.get((5 * i) + j - 5));

                }

            }
        }

        if (recipe.isStep1()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 28, 10).addIngredients(inputs.get(0));

        } else {
            builder.addSlot(RecipeIngredientRole.INPUT, 28, 38).addIngredients(inputs.get(0));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 28, 66).addItemStack(output);

        }


    }

    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(MillingMachineRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.wp.draw(guiGraphics, 71, 3);

    }
}
