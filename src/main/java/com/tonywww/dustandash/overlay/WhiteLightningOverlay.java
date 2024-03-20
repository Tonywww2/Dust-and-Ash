package com.tonywww.dustandash.overlay;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.item.ModItems;
import com.tonywww.dustandash.item.custom.WhiteLightning;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class WhiteLightningOverlay implements IGuiOverlay {

    public static final WhiteLightningOverlay INSTANCE = new WhiteLightningOverlay();
    private final Minecraft minecraft = Minecraft.getInstance();
    private static final ResourceLocation HUD = new ResourceLocation(DustAndAsh.MOD_ID, "textures/gui/white_lightning_gui.png");

    private WhiteLightningOverlay() {

    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {

        Player player = minecraft.player;

        if (player.isSpectator()) return;

        if (player.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.WHITE_LIGHTNING.get())) {
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

            int stacks = WhiteLightning.getCharges(stack);
            int advStacks = WhiteLightning.getAdvCharges(stack);

            int amountPerLine = 4;
            int unitSize = 5;
            int xInit = screenWidth / 2 - (unitSize * 2);
            int yInit = screenHeight / 2 + 4;

            int j = 0;
            for (int i = 0; i < stacks + advStacks; i++) {
                int x = xInit + unitSize * (i - j * amountPerLine);
                int y = yInit + (j * unitSize);

                if (i < advStacks) {
                    guiGraphics.blit(HUD, x, y, 4, 0, 4, 4, 8, 4);

                } else {
                    guiGraphics.blit(HUD, x, y, 0, 0, 4, 4, 8, 4);

                }

                if (i % amountPerLine == amountPerLine - 1) {
                    j++;

                }
            }


        }

    }
}
