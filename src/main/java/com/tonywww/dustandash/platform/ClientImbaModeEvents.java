package com.tonywww.dustandash.platform;

import com.tonywww.dustandash.client.config.ClientImbaMode;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.client.cooldown.ClientCurioCooldowns;
//? if forge {
import net.minecraftforge.api.distmarker.Dist;
//?} else {
/*import net.neoforged.api.distmarker.Dist;
*///?}

//? if forge {
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
//?} else {
/*import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
*///?}

//? if forge {
import net.minecraftforge.eventbus.api.SubscribeEvent;
//?} else {
/*import net.neoforged.bus.api.SubscribeEvent;
*///?}

//? if forge {
import net.minecraftforge.fml.common.Mod;
//?} else {
/*import net.neoforged.fml.common.Mod;
*///?}


//? if forge {
@Mod.EventBusSubscriber(modid = DustAndAsh.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
//?} else {
/*@net.neoforged.fml.common.EventBusSubscriber(modid = DustAndAsh.MOD_ID, value = Dist.CLIENT, bus = net.neoforged.fml.common.EventBusSubscriber.Bus.GAME)
*///?}

public final class ClientImbaModeEvents {
    private ClientImbaModeEvents() {
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientImbaMode.clear();
        ClientCurioCooldowns.clear();
    }
}
