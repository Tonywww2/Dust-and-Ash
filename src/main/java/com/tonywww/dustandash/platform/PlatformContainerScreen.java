package com.tonywww.dustandash.platform;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
public abstract class PlatformContainerScreen<M extends AbstractContainerMenu> extends AbstractContainerScreen<M> {
    protected PlatformContainerScreen(M menu, Inventory inventory, Component title) { super(menu, inventory, title); }
    protected void renderBackdrop(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        //? if forge {
        super.renderBackground(graphics);
        //?} else {
        /*super.renderBackground(graphics, mouseX, mouseY, partialTick);
        *///?}
    }
}
