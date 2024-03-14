package com.tonywww.dustandash;

import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.config.DustAndAshConfig;
import com.tonywww.dustandash.menu.ModMenus;
import com.tonywww.dustandash.screen.*;
import com.tonywww.dustandash.block.entity.ModTileEntities;
import com.tonywww.dustandash.data.recipes.ModRecipe;
import com.tonywww.dustandash.item.ModItems;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

import static com.tonywww.dustandash.DustAndAsh.MOD_ID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class DustAndAsh
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "dustandash";

    public DustAndAsh() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        FileUtils.getOrCreateDirectory(FMLPaths.CONFIGDIR.get().resolve(MOD_ID), MOD_ID);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DustAndAshConfig.SPEC, MOD_ID + "/dustandash-common.toml");


        ModItems.register(eventBus);

        ModBlocks.register(eventBus);

        ModTileEntities.register(eventBus);

        ModMenus.register(eventBus);

        ModRecipe.register(eventBus);

        // Register the setup method for modloading
        eventBus.addListener(this::setup);
        // Register the doClientStuff method for modloading
        eventBus.addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client

        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.INTEGRATED_BLOCK_CONTAINER.get(), IntegratedBlockScreen::new);
            MenuScreens.register(ModMenus.ASH_COLLECTOR_CONTAINER.get(), AshCollectorScreen::new);
            MenuScreens.register(ModMenus.MILLING_MACHINE_CONTAINER.get(), MillingMachineScreen::new);
            MenuScreens.register(ModMenus.CENTRIFUGE_CONTAINER.get(), CentrifugeScreen::new);
            MenuScreens.register(ModMenus.IONIZER_CONTAINER.get(), IonizerScreen::new);


        });
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
