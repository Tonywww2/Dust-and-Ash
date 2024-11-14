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

    public static final ForgeConfigSpec.ConfigValue<Double> whiteLightningExtraDamage;
    public static final ForgeConfigSpec.ConfigValue<Double> whiteLightningExtraPercentage;

    public static final ForgeConfigSpec.ConfigValue<Double> rottenBladeExtraDamage;
    public static final ForgeConfigSpec.ConfigValue<Integer> rottenBladeRadius;
    public static final ForgeConfigSpec.ConfigValue<Integer> rottenBladeHeight;

    public static final ForgeConfigSpec.ConfigValue<Integer> rockSolidCD;
    public static final ForgeConfigSpec.ConfigValue<Integer> indestructibleCD;

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

        whiteLightningExtraPercentage = BUILDER.comment("\nThe Extra Percentage Damage of 疾風迅雷. Range[0, 10.0] Default: 0.08")
                .defineInRange("whiteLightningExtraPercentage", 0.08d, 0d, 10d);
        whiteLightningExtraDamage = BUILDER.comment("\nThe Extra Damage of 疾風迅雷. Range[0, MAX_FLOAT] Default: 2")
                .defineInRange("whiteLightningExtraDamage", 2.0d, 0d, Float.MAX_VALUE);

        rottenBladeExtraDamage = BUILDER.comment("\nThe Extra Damage of Rotten Blade. Range[0, MAX_FLOAT] Default: 5")
                .defineInRange("rottenBladeExtraDamage", 5.0d, 0d, Float.MAX_VALUE);
        rottenBladeRadius = BUILDER.comment("\nThe Radius of Rotten Blade. Range[0, 32] Default: 9")
                .defineInRange("rottenBladeRadius", 9, 0, 32);
        rottenBladeHeight = BUILDER.comment("\nThe Height of Rotten Blade. Range[0, 32] Default: 5")
                .defineInRange("rottenBladeHeight", 5, 0, 32);

        rockSolidCD = BUILDER.comment("\nThe Cool Down of Rock Solid in Tick. Range[0, 10000] Default: 300")
                .defineInRange("rockSolidCD", 300, 0, 10000);
        indestructibleCD = BUILDER.comment("\nThe Cool Down of IndestructibleCD in Tick. Range[0, 10000] Default: 300")
                .defineInRange("indestructibleCD", 300, 0, 10000);



        BUILDER.pop();
        COMMON_CONFIG = BUILDER.build();

    }

}
