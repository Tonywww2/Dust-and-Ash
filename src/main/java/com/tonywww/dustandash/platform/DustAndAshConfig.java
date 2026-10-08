package com.tonywww.dustandash.platform;

//? if forge {
//?} else {
/*import net.neoforged.neoforge.common.ModConfigSpec;
*///?}


public final class DustAndAshConfig {
    public static final MachineSettings MACHINES;
    public static final ReactorSettings REACTOR;
    public static final ToolSettings TOOLS;
    public static final WeaponSettings WEAPONS;
        public static final CurioSettings CURIOS;
        public static final PlatformConfigSpec.BooleanValue IMBA_MODE;
        public static final ClientSettings CLIENT;
    public static final PlatformConfigSpec COMMON_CONFIG;
        public static final PlatformConfigSpec CLIENT_CONFIG;

    static {
        PlatformConfigSpec.Builder builder = new PlatformConfigSpec.Builder();
        builder.comment("Config for Dust and Ash").push("Machines");
        MACHINES = new MachineSettings(builder);

        builder.comment("Fission Reactor related configs").push("Fission Reactor");
        REACTOR = new ReactorSettings(builder);

        builder.pop(2);
        builder.comment("Tools & Weapons").push("Tools & Weapons");
        TOOLS = new ToolSettings(builder);
        WEAPONS = new WeaponSettings(builder);
        builder.pop();

        builder.comment("Curios accessories").push("Curios");
        CURIOS = new CurioSettings(builder);
        builder.pop();

        IMBA_MODE = builder
                .comment("Greatly enhance the special effects of combat equipment. Default: false")
                .define("imbaMode", false);

        COMMON_CONFIG = builder.build();

        PlatformConfigSpec.Builder clientBuilder = new PlatformConfigSpec.Builder();
        clientBuilder.comment("Client display settings").push("Cooldown Overlay");
        CLIENT = new ClientSettings(clientBuilder);
        clientBuilder.pop();
        CLIENT_CONFIG = clientBuilder.build();
    }

    private DustAndAshConfig() {
    }

    public static final class ClientSettings {
        public final PlatformConfigSpec.IntValue cooldownOverlayX;
        public final PlatformConfigSpec.IntValue cooldownOverlayY;

        private ClientSettings(PlatformConfigSpec.Builder builder) {
            this.cooldownOverlayX = builder
                    .comment("\nCooldown overlay X position in GUI pixels from the left edge. Default: 8")
                    .defineInRange("cooldownOverlayX", 8, 0, 10000);
            this.cooldownOverlayY = builder
                    .comment("\nCooldown overlay Y position in GUI pixels from the top edge. Default: 8")
                    .defineInRange("cooldownOverlayY", 8, 0, 10000);
        }
    }

    public static final class MachineSettings {
        public final PlatformConfigSpec.DoubleValue dustSourceChancePerTick;
        public final PlatformConfigSpec.DoubleValue dustSourceChancePerBlock;
        public final PlatformConfigSpec.IntValue dustSourceHeight;
        public final PlatformConfigSpec.IntValue dustSourceRadius;
        public final PlatformConfigSpec.DoubleValue ashCollectorChancePerWorkingTick;
        public final PlatformConfigSpec.IntValue centrifugeProgressPerTick;
        public final PlatformConfigSpec.DoubleValue ionizerProgressPerTick;

