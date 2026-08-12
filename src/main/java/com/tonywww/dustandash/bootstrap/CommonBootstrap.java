package com.tonywww.dustandash.bootstrap;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.DustAndAshConfig;
import com.tonywww.dustandash.network.PacketHandler;
import com.tonywww.dustandash.network.ImbaModeSyncPacket;
import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAARegistries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

public final class CommonBootstrap {
    private CommonBootstrap() {
    }

    public static void initialize(IEventBus modEventBus) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DustAndAshConfig.COMMON_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DustAndAshConfig.CLIENT_CONFIG);
        DAARegistries.registerAll(modEventBus);
        modEventBus.addListener(CommonBootstrap::onCommonSetup);
        modEventBus.addListener(CommonBootstrap::onConfigReloading);
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PacketHandler::register);
        DustAndAsh.getLogger().info("Dust and Ash >> {}", DAABlocks.INTEGRATED_BLOCK.get());
    }

    private static void onConfigReloading(ModConfigEvent.Reloading event) {
        net.minecraft.server.MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (event.getConfig().getSpec() != DustAndAshConfig.COMMON_CONFIG || server == null) {
            return;
        }

        server.execute(() -> {
            for (net.minecraft.server.level.ServerPlayer player : server.getPlayerList().getPlayers()) {
                ImbaModeSyncPacket.send(player);
            }
        });
    }
}