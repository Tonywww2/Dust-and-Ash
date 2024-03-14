package com.tonywww.dustandash.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.data.recipes.IonizerRecipe;
import com.tonywww.dustandash.item.ModItems;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraft.world.item.crafting.Ingredient;


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
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends IonizerRecipe> getRecipeClass() {
        return IonizerRecipe.class;
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

    @Override
    public void setIngredients(IonizerRecipe ionizerRecipe, IIngredients iIngredients) {
        NonNullList<Ingredient> in = NonNullList.create();
        for (Ingredient i : ionizerRecipe.getIngredients()) {
            in.add(i);

        }
        // in block
        ItemStack instance1;
        if (ionizerRecipe.getInputBlock() instanceof LiquidBlock) {
            LiquidBlock fluidBlock = (LiquidBlock) ionizerRecipe.getInputBlock();
            instance1 = FluidUtil.getFilledBucket(new FluidStack(fluidBlock.getFluid(), 1000));

        } else {
            instance1 = ionizerRecipe.getInputBlock().asItem().getDefaultInstance();

        }

        if (!instance1.isEmpty() && instance1.getItem() != Items.AIR) {
            in.add(Ingredient.of(instance1));

        }
        iIngredients.setInputIngredients(in);

        NonNullList<ItemStack> out = NonNullList.create();
        for (ItemStack i : ionizerRecipe.getResultItemStacks()) {
            if (!i.isEmpty()) {
                out.add(i);
            }

        }

        // out block
        ItemStack instance2;
        if (ionizerRecipe.getResultBlock() instanceof LiquidBlock) {
            LiquidBlock fluidBlock = (LiquidBlock) ionizerRecipe.getResultBlock();
            instance2 = FluidUtil.getFilledBucket(new FluidStack(fluidBlock.getFluid(), 1000));

        } else {
            instance2 = ionizerRecipe.getResultBlock().asItem().getDefaultInstance();

        }
        if (!instance2.isEmpty() && instance2.getItem() != Items.AIR) {
            out.add(instance2);

        }
        iIngredients.setOutputs(VanillaTypes.ITEM, out);

    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, IonizerRecipe recipe, IIngredients ingredients) {

        IGuiItemStackGroup itemStacks = iRecipeLayout.getItemStacks();
//        IGuiFluidStackGroup fluidStacks = iRecipeLayout.getFluidStacks();

        itemStacks.init(1, true, 15, 49);
        itemStacks.init(2, true, 5, 69);
        itemStacks.init(3, true, 25, 69);
        itemStacks.init(4, true, 61, 4);
        itemStacks.init(5, true, 97, 4);
        itemStacks.set(ingredients);

        itemStacks.init(0, true, 15, 29);
        if (recipe.getPowerCost() > 0) {
            ItemStack stack = ModItems.ELECTRON.get().getDefaultInstance();
            stack.setCount(recipe.getPowerCost());
            itemStacks.set(0, stack);

        }

        // in block
        itemStacks.init(6, true, 59, 43);
        if (recipe.getInputBlock() instanceof LiquidBlock) {
//            fluidStacks.init(0, true, 59, 43);
            LiquidBlock fluidBlock = (LiquidBlock) recipe.getInputBlock();
//            fluidStacks.set(0, new FluidStack(fluidBlock.getFluid(), 1000));
            itemStacks.set(6, FluidUtil.getFilledBucket(new FluidStack(fluidBlock.getFluid(), 1000)));

        } else {
//            itemStacks.init(6, true, 58, 42);
            ItemStack stack = recipe.getInputBlock().asItem().getDefaultInstance();
            if (!stack.isEmpty() && stack.getItem() != Items.AIR) {
                itemStacks.set(6, stack);

            }

        }


        itemStacks.init(7, false, 134, 48);
        itemStacks.init(8, false, 154, 48);
        itemStacks.init(9, false, 134, 68);
        itemStacks.init(10, false, 154, 68);

        for (int i = 7; i <= 10; i++) {
            if (recipe.getResultItemStacks().get(i - 7) != ItemStack.EMPTY) {
                itemStacks.set(i, recipe.getResultItemStacks().get(i - 7));

            }

        }

        // out block
        itemStacks.init(11, true, 100, 42);
        if (recipe.getResultBlock() instanceof LiquidBlock) {
//            fluidStacks.init(1, true, 101, 43);
            LiquidBlock fluidBlock = (LiquidBlock) recipe.getResultBlock();
            itemStacks.set(11, FluidUtil.getFilledBucket(new FluidStack(fluidBlock.getFluid(), 1000)));

        } else {
//            itemStacks.init(11, true, 100, 42);
            ItemStack stack = recipe.getResultBlock().asItem().getDefaultInstance();
            if (!stack.isEmpty() && stack.getItem() != Items.AIR) {
                itemStacks.set(11, stack);

            }

        }


    }

    @Override
    public void draw(IonizerRecipe recipe, PoseStack matrixStack, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;

        font.draw(matrixStack, recipe.getTick() + " ticks", 65, 60, 0x555555);
        font.draw(matrixStack, "Consume", 47, 70, 0x555555);
        font.draw(matrixStack, "Electrodes: " + recipe.isCostElectrodes(), 47, 80, 0x555555);
    }
}
