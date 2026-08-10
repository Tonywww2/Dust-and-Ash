package com.tonywww.dustandash;

import com.tonywww.dustandash.bootstrap.CommonBootstrap;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
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
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        CommonBootstrap.initialize(modEventBus);
    }

    public static Logger getLogger() {

        return LOGGER;
    }
}
