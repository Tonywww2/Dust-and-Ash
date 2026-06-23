package com.tonywww.dustandash;

import com.tonywww.dustandash.registeries.ModBlocks;
import com.tonywww.dustandash.registeries.ModEntites;
import com.tonywww.dustandash.registeries.ModCreativeModTabs;
import com.tonywww.dustandash.registeries.ModContainerMenus;
import com.tonywww.dustandash.registeries.ModBlockEntities;
import com.tonywww.dustandash.registeries.ModRecipe;
import com.tonywww.dustandash.registeries.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.tonywww.dustandash.DustAndAsh.MOD_ID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MOD_ID)
public class DustAndAsh {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "dustandash";

    @SuppressWarnings("removal")
    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(DustAndAsh.MOD_ID, path);
    }

    @SuppressWarnings("removal")
    public DustAndAsh() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DustAndAshConfig.COMMON_CONFIG);

        ModCreativeModTabs.register(eventBus);

        ModItems.register(eventBus);

        ModBlocks.register(eventBus);

        ModBlockEntities.register(eventBus);

        ModEntites.register(eventBus);

        ModContainerMenus.register(eventBus);

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
