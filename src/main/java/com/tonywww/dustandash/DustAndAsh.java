package com.tonywww.dustandash;

import com.tonywww.dustandash.block.ModBlocks;
import com.tonywww.dustandash.config.DustAndAshConfig;
import com.tonywww.dustandash.entity.ModEntites;
import com.tonywww.dustandash.item.ModCreativeModTabs;
import com.tonywww.dustandash.menu.ModMenus;
import com.tonywww.dustandash.screen.*;
import com.tonywww.dustandash.block.entity.ModTileEntities;
import com.tonywww.dustandash.data.recipes.ModRecipe;
import com.tonywww.dustandash.item.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.tonywww.dustandash.DustAndAsh.MOD_ID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class DustAndAsh {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "dustandash";

    public DustAndAsh() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DustAndAshConfig.COMMON_CONFIG);

        ModCreativeModTabs.register(eventBus);

        ModItems.register(eventBus);

        ModBlocks.register(eventBus);

        ModTileEntities.register(eventBus);

        ModEntites.register(eventBus);

        ModMenus.register(eventBus);

        ModRecipe.register(eventBus);

        // Register the setup method for modloading
        eventBus.addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Dust and Ash >> {}", ModBlocks.INTEGRATED_BLOCK.get());

    }

    public static Logger getLogger() {

        return LOGGER;
    }
}
