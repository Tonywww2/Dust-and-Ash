package com.tonywww.dustandash.screen;

import com.tonywww.dustandash.platform.PlatformIdentifier;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.menu.FissionReactorInterfaceContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import com.tonywww.dustandash.platform.PlatformContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FissionReactorInterfaceScreen extends PlatformContainerScreen<FissionReactorInterfaceContainerMenu> {

    private final ResourceLocation GUI = PlatformIdentifier.id(DustAndAsh.MOD_ID, "textures/gui/fission_reactor_interface_gui.png");

    public FissionReactorInterfaceScreen(FissionReactorInterfaceContainerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackdrop(guiGraphics, pMouseX, pMouseY, pPartialTicks);
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTicks);
        this.renderTooltip(guiGraphics, pMouseX, pMouseY);

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTicks, int pX, int py) {
        if (this.minecraft == null) return;

        ScreenUtils.init(GUI);

        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(GUI, i, j, 0, 0, this.imageWidth, this.imageHeight + 2);

    }
}
