package com.tonywww.dustandash.integration.jei;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.integration.DustAndAshRecipeTypes;
import com.tonywww.dustandash.registeries.ModBlocks;
import com.tonywww.dustandash.data.recipes.IonizerRecipe;
import com.tonywww.dustandash.registeries.ModItems;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LiquidBlock;

public class IonizerCategory implements IRecipeCategory<IonizerRecipe> {

    //    public static final RecipeType<IonizerRecipe> RECIPE_TYPE = RecipeType.create(DustAndAsh.MOD_ID, "ionizer", IonizerRecipe.class);
    public final static ResourceLocation TEXTURE = DustAndAsh.prefix("textures/gui/ionizer_gui.png");

    private final IDrawable bg;
    private final IDrawable icon;

    public IonizerCategory(IGuiHelper helper) {
        this.bg = helper.createDrawable(TEXTURE, 0, 0, 176, 90);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.IONIZER.get()));

    }

    @Override
    public RecipeType<IonizerRecipe> getRecipeType() {
        return DustAndAshRecipeTypes.IONIZER;
    }

    @Override
    public Component getTitle() {
        return ModBlocks.IONIZER.get().getName();
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IonizerRecipe recipe, IFocusGroup focuses) {
        var level = Minecraft.getInstance().level;

        assert level != null;

        var inputs = recipe.getIngredients();
        var outputs = recipe.getResultItemStacks();

        builder.addSlot(RecipeIngredientRole.INPUT, 16, 50).addIngredients(inputs.get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 70).addIngredients(inputs.get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 70).addIngredients(inputs.get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 62, 5).addIngredients(inputs.get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 98, 5).addIngredients(inputs.get(4));

        if (recipe.getPowerCost() > 0) {
            ItemStack stack = ModItems.ELECTRON.get().getDefaultInstance();
            stack.setCount(recipe.getPowerCost());
            builder.addSlot(RecipeIngredientRole.INPUT, 16, 30).addItemStack(stack);

        }

        // in block
        if (recipe.getInputBlock() instanceof LiquidBlock fluidBlock) {
            builder.addSlot(RecipeIngredientRole.INPUT, 60, 44).addFluidStack(fluidBlock.getFluid(), 1000);

        } else {
            ItemStack stack = recipe.getInputBlock().asItem().getDefaultInstance();
            if (!stack.isEmpty() && stack.getItem() != Items.AIR) {
                builder.addSlot(RecipeIngredientRole.INPUT, 60, 44).addItemStack(stack);

            }

        }

        builder.addSlot(RecipeIngredientRole.OUTPUT, 135, 49).addItemStack(outputs.get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 155, 49).addItemStack(outputs.get(1));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 135, 69).addItemStack(outputs.get(2));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 155, 69).addItemStack(outputs.get(3));

        // out block
        if (recipe.getResultBlock() instanceof LiquidBlock fluidBlock) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 101, 43).addFluidStack(fluidBlock.getFluid(), 1000);

        } else {
            ItemStack stack = recipe.getResultBlock().asItem().getDefaultInstance();
            if (!stack.isEmpty() && stack.getItem() != Items.AIR) {
                builder.addSlot(RecipeIngredientRole.OUTPUT, 101, 43).addItemStack(stack);

            }

        }


    }

    @Override
    public void draw(IonizerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;

        guiGraphics.drawString(font, recipe.getTick() + " ticks", 65, 60, 0xffffff);
        guiGraphics.drawString(font, Component.translatable("jei.dustandash.ionizer_consume_1"), 47, 70, 0xffffff);
        guiGraphics.drawString(font, Component.translatable("jei.dustandash.ionizer_consume_2").append(String.valueOf(recipe.isCostElectrodes())), 47, 80, 0xffffff);
    }

    @Override
    public IDrawable getBackground() {
        return bg;
    }
}
