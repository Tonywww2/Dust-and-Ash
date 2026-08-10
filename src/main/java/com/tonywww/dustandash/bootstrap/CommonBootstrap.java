package com.tonywww.dustandash.bootstrap;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.DustAndAshConfig;
import com.tonywww.dustandash.network.PacketHandler;
import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAARegistries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public final class CommonBootstrap {
    private CommonBootstrap() {
    }

    public static void initialize(IEventBus modEventBus) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DustAndAshConfig.COMMON_CONFIG);
        DAARegistries.registerAll(modEventBus);
        modEventBus.addListener(CommonBootstrap::onCommonSetup);
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(PacketHandler::register);
        DustAndAsh.getLogger().info("Dust and Ash >> {}", DAABlocks.INTEGRATED_BLOCK.get());
    }
}