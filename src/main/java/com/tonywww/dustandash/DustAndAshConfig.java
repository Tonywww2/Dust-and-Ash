package com.tonywww.dustandash;

import net.minecraftforge.common.ForgeConfigSpec;

public final class DustAndAshConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec COMMON_CONFIG;

    // Machines
    public static final ForgeConfigSpec.ConfigValue<Double> dustSourceChancePerTick;
    public static final ForgeConfigSpec.ConfigValue<Double> dustSourceChancePerBlock;

    public static final ForgeConfigSpec.ConfigValue<Integer> dustSourceHeight;
    public static final ForgeConfigSpec.ConfigValue<Integer> dustSourceRadius;

    public static final ForgeConfigSpec.ConfigValue<Double> ashCollectorChancePerWorkingTick;

    public static final ForgeConfigSpec.ConfigValue<Integer> centrifugeProgressPerTick;

    public static final ForgeConfigSpec.ConfigValue<Double> ionizerProgressPerTick;

    public static final ForgeConfigSpec.ConfigValue<Double> fissionReactorMinEfficiency;
    public static final ForgeConfigSpec.ConfigValue<Double> fissionReactorMaxEfficiency;
    public static final ForgeConfigSpec.ConfigValue<Double> fissionReactorEfficiencyMultiplayer;
    public static final ForgeConfigSpec.ConfigValue<Double> fissionReactorNeutronToEnergyRatio;
    public static final ForgeConfigSpec.ConfigValue<Double> fissionReactorIdealHeatRate;

    // Tools
    public static final ForgeConfigSpec.ConfigValue<Double> handVacuumSuccessRate;
    public static final ForgeConfigSpec.ConfigValue<Double> ironVacuumConsumeRate;

    // Weapons
    public static final ForgeConfigSpec.ConfigValue<Double> galeOtaijutsuDamageRate;

    public static final ForgeConfigSpec.ConfigValue<Integer> lordOfBloodRadius;
    public static final ForgeConfigSpec.ConfigValue<Boolean> lordOfBloodCDCheck;

    public static final ForgeConfigSpec.ConfigValue<Double> whiteLightningExtraDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> whiteLightningExtraPercentage;
    public static final ForgeConfigSpec.ConfigValue<Boolean> whiteLightningCDCheck;

    public static final ForgeConfigSpec.ConfigValue<Double> rottenBladeExtraDamage;
    public static final ForgeConfigSpec.ConfigValue<Integer> rottenBladeRadius;
    public static final ForgeConfigSpec.ConfigValue<Integer> rottenBladeHeight;
    public static final ForgeConfigSpec.ConfigValue<Boolean> rottenBladeCDCheck;

    public static final ForgeConfigSpec.ConfigValue<Integer> rockSolidCD;
    public static final ForgeConfigSpec.ConfigValue<Integer> indestructibleCD;

    // Cthulhu Boss Fight
    public static final ForgeConfigSpec.ConfigValue<Integer> cthulhuMaxDeathsBeforeBanish;
    public static final ForgeConfigSpec.ConfigValue<String> cthulhuBanishAltDim;
    public static final ForgeConfigSpec.ConfigValue<Integer> cthulhuBanishNetherY;
    public static final ForgeConfigSpec.ConfigValue<String> cthulhuPhase3FallbackLetters;
    public static final ForgeConfigSpec.ConfigValue<Double> cthulhuVitalityDrainPercent;
    public static final ForgeConfigSpec.ConfigValue<Double> cthulhuSoulWitherPerMissing;
    public static final ForgeConfigSpec.ConfigValue<Double> cthulhuSoulWitherMax;
    public static final ForgeConfigSpec.ConfigValue<Double> cthulhuSoulRecoveryPerSleep;
    public static final ForgeConfigSpec.ConfigValue<Integer> cthulhuPillarOutputWindowTicks;
    public static final ForgeConfigSpec.ConfigValue<Integer> cthulhuPillarInvulTicks;
    public static final ForgeConfigSpec.ConfigValue<Integer> cthulhuGlobalTypeCooldownTicks;
    public static final ForgeConfigSpec.ConfigValue<Boolean> cthulhuLockBlockInteractionsDuringFight;

    static {

        BUILDER.comment("Config for Dust and Ash").push("Machines");

        // Machines
        dustSourceChancePerTick = BUILDER.comment("\nThe chance of active a dust generating cycle per tick. Range[0, 1] Default: 0.3")
                .defineInRange("dustSourceChancePerTick", 0.4d, 0d, 1d);
        dustSourceChancePerBlock = BUILDER.comment("\nThe chance of generate a dust per block. Range[0, 1] Default: 0.4")
                .defineInRange("dustSourceChancePerBlock", 0.4d, 0d, 1d);

        dustSourceHeight = BUILDER.comment("\nThe distance of checking below the Dust Source. Range[1, 20] Default: 5")
                .defineInRange("dustSourceHeight", 5, 1, 20);
        dustSourceRadius = BUILDER.comment("\nThe radius of checking below the Dust Source. Range[1, 20] Default: 3")
                .defineInRange("dustSourceRadius", 3, 1, 20);

        ashCollectorChancePerWorkingTick = BUILDER.comment("\nThe chance of getting an ash in every working tick. Range(0, 1] Default: 0.004")
                .defineInRange("ashCollectorChancePerWorkingTick", 0.004d, 0d, 1d);

        centrifugeProgressPerTick = BUILDER.comment("\nThe progress added per random tick. Range[0, 100] Default: 4")
                .defineInRange("centrifugeProgressPerTick", 4, 0, 100);

        ionizerProgressPerTick = BUILDER.comment("\nThe progress added per random tick. Range[0, 100] Default: 1")
                .defineInRange("ionizerProgressPerTick", 1d, 0d, 100d);

        // Fission Reactor
        BUILDER.comment("Fission Reactor related configs").push("Fission Reactor");

        fissionReactorMinEfficiency = BUILDER.comment("\nThe Minimum Efficiency of Fission Reactor. Range[0.1, 1000000] Default: 1")
                .defineInRange("fissionReactorMinEfficiency", 1d, 0.1d, 1000000d);

        fissionReactorMaxEfficiency = BUILDER.comment("\nThe Maximum Efficiency of Fission Reactor. Range[0.1, 1000000] Default: 14")
                .defineInRange("fissionReactorMaxEfficiency", 14d, 0.1d, 1000000d);

        fissionReactorEfficiencyMultiplayer = BUILDER.comment("\nThe Efficiency Multiplayer of Fission Reactor. Range[0.1, 1000000] Default: 10")
                .defineInRange("fissionReactorEfficiencyMultiplayer", 10d, 0.1d, 1000000d);

        fissionReactorNeutronToEnergyRatio = BUILDER.comment("\nThe Neutron To EnergyRatio of Fission Reactor. Range[0.1, 1000000] Default: 75")
                .defineInRange("fissionReactorNeutronToEnergyRatio", 75d, 0.1d, 1000000d);

        fissionReactorIdealHeatRate = BUILDER.comment("\nThe Ideal Heat Rate of Fission Reactor. Range[0.1, 2] Default: 0.75")
                .defineInRange("fissionReactorIdealHeatRate", 0.75d, 0.1d, 2d);

        // Tools
        BUILDER.pop(2);
        BUILDER.comment("Tools & Weapons").push("Tools & Weapons");
        handVacuumSuccessRate = BUILDER.comment("\nThe chance of success per use. Range[0, 1] Default: 0.55")
                .defineInRange("handVacuumSuccessRate", 0.55d, 0, 1);

        ironVacuumConsumeRate = BUILDER.comment("\nThe chance of consume a fuel per use. Range[0, 1] Default: 0.35")
                .defineInRange("ironVacuumConsumeRate", 0.35d, 0, 1);

        // Weapons
        galeOtaijutsuDamageRate = BUILDER.comment("\nThe Radius of Lord Of Blood. Range[1, 10] Default: 1.25")
                .defineInRange("galeOtaijutsuDamageRate", 1.25d, 1d, 10d);

        lordOfBloodRadius = BUILDER.comment("\nThe Radius of Lord Of Blood. Range[4, 32] Default: 12")
                .defineInRange("lordOfBloodRadius", 12, 4, 32);
        lordOfBloodCDCheck = BUILDER.comment("\nEnable attack cd check of Lord Of Blood.")
                .define("lordOfBloodCDCheck", true);

        whiteLightningExtraPercentage = BUILDER.comment("\nThe Extra Percentage Damage of 疾風迅雷. Range[0, 10.0] Default: 0.08")
                .defineInRange("whiteLightningExtraPercentage", 0.08d, 0d, 10d);
        whiteLightningExtraDamage = BUILDER.comment("\nThe Extra Damage of 疾風迅雷. Range[0, MAX_FLOAT] Default: 2")
                .defineInRange("whiteLightningExtraDamage", 2.0d, 0d, Float.MAX_VALUE);
        whiteLightningCDCheck = BUILDER.comment("\nEnable attack cd check of 疾風迅雷.")
                .define("whiteLightningCDCheck", true);

        rottenBladeExtraDamage = BUILDER.comment("\nThe Extra Damage of Rotten Blade. Range[0, MAX_FLOAT] Default: 5")
                .defineInRange("rottenBladeExtraDamage", 5.0d, 0d, Float.MAX_VALUE);
        rottenBladeRadius = BUILDER.comment("\nThe Radius of Rotten Blade. Range[0, 32] Default: 9")
                .defineInRange("rottenBladeRadius", 9, 0, 32);
        rottenBladeHeight = BUILDER.comment("\nThe Height of Rotten Blade. Range[0, 32] Default: 5")
                .defineInRange("rottenBladeHeight", 5, 0, 32);
        rottenBladeCDCheck = BUILDER.comment("\nEnable attack cd check of Rotten Blade.")
                .define("rottenBladeCDCheck", true);

        rockSolidCD = BUILDER.comment("\nThe Cool Down of Rock Solid in Tick. Range[0, 10000] Default: 300")
                .defineInRange("rockSolidCD", 300, 0, 10000);
        indestructibleCD = BUILDER.comment("\nThe Cool Down of IndestructibleCD in Tick. Range[0, 10000] Default: 300")
                .defineInRange("indestructibleCD", 300, 0, 10000);



        BUILDER.pop();

        BUILDER.comment("Cthulhu Boss Fight").push("Cthulhu Boss Fight");

        cthulhuMaxDeathsBeforeBanish = BUILDER.comment("\nDeaths before a participant is banished. Range[1, 100] Default: 3")
                .defineInRange("maxDeathsBeforeBanish", 3, 1, 100);
        cthulhuBanishAltDim = BUILDER.comment("\nFallback dimension id used when overworld is the fight dimension. Default: minecraft:the_nether")
                .define("banishAltDim", "minecraft:the_nether");
        cthulhuBanishNetherY = BUILDER.comment("\nNether fallback Y coordinate for banished players. Range[-64, 320] Default: 130")
                .defineInRange("banishNetherY", 130, -64, 320);
        cthulhuPhase3FallbackLetters = BUILDER.comment("\nFallback letters granted when entering phase 3. Default: REALITY")
                .define("phase3FallbackLetters", "REALITY");
        cthulhuVitalityDrainPercent = BUILDER.comment("\nVitality drain percent per erosion pulse. Range[0, 0.95] Default: 0.02")
                .defineInRange("vitalityDrainPercent", 0.02d, 0.0d, 0.95d);
        cthulhuSoulWitherPerMissing = BUILDER.comment("\nSoul wither percent per missing letter sacrifice. Range[0, 0.95] Default: 0.05")
                .defineInRange("soulWitherPerMissing", 0.05d, 0.0d, 0.95d);
        cthulhuSoulWitherMax = BUILDER.comment("\nMaximum soul wither percent. Range[0, 0.95] Default: 0.95")
                .defineInRange("soulWitherMax", 0.95d, 0.0d, 0.95d);
        cthulhuSoulRecoveryPerSleep = BUILDER.comment("\nSoul wither recovery per sleep. Range[0, 0.95] Default: 0.05")
                .defineInRange("soulRecoveryPerSleep", 0.05d, 0.0d, 0.95d);
        cthulhuPillarOutputWindowTicks = BUILDER.comment("\nContinuous pillar damage window in ticks. Range[1, 200] Default: 3")
                .defineInRange("pillarOutputWindowTicks", 3, 1, 200);
        cthulhuPillarInvulTicks = BUILDER.comment("\nPillar custom invulnerability after the output window. Range[0, 200] Default: 15")
                .defineInRange("pillarInvulTicks", 15, 0, 200);
        cthulhuGlobalTypeCooldownTicks = BUILDER.comment("\nGlobal law-word typing cooldown in ticks. Range[0, 200] Default: 20")
                .defineInRange("globalTypeCooldownTicks", 20, 0, 200);
        cthulhuLockBlockInteractionsDuringFight = BUILDER.comment("\nPrevent block breaking and placing while a fight is active in the dimension.")
                .define("lockBlockInteractionsDuringFight", true);

        BUILDER.pop();
        COMMON_CONFIG = BUILDER.build();

    }

}
