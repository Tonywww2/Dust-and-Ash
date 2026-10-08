package com.tonywww.dustandash.platform;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
public class PlatformEditBox extends EditBox {
    public PlatformEditBox(Font font, int x, int y, int width, int height, Component title) { super(font, x, y, width, height, title); }
    public void tick() {
        //? if forge {
        super.tick();
        //?}
    }
}
