package com.tonywww.dustandash.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class DustAndAshConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> dustSourceChancePerTick;
    public static final ForgeConfigSpec.ConfigValue<Double> dustSourceChancePerBlock;
    public static final ForgeConfigSpec.ConfigValue<Integer> dustSourceHeight;
    public static final ForgeConfigSpec.ConfigValue<Integer> dustSourceRadius;
    public static final ForgeConfigSpec.ConfigValue<Double> centrifugeProgressPerTick;
    public static final ForgeConfigSpec.ConfigValue<Double> ionizerProgressPerTick;
    public static final ForgeConfigSpec.ConfigValue<Double> handVacuumSuccessRate;
    public static final ForgeConfigSpec.ConfigValue<Double> ironVacuumConsumeRate;

    static {
        BUILDER.push("Config for Dust and Ash");

        dustSourceChancePerTick = BUILDER.comment("\nThe chance of active a dust generating cycle per tick. Range[0, 1] Default: 0.3").defineInRange("dustSourceChancePerTick", 0.4d, 0d, 1d);
        dustSourceChancePerBlock = BUILDER.comment("\nThe chance of generate a dust per block. Range[0, 1] Default: 0.4").defineInRange("dustSourceChancePerBlock", 0.4d, 0d, 1d);

        dustSourceHeight = BUILDER.comment("\nThe distance of checking below the Dust Source. Range[1, 20] Default: 5").defineInRange("dustSourceHeight", 5, 1, 20);
        dustSourceRadius = BUILDER.comment("\nThe radius of checking below the Dust Source. Range[1, 20] Default: 3").defineInRange("dustSourceRadius", 3, 1, 20);

        centrifugeProgressPerTick = BUILDER.comment("\nThe progress added per random tick. Range[0, 100] Default: 1").defineInRange("centrifugeProgressPerTick", 1d, 0d, 100d);
        ionizerProgressPerTick = BUILDER.comment("\nThe progress added per random tick. Range[0, 100] Default: 1").defineInRange("ionizerProgressPerTick", 1d, 0d, 100d);

        handVacuumSuccessRate = BUILDER.comment("\nThe chance of success per use. Range[0, 1] Default: 0.55").defineInRange("handVacuumSuccessRate", 0.55d, 0, 1);
        ironVacuumConsumeRate = BUILDER.comment("\nThe chance of consume a fuel per use. Range[0, 1] Default: 0.35").defineInRange("ironVacuumConsumeRate", 0.35d, 0, 1);

        BUILDER.pop();
        SPEC = BUILDER.build();

    }

}
