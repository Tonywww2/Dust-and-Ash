package com.tonywww.dustandash;

import net.minecraftforge.common.ForgeConfigSpec;

public final class DustAndAshConfig {
    public static final MachineSettings MACHINES;
    public static final ReactorSettings REACTOR;
    public static final ToolSettings TOOLS;
    public static final WeaponSettings WEAPONS;
    public static final ForgeConfigSpec COMMON_CONFIG;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Config for Dust and Ash").push("Machines");
        MACHINES = new MachineSettings(builder);

        builder.comment("Fission Reactor related configs").push("Fission Reactor");
        REACTOR = new ReactorSettings(builder);

        builder.pop(2);
        builder.comment("Tools & Weapons").push("Tools & Weapons");
        TOOLS = new ToolSettings(builder);
        WEAPONS = new WeaponSettings(builder);
        builder.pop();

        COMMON_CONFIG = builder.build();
    }

    private DustAndAshConfig() {
    }

    public static final class MachineSettings {
        public final ForgeConfigSpec.DoubleValue dustSourceChancePerTick;
        public final ForgeConfigSpec.DoubleValue dustSourceChancePerBlock;
        public final ForgeConfigSpec.IntValue dustSourceHeight;
        public final ForgeConfigSpec.IntValue dustSourceRadius;
        public final ForgeConfigSpec.DoubleValue ashCollectorChancePerWorkingTick;
        public final ForgeConfigSpec.IntValue centrifugeProgressPerTick;
        public final ForgeConfigSpec.DoubleValue ionizerProgressPerTick;

        private MachineSettings(ForgeConfigSpec.Builder builder) {
            this.dustSourceChancePerTick = builder
                    .comment("\nThe chance of active a dust generating cycle per tick. Range[0, 1] Default: 0.3")
                    .defineInRange("dustSourceChancePerTick", 0.4d, 0d, 1d);
            this.dustSourceChancePerBlock = builder
                    .comment("\nThe chance of generate a dust per block. Range[0, 1] Default: 0.4")
                    .defineInRange("dustSourceChancePerBlock", 0.4d, 0d, 1d);
            this.dustSourceHeight = builder
                    .comment("\nThe distance of checking below the Dust Source. Range[1, 20] Default: 5")
                    .defineInRange("dustSourceHeight", 5, 1, 20);
            this.dustSourceRadius = builder
                    .comment("\nThe radius of checking below the Dust Source. Range[1, 20] Default: 3")
                    .defineInRange("dustSourceRadius", 3, 1, 20);
            this.ashCollectorChancePerWorkingTick = builder
                    .comment("\nThe chance of getting an ash in every working tick. Range(0, 1] Default: 0.004")
                    .defineInRange("ashCollectorChancePerWorkingTick", 0.004d, 0d, 1d);
            this.centrifugeProgressPerTick = builder
                    .comment("\nThe progress added per random tick. Range[0, 100] Default: 4")
                    .defineInRange("centrifugeProgressPerTick", 4, 0, 100);
            this.ionizerProgressPerTick = builder
                    .comment("\nThe progress added per random tick. Range[0, 100] Default: 1")
                    .defineInRange("ionizerProgressPerTick", 1d, 0d, 100d);
        }
    }

    public static final class ReactorSettings {
        public final ForgeConfigSpec.DoubleValue minimumEfficiency;
        public final ForgeConfigSpec.DoubleValue maximumEfficiency;
        public final ForgeConfigSpec.DoubleValue efficiencyMultiplier;
        public final ForgeConfigSpec.DoubleValue neutronToEnergyRatio;
        public final ForgeConfigSpec.DoubleValue idealHeatRate;

        private ReactorSettings(ForgeConfigSpec.Builder builder) {
            this.minimumEfficiency = builder
                    .comment("\nThe Minimum Efficiency of Fission Reactor. Range[0.1, 1000000] Default: 1")
                    .defineInRange("fissionReactorMinEfficiency", 1d, 0.1d, 1000000d);
            this.maximumEfficiency = builder
                    .comment("\nThe Maximum Efficiency of Fission Reactor. Range[0.1, 1000000] Default: 14")
                    .defineInRange("fissionReactorMaxEfficiency", 14d, 0.1d, 1000000d);
            this.efficiencyMultiplier = builder
                    .comment("\nThe Efficiency Multiplayer of Fission Reactor. Range[0.1, 1000000] Default: 10")
                    .defineInRange("fissionReactorEfficiencyMultiplayer", 10d, 0.1d, 1000000d);
            this.neutronToEnergyRatio = builder
                    .comment("\nThe Neutron To EnergyRatio of Fission Reactor. Range[0.1, 1000000] Default: 75")
                    .defineInRange("fissionReactorNeutronToEnergyRatio", 75d, 0.1d, 1000000d);
            this.idealHeatRate = builder
                    .comment("\nThe Ideal Heat Rate of Fission Reactor. Range[0.1, 2] Default: 0.75")
                    .defineInRange("fissionReactorIdealHeatRate", 0.75d, 0.1d, 2d);
        }
    }

    public static final class ToolSettings {
        public final ForgeConfigSpec.DoubleValue handVacuumSuccessRate;
        public final ForgeConfigSpec.DoubleValue ironVacuumConsumeRate;
        public final ForgeConfigSpec.IntValue rockSolidCooldown;
        public final ForgeConfigSpec.IntValue indestructibleCooldown;

        private ToolSettings(ForgeConfigSpec.Builder builder) {
            this.handVacuumSuccessRate = builder
                    .comment("\nThe chance of success per use. Range[0, 1] Default: 0.55")
                    .defineInRange("handVacuumSuccessRate", 0.55d, 0d, 1d);
            this.ironVacuumConsumeRate = builder
                    .comment("\nThe chance of consume a fuel per use. Range[0, 1] Default: 0.35")
                    .defineInRange("ironVacuumConsumeRate", 0.35d, 0d, 1d);
            this.rockSolidCooldown = builder
                    .comment("\nThe Cool Down of Rock Solid in Tick. Range[0, 10000] Default: 300")
                    .defineInRange("rockSolidCD", 300, 0, 10000);
            this.indestructibleCooldown = builder
                    .comment("\nThe Cool Down of IndestructibleCD in Tick. Range[0, 10000] Default: 300")
                    .defineInRange("indestructibleCD", 300, 0, 10000);
        }
    }

    public static final class WeaponSettings {
        public final ForgeConfigSpec.DoubleValue galeOtaijutsuDamageRate;
        public final ForgeConfigSpec.IntValue lordOfBloodRadius;
        public final ForgeConfigSpec.BooleanValue lordOfBloodCooldownCheck;
        public final ForgeConfigSpec.DoubleValue whiteLightningExtraDamage;
        public final ForgeConfigSpec.DoubleValue whiteLightningExtraPercentage;
        public final ForgeConfigSpec.BooleanValue whiteLightningCooldownCheck;
        public final ForgeConfigSpec.DoubleValue rottenBladeExtraDamage;
        public final ForgeConfigSpec.IntValue rottenBladeRadius;
        public final ForgeConfigSpec.IntValue rottenBladeHeight;
        public final ForgeConfigSpec.BooleanValue rottenBladeCooldownCheck;

        private WeaponSettings(ForgeConfigSpec.Builder builder) {
            this.galeOtaijutsuDamageRate = builder
                    .comment("\nThe Radius of Lord Of Blood. Range[1, 10] Default: 1.25")
                    .defineInRange("galeOtaijutsuDamageRate", 1.25d, 1d, 10d);
            this.lordOfBloodRadius = builder
                    .comment("\nThe Radius of Lord Of Blood. Range[4, 32] Default: 12")
                    .defineInRange("lordOfBloodRadius", 12, 4, 32);
            this.lordOfBloodCooldownCheck = builder
                    .comment("\nEnable attack cd check of Lord Of Blood.")
                    .define("lordOfBloodCDCheck", true);
            this.whiteLightningExtraPercentage = builder
                    .comment("\nThe Extra Percentage Damage of 疾風迅雷. Range[0, 10.0] Default: 0.08")
                    .defineInRange("whiteLightningExtraPercentage", 0.08d, 0d, 10d);
            this.whiteLightningExtraDamage = builder
                    .comment("\nThe Extra Damage of 疾風迅雷. Range[0, MAX_FLOAT] Default: 2")
                    .defineInRange("whiteLightningExtraDamage", 2.0d, 0d, Float.MAX_VALUE);
            this.whiteLightningCooldownCheck = builder
                    .comment("\nEnable attack cd check of 疾風迅雷.")
                    .define("whiteLightningCDCheck", true);
            this.rottenBladeExtraDamage = builder
                    .comment("\nThe Extra Damage of Rotten Blade. Range[0, MAX_FLOAT] Default: 5")
                    .defineInRange("rottenBladeExtraDamage", 5.0d, 0d, Float.MAX_VALUE);
            this.rottenBladeRadius = builder
                    .comment("\nThe Radius of Rotten Blade. Range[0, 32] Default: 9")
                    .defineInRange("rottenBladeRadius", 9, 0, 32);
            this.rottenBladeHeight = builder
                    .comment("\nThe Height of Rotten Blade. Range[0, 32] Default: 5")
                    .defineInRange("rottenBladeHeight", 5, 0, 32);
            this.rottenBladeCooldownCheck = builder
                    .comment("\nEnable attack cd check of Rotten Blade.")
                    .define("rottenBladeCDCheck", true);
        }
    }
}