package com.tonywww.dustandash.cthulhu.config;

import com.tonywww.dustandash.DustAndAshConfig;

public final class CthulhuConfig {

    public static int MAX_DEATHS_BEFORE_BANISH = 3;
    public static String BANISH_ALT_DIM = "minecraft:the_nether";
    public static int BANISH_NETHER_Y = 130;

    public static String PHASE3_FALLBACK_LETTERS = "REALITY";

    public static float VITALITY_DRAIN_PERCENT = 0.02f;
    public static float SOUL_WITHER_PER_MISSING = 0.05f;
    public static float SOUL_WITHER_MAX = 0.95f;
    public static float SOUL_RECOVERY_PER_SLEEP = 0.05f;

    public static int PILLAR_OUTPUT_WINDOW_TICKS = 3;
    public static int PILLAR_INVUL_TICKS = 15;

    public static int GLOBAL_TYPE_COOLDOWN_TICKS = 20;

    public static boolean LOCK_BLOCK_INTERACTIONS_DURING_FIGHT = true;

    private CthulhuConfig() {
    }

    public static void syncFromForgeConfig() {
        MAX_DEATHS_BEFORE_BANISH = DustAndAshConfig.cthulhuMaxDeathsBeforeBanish.get();
        BANISH_ALT_DIM = DustAndAshConfig.cthulhuBanishAltDim.get();
        BANISH_NETHER_Y = DustAndAshConfig.cthulhuBanishNetherY.get();
        PHASE3_FALLBACK_LETTERS = DustAndAshConfig.cthulhuPhase3FallbackLetters.get();
        VITALITY_DRAIN_PERCENT = DustAndAshConfig.cthulhuVitalityDrainPercent.get().floatValue();
        SOUL_WITHER_PER_MISSING = DustAndAshConfig.cthulhuSoulWitherPerMissing.get().floatValue();
        SOUL_WITHER_MAX = DustAndAshConfig.cthulhuSoulWitherMax.get().floatValue();
        SOUL_RECOVERY_PER_SLEEP = DustAndAshConfig.cthulhuSoulRecoveryPerSleep.get().floatValue();
        PILLAR_OUTPUT_WINDOW_TICKS = DustAndAshConfig.cthulhuPillarOutputWindowTicks.get();
        PILLAR_INVUL_TICKS = DustAndAshConfig.cthulhuPillarInvulTicks.get();
        GLOBAL_TYPE_COOLDOWN_TICKS = DustAndAshConfig.cthulhuGlobalTypeCooldownTicks.get();
        LOCK_BLOCK_INTERACTIONS_DURING_FIGHT = DustAndAshConfig.cthulhuLockBlockInteractionsDuringFight.get();
    }
}