        private MachineSettings(PlatformConfigSpec.Builder builder) {
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
        public final PlatformConfigSpec.DoubleValue minimumEfficiency;
        public final PlatformConfigSpec.DoubleValue maximumEfficiency;
        public final PlatformConfigSpec.DoubleValue efficiencyMultiplier;
        public final PlatformConfigSpec.DoubleValue neutronToEnergyRatio;
        public final PlatformConfigSpec.DoubleValue idealHeatRate;

        private ReactorSettings(PlatformConfigSpec.Builder builder) {
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
        public final PlatformConfigSpec.DoubleValue handVacuumSuccessRate;
        public final PlatformConfigSpec.DoubleValue ironVacuumConsumeRate;
        public final PlatformConfigSpec.IntValue rockSolidCooldown;
        public final PlatformConfigSpec.IntValue indestructibleCooldown;

        private ToolSettings(PlatformConfigSpec.Builder builder) {
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
        public final PlatformConfigSpec.DoubleValue galeOtaijutsuDamageRate;
        public final PlatformConfigSpec.IntValue lordOfBloodRadius;
        public final PlatformConfigSpec.BooleanValue lordOfBloodCooldownCheck;
        public final PlatformConfigSpec.DoubleValue whiteLightningExtraDamage;
        public final PlatformConfigSpec.DoubleValue whiteLightningExtraPercentage;
        public final PlatformConfigSpec.BooleanValue whiteLightningCooldownCheck;
        public final PlatformConfigSpec.DoubleValue rottenBladeExtraDamage;
        public final PlatformConfigSpec.IntValue rottenBladeRadius;
        public final PlatformConfigSpec.IntValue rottenBladeHeight;
        public final PlatformConfigSpec.BooleanValue rottenBladeCooldownCheck;

        private WeaponSettings(PlatformConfigSpec.Builder builder) {
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

    public static final class CurioSettings {
        public final PlatformConfigSpec.IntValue judgementInvulnerabilityTicks;
        public final PlatformConfigSpec.IntValue judgementCooldownTicks;
        public final PlatformConfigSpec.DoubleValue lightHaloAttackDamageBonus;
        public final PlatformConfigSpec.DoubleValue lightHaloAttackSpeedBonus;
        public final PlatformConfigSpec.IntValue lightHaloBrightnessThreshold;
        public final PlatformConfigSpec.DoubleValue lightStaffDirectDamageMultiplier;
        public final PlatformConfigSpec.DoubleValue lightStaffAreaDamageMultiplier;
        public final PlatformConfigSpec.DoubleValue lightStaffRadius;
        public final PlatformConfigSpec.DoubleValue lightStaffFallHeight;
        public final PlatformConfigSpec.IntValue lightStaffFallTicks;
        public final PlatformConfigSpec.IntValue lightStaffImpactLingerTicks;
        public final PlatformConfigSpec.IntValue lightHaloCooldownTicks;
        public final PlatformConfigSpec.DoubleValue darkHaloMaxHealthBonus;
        public final PlatformConfigSpec.DoubleValue darkHaloArmorBonus;
        public final PlatformConfigSpec.IntValue darkHaloNightVisionIntervalTicks;
        public final PlatformConfigSpec.IntValue darkHaloNightVisionDurationTicks;
        public final PlatformConfigSpec.IntValue darkHaloBrightnessThreshold;
        public final PlatformConfigSpec.IntValue darkHaloEffectCount;
        public final PlatformConfigSpec.IntValue darkHaloEffectDurationTicks;
        public final PlatformConfigSpec.IntValue darkHaloMinimumEffectLevel;
        public final PlatformConfigSpec.IntValue darkHaloMaximumEffectLevel;
        public final PlatformConfigSpec.IntValue darkHaloMarkDurationTicks;
        public final PlatformConfigSpec.DoubleValue darkHaloLifeStealMultiplier;
        public final PlatformConfigSpec.DoubleValue darkHaloSourcelessHealingMultiplier;
        public final PlatformConfigSpec.DoubleValue darkHaloSmokeParticlesPerBlock;
        public final PlatformConfigSpec.IntValue darkHaloMaximumSmokeParticles;
        public final PlatformConfigSpec.DoubleValue voidRingHealthRestoreMultiplier;
        public final PlatformConfigSpec.IntValue voidRingFoodLevel;
        public final PlatformConfigSpec.DoubleValue voidRingSaturationLevel;
        public final PlatformConfigSpec.IntValue voidRingSpectatorDurationTicks;
        public final PlatformConfigSpec.IntValue voidRingCooldownTicks;
        public final PlatformConfigSpec.BooleanValue voidRingBypassesHaloBrightness;

        private CurioSettings(PlatformConfigSpec.Builder builder) {
            this.judgementInvulnerabilityTicks = builder
                    .comment("\nJudgement invulnerability duration in ticks. Default: 60")
                    .defineInRange("judgementInvulnerabilityTicks", 60, 0, 72000);
            this.judgementCooldownTicks = builder
                    .comment("\nJudgement cooldown in ticks, starting when damage is blocked. Default: 240")
                    .defineInRange("judgementCooldownTicks", 240, 0, 72000);
            this.lightHaloAttackDamageBonus = builder
                    .comment("\nLight Forged Halo attack damage bonus. Default: 0.25")
                    .defineInRange("lightHaloAttackDamageBonus", 0.25d, 0d, 10d);
            this.lightHaloAttackSpeedBonus = builder
                    .comment("\nLight Forged Halo flat attack speed bonus. Default: 0.2")
                    .defineInRange("lightHaloAttackSpeedBonus", 0.2d, 0d, 1024d);
            this.lightHaloBrightnessThreshold = builder
                    .comment("\nMinimum brightness required to summon a Light Staff. Default: 14")
                    .defineInRange("lightHaloBrightnessThreshold", 14, 0, 15);
            this.lightStaffDirectDamageMultiplier = builder
                    .comment("\nLight Staff direct damage as a multiplier of attack damage. Default: 0.75")
                    .defineInRange("lightStaffDirectDamageMultiplier", 0.75d, 0d, 100d);
            this.lightStaffAreaDamageMultiplier = builder
                    .comment("\nLight Staff area damage as a multiplier of attack damage. Default: 0.5")
                    .defineInRange("lightStaffAreaDamageMultiplier", 0.5d, 0d, 100d);
            this.lightStaffRadius = builder
                    .comment("\nLight Staff area damage radius in blocks. Default: 4")
                    .defineInRange("lightStaffRadius", 4d, 0d, 64d);
            this.lightStaffFallHeight = builder
                    .comment("\nLight Staff summon height in blocks. Default: 8")
                    .defineInRange("lightStaffFallHeight", 8d, 0d, 128d);
            this.lightStaffFallTicks = builder
                    .comment("\nLight Staff fall duration in ticks. Default: 10")
                    .defineInRange("lightStaffFallTicks", 10, 1, 1200);
            this.lightStaffImpactLingerTicks = builder
                    .comment("\nLight Staff visible duration after impact in ticks. Default: 60")
                    .defineInRange("lightStaffImpactLingerTicks", 60, 0, 1200);
            this.lightHaloCooldownTicks = builder
                    .comment("\nLight Forged Halo effect cooldown in ticks. Default: 30")
                    .defineInRange("lightHaloCooldownTicks", 30, 0, 72000);
            this.darkHaloMaxHealthBonus = builder
                    .comment("\nDark Forged Halo maximum health bonus. Default: 0.25")
                    .defineInRange("darkHaloMaxHealthBonus", 0.25d, 0d, 10d);
            this.darkHaloArmorBonus = builder
                    .comment("\nDark Forged Halo armor bonus. Default: 0.25")
                    .defineInRange("darkHaloArmorBonus", 0.25d, 0d, 10d);
            this.darkHaloNightVisionIntervalTicks = builder
                    .comment("\nDark Forged Halo night vision refresh interval in ticks. Default: 10")
                    .defineInRange("darkHaloNightVisionIntervalTicks", 10, 1, 1200);
            this.darkHaloNightVisionDurationTicks = builder
                    .comment("\nDark Forged Halo night vision duration in ticks. Default: 100")
                    .defineInRange("darkHaloNightVisionDurationTicks", 100, 1, 1200);
            this.darkHaloBrightnessThreshold = builder
                    .comment("\nMaximum brightness required to trigger Dark Forged Halo. Default: 5")
                    .defineInRange("darkHaloBrightnessThreshold", 5, 0, 15);
            this.darkHaloEffectCount = builder
                    .comment("\nNumber of random effects applied by Dark Forged Halo. Default: 3")
                    .defineInRange("darkHaloEffectCount", 3, 0, 64);
            this.darkHaloEffectDurationTicks = builder
                    .comment("\nDark Forged Halo random effect duration in ticks. Default: 40")
                    .defineInRange("darkHaloEffectDurationTicks", 40, 1, 72000);
            this.darkHaloMinimumEffectLevel = builder
                    .comment("\nMinimum displayed random effect level. Default: 1")
                    .defineInRange("darkHaloMinimumEffectLevel", 1, 1, 255);
            this.darkHaloMaximumEffectLevel = builder
                    .comment("\nMaximum displayed random effect level. Default: 5")
                    .defineInRange("darkHaloMaximumEffectLevel", 5, 1, 255);
            this.darkHaloMarkDurationTicks = builder
                    .comment("\nDark Forged Halo mark duration in ticks. Default: 100")
                    .defineInRange("darkHaloMarkDurationTicks", 100, 0, 72000);
            this.darkHaloLifeStealMultiplier = builder
                    .comment("\nHealing from damage dealt to marked targets. Default: 0.5")
                    .defineInRange("darkHaloLifeStealMultiplier", 0.5d, 0d, 100d);
            this.darkHaloSourcelessHealingMultiplier = builder
                    .comment("\nHealing from sourceless damage taken. Default: 0.5")
                    .defineInRange("darkHaloSourcelessHealingMultiplier", 0.5d, 0d, 100d);
            this.darkHaloSmokeParticlesPerBlock = builder
                    .comment("\nDark smoke line particle density per block. Default: 4")
                    .defineInRange("darkHaloSmokeParticlesPerBlock", 4d, 0.1d, 64d);
            this.darkHaloMaximumSmokeParticles = builder
                    .comment("\nMaximum particles in one Dark Forged Halo smoke line. Default: 128")
                    .defineInRange("darkHaloMaximumSmokeParticles", 128, 1, 4096);
            this.voidRingHealthRestoreMultiplier = builder
                    .comment("\nVoid Ring restored maximum health multiplier. Default: 1")
                    .defineInRange("voidRingHealthRestoreMultiplier", 1d, 0d, 10d);
            this.voidRingFoodLevel = builder
                    .comment("\nVoid Ring restored food level. Default: 20")
                    .defineInRange("voidRingFoodLevel", 20, 0, 20);
            this.voidRingSaturationLevel = builder
                    .comment("\nVoid Ring restored saturation level. Default: 20")
                    .defineInRange("voidRingSaturationLevel", 20d, 0d, 20d);
            this.voidRingSpectatorDurationTicks = builder
                    .comment("\nVoid Ring spectator duration in ticks. Default: 100")
                    .defineInRange("voidRingSpectatorDurationTicks", 100, 0, 72000);
            this.voidRingCooldownTicks = builder
                    .comment("\nVoid Ring cooldown in ticks. Default: 6000")
                    .defineInRange("voidRingCooldownTicks", 6000, 0, 720000);
            this.voidRingBypassesHaloBrightness = builder
                    .comment("\nAllow Void Ring to bypass both forged halo brightness conditions.")
                    .define("voidRingBypassesHaloBrightness", true);
        }
    }
}
