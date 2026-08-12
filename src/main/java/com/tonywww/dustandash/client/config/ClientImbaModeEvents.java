package com.tonywww.dustandash.client.config;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.client.cooldown.ClientCurioCooldowns;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DustAndAsh.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ClientImbaModeEvents {
    private ClientImbaModeEvents() {
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientImbaMode.clear();
        ClientCurioCooldowns.clear();
    }
}