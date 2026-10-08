package com.tonywww.dustandash;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.tonywww.dustandash.DustAndAsh.MOD_ID;

// The value here should match an entry in the META-INF/mods.toml file

public class DustAndAsh {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "dustandash";


    public static Logger getLogger() {

        return LOGGER;
    }
}
