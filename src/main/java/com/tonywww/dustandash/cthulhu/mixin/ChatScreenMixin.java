package com.tonywww.dustandash.cthulhu.mixin;

import com.tonywww.dustandash.cthulhu.client.ClientGraphemeData;
import com.tonywww.dustandash.cthulhu.client.CthulhuChatFormatter;
import com.tonywww.dustandash.cthulhu.network.WordSpellPacket;
import com.tonywww.dustandash.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {

    @Shadow
    protected EditBox input;

    protected ChatScreenMixin(net.minecraft.network.chat.Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void dustandash$installCthulhuFormatter(CallbackInfo ci) {
        this.input.setFormatter(CthulhuChatFormatter::format);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void dustandash$handleCthulhuTyping(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!isCthulhuTypingActive()) {
            return;
        }

        if (keyCode == 258 || keyCode == 265) {
            cir.setReturnValue(true);
            return;
        }

        if (keyCode == 257 || keyCode == 335) {
            String value = input.getValue().trim();
            if (!value.isEmpty() && !value.startsWith("/")) {
                PacketHandler.sendToServer(new WordSpellPacket(value));
                Minecraft.getInstance().setScreen(null);
                cir.setReturnValue(true);
            }
        }
    }

    private static boolean isCthulhuTypingActive() {
        return !ClientGraphemeData.isEmpty();
    }
}
