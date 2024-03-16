package com.tonywww.dustandash.integration.jei;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.data.recipes.IonizerRecipe;
import com.tonywww.dustandash.integration.DustAndAshRecipeTypes;
import com.tonywww.dustandash.item.ModItems;
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

import java.util.Arrays;


public class IonizerCategory implements IRecipeCategory<IonizerRecipe> {

    public final static ResourceLocation UID = new ResourceLocation(DustAndAsh.MOD_ID, "ionizer");
    public final static ResourceLocation TEXTURE = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/ionizer_gui.png");

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
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

//    @Override
//    public void setIngredients(IonizerRecipe ionizerRecipe, IIngredients iIngredients) {
//        NonNullList<Ingredient> in = NonNullList.create();
//        for (Ingredient i : ionizerRecipe.getIngredients()) {
//            in.add(i);
//
//        }
//        // in block
//        ItemStack instance1;
//        if (ionizerRecipe.getInputBlock() instanceof LiquidBlock) {
//            LiquidBlock fluidBlock = (LiquidBlock) ionizerRecipe.getInputBlock();
//            instance1 = FluidUtil.getFilledBucket(new FluidStack(fluidBlock.getFluid(), 1000));
//
//        } else {
//            instance1 = ionizerRecipe.getInputBlock().asItem().getDefaultInstance();
//
//        }
//
//        if (!instance1.isEmpty() && instance1.getItem() != Items.AIR) {
//            in.add(Ingredient.of(instance1));
//
//        }
//        iIngredients.setInputIngredients(in);
//
//        NonNullList<ItemStack> out = NonNullList.create();
//        for (ItemStack i : ionizerRecipe.getResultItemStacks()) {
//            if (!i.isEmpty()) {
//                out.add(i);
//            }
//
//        }
//
//        // out block
//        ItemStack instance2;
//        if (ionizerRecipe.getResultBlock() instanceof LiquidBlock) {
//            LiquidBlock fluidBlock = (LiquidBlock) ionizerRecipe.getResultBlock();
//            instance2 = FluidUtil.getFilledBucket(new FluidStack(fluidBlock.getFluid(), 1000));
//
//        } else {
//            instance2 = ionizerRecipe.getResultBlock().asItem().getDefaultInstance();
//
//        }
//        if (!instance2.isEmpty() && instance2.getItem() != Items.AIR) {
//            out.add(instance2);
//
//        }
//        iIngredients.setOutputs(VanillaTypes.ITEM, out);
//
//    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IonizerRecipe recipe, IFocusGroup focuses) {

//        IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();
//        IGuiFluidStackGroup fluidStacks = iRecipeLayout.getFluidStacks();

        builder.addSlot(RecipeIngredientRole.INPUT, 16, 50).addItemStacks(Arrays.asList(recipe.getIngredients().get(0).getItems()));
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 70).addItemStacks(Arrays.asList(recipe.getIngredients().get(1).getItems()));
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 70).addItemStacks(Arrays.asList(recipe.getIngredients().get(2).getItems()));
        builder.addSlot(RecipeIngredientRole.INPUT, 62, 5).addItemStacks(Arrays.asList(recipe.getIngredients().get(3).getItems()));
        builder.addSlot(RecipeIngredientRole.INPUT, 98, 5).addItemStacks(Arrays.asList(recipe.getIngredients().get(4).getItems()));

        if (recipe.getPowerCost() > 0) {
            ItemStack stack = ModItems.ELECTRON.get().getDefaultInstance();
            stack.setCount(recipe.getPowerCost());
            builder.addSlot(RecipeIngredientRole.INPUT, 16, 30).addItemStack(stack);

        }

        // in block
        if (recipe.getInputBlock() instanceof LiquidBlock) {
            LiquidBlock fluidBlock = (LiquidBlock) recipe.getInputBlock();
            builder.addSlot(RecipeIngredientRole.INPUT, 60, 44).addFluidStack(fluidBlock.getFluid(), 1000);

        } else {
            ItemStack stack = recipe.getInputBlock().asItem().getDefaultInstance();
            if (!stack.isEmpty() && stack.getItem() != Items.AIR) {
                builder.addSlot(RecipeIngredientRole.INPUT, 60, 44).addItemStack(stack);

            }

        }

        builder.addSlot(RecipeIngredientRole.INPUT, 135, 49).addItemStack(recipe.getResultItemStacks().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 155, 49).addItemStack(recipe.getResultItemStacks().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 135, 69).addItemStack(recipe.getResultItemStacks().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 155, 69).addItemStack(recipe.getResultItemStacks().get(3));

        // out block
        if (recipe.getResultBlock() instanceof LiquidBlock) {
            LiquidBlock fluidBlock = (LiquidBlock) recipe.getResultBlock();
            builder.addSlot(RecipeIngredientRole.INPUT, 101, 43).addFluidStack(fluidBlock.getFluid(), 1000);

        } else {
            ItemStack stack = recipe.getResultBlock().asItem().getDefaultInstance();
            if (!stack.isEmpty() && stack.getItem() != Items.AIR) {
                builder.addSlot(RecipeIngredientRole.INPUT, 101, 43).addItemStack(stack);

            }

        }


    }

    @Override
    public void draw(IonizerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;

        guiGraphics.drawString(font, recipe.getTick() + " ticks", 65, 60, 0xffffff);
        guiGraphics.drawString(font, "Consume", 47, 70, 0xffffff);
        guiGraphics.drawString(font, "Electrodes: " + recipe.isCostElectrodes(), 47, 80, 0xffffff);
    }
}
