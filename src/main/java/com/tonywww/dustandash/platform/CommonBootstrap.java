package com.tonywww.dustandash.platform;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.platform.DustAndAshConfig;
import com.tonywww.dustandash.platform.PacketHandler;
import com.tonywww.dustandash.platform.ImbaModeSyncPacket;
import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAARegistries;
//? if forge {
import net.minecraftforge.eventbus.api.IEventBus;
//?} else {
/*import net.neoforged.bus.api.IEventBus;
*///?}

//? if forge {
import net.minecraftforge.fml.ModLoadingContext;
//?} else {
/*import net.neoforged.fml.ModLoadingContext;
*///?}

//? if forge {
import net.minecraftforge.fml.config.ModConfig;
//?} else {
/*import net.neoforged.fml.config.ModConfig;
*///?}

//? if forge {
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
//?} else {
/*import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
*///?}

//? if forge {
import net.minecraftforge.fml.event.config.ModConfigEvent;
//?} else {
/*import net.neoforged.fml.event.config.ModConfigEvent;
*///?}

//? if forge {
import net.minecraftforge.server.ServerLifecycleHooks;
//?} else {
/*import net.neoforged.neoforge.server.ServerLifecycleHooks;
*///?}


public final class CommonBootstrap {
    private CommonBootstrap() {
    }

    public static void initialize(PlatformEventBus bus) {
//? if forge {
ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, (net.minecraftforge.common.ForgeConfigSpec) DustAndAshConfig.COMMON_CONFIG.nativeSpec());
ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, (net.minecraftforge.common.ForgeConfigSpec) DustAndAshConfig.CLIENT_CONFIG.nativeSpec());
//?} else {
/*PlatformMod.container.registerConfig(ModConfig.Type.COMMON, (net.neoforged.neoforge.common.ModConfigSpec) DustAndAshConfig.COMMON_CONFIG.nativeSpec());
PlatformMod.container.registerConfig(ModConfig.Type.CLIENT, (net.neoforged.neoforge.common.ModConfigSpec) DustAndAshConfig.CLIENT_CONFIG.nativeSpec());
*///?}

        DAARegistries.registerAll(bus);
        bus.nativeBus().addListener(CommonBootstrap::onCommonSetup);
        bus.nativeBus().addListener(CommonBootstrap::onConfigReloading);
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PacketHandler::register);
        DustAndAsh.getLogger().info("Dust and Ash >> {}", DAABlocks.INTEGRATED_BLOCK.get());
    }

    private static void onConfigReloading(ModConfigEvent.Reloading event) {
        net.minecraft.server.MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (event.getConfig().getSpec() != DustAndAshConfig.COMMON_CONFIG.nativeSpec() || server == null) {
            return;
        }

        server.execute(() -> {
            for (net.minecraft.server.level.ServerPlayer player : server.getPlayerList().getPlayers()) {
                ImbaModeSyncPacket.send(player);
            }
        });
    }
}
