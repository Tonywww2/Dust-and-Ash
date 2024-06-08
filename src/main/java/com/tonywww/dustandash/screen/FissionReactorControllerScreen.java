package com.tonywww.dustandash.screen;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.menu.FissionReactorControllerContainerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;

public class FissionReactorControllerScreen extends AbstractContainerScreen<FissionReactorControllerContainerMenu> {

    private final ResourceLocation GUI = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/fission_reactor_controller_gui.png");

    public FissionReactorControllerScreen(FissionReactorControllerContainerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTicks);
        this.renderTooltip(guiGraphics, pMouseX, pMouseY);

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTicks, int pX, int pY) {
        if (this.minecraft == null) return;

        ScreenUtils.init(GUI);

        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(GUI, i, j, 0, 0, 196, 192);

    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int pX, int pY) {

        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_heat"),
                5, 5, 4210752, false);
        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_fuel"),
                5, 17, 4210752, false);
        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_energy"),
                5, 29, 4210752, false);

        ContainerData data = this.menu.getData();

        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_efficiency"),
                5, 41, 4210752, false);

        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_fuel_cell_count"),
                5, 53, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal(String.valueOf(data.get(6))),
                68, 53, 4210752, false);

        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_cooling_cell_count"),
                5, 65, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal(String.valueOf(data.get(7))),
                68, 65, 4210752, false);

        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_neutron"),
                5, 77, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal(String.valueOf(data.get(5))),
                68, 77, 4210752, false);

        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_radius"),
                101, 41, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal(String.valueOf(data.get(3))),
                164, 41, 4210752, false);

        guiGraphics.drawString(this.font, Component.translatable("screen.dustandash.fission_reactor_controller_height"),
                101, 53, 4210752, false);
        guiGraphics.drawString(this.font, Component.literal(String.valueOf(data.get(4))),
                164, 53, 4210752, false);

    }
}
