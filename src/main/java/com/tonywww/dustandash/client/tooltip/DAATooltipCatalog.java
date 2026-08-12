package com.tonywww.dustandash.client.tooltip;

import com.tonywww.dustandash.DustAndAshConfig;
import com.tonywww.dustandash.config.ImbaRules;
import com.tonywww.dustandash.item.WhiteLightning;
import com.tonywww.dustandash.registry.DAAItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class DAATooltipCatalog {
    private static final String BASE = "tooltip.dustandash.gear.";
    private static final int[] NO_EXACT_LINES = new int[0];

    private DAATooltipCatalog() {
    }

    public static void registerAll() {
        exact(DAAItems.HAND_VACUUM.get(), "hand_vacuum", 2, lines(1, 2), stack -> args(
                percent(DustAndAshConfig.TOOLS.handVacuumSuccessRate.get()), seconds(15), seconds(10)));
        exact(DAAItems.IRON_VACUUM.get(), "iron_vacuum", 2, lines(1, 2), stack -> args(
                percent(DustAndAshConfig.TOOLS.ironVacuumConsumeRate.get()), seconds(5)));
        exact(DAAItems.SHARPEN_FLINT.get(), "sharpen_flint", 2, lines(1), stack -> args(number(2), number(1)));

        plain(DAAItems.FLINT_PICKAXE.get(), "standard_tool", 1);
        plain(DAAItems.ASH_STEEL_SWORD.get(), "standard_weapon", 1);
        plain(DAAItems.ASH_STEEL_SHOVEL.get(), "standard_tool", 1);
        plain(DAAItems.ASH_STEEL_PICKAXE.get(), "standard_tool", 1);
        plain(DAAItems.ASH_STEEL_AXE.get(), "standard_tool", 1);
        plain(DAAItems.ASH_STEEL_HOE.get(), "standard_tool", 1);
        plain(DAAItems.TITANIUM_ALLOY_PICKAXE.get(), "standard_tool", 1);

        exact(DAAItems.TITANIUM_ALLOY_SWORD.get(), "titanium_alloy_sword", 2, lines(1, 2),
                stack -> args(seconds(100)));
        exact(DAAItems.TITANIUM_ALLOY_GREAT_SWORD.get(), "titanium_alloy_great_sword", 2, lines(1, 2),
                stack -> args(seconds(100)));
        imbaExact(DAAItems.GALE_OTAIJUTSU.get(), "gale_otaijutsu", 3, lines(1, 2, 3),
                stack -> galeOtaijutsuArgs(false), stack -> galeOtaijutsuArgs(true));
        imbaExact(DAAItems.SUNBURN_MEGA_SWORD.get(), "sunburn_mega_sword", 3, lines(1, 2),
                stack -> sunburnMegaSwordArgs(false), stack -> sunburnMegaSwordArgs(true));
        imbaExact(DAAItems.WHITE_LIGHTNING.get(), "white_lightning", 4, lines(1, 2, 3, 4),
                stack -> whiteLightningArgs(stack, false), stack -> whiteLightningArgs(stack, true));
        imbaExact(DAAItems.LORD_OF_BLOOD.get(), "lord_of_blood", 4, lines(1, 2, 3, 4),
                stack -> lordOfBloodArgs(false), stack -> lordOfBloodArgs(true));
        imbaExact(DAAItems.ROTTEN_BLADE.get(), "rotten_blade", 4, lines(1, 2, 3, 4),
                stack -> rottenBladeArgs(false), stack -> rottenBladeArgs(true));

        imbaExact(DAAItems.JUDGEMENT.get(), "judgement", 2, lines(1, 2),
                stack -> judgementArgs(false), stack -> judgementArgs(true));
        imbaExact(DAAItems.LIGHT_FORGED_HALO.get(), "light_forged_halo", 4, lines(1, 2, 3, 4),
                stack -> lightForgedHaloArgs(false), stack -> lightForgedHaloArgs(true));
        imbaExact(DAAItems.DARK_FORGED_HALO.get(), "dark_forged_halo", 5, lines(1, 2, 3, 4, 5),
                stack -> darkForgedHaloArgs(false), stack -> darkForgedHaloArgs(true));
        imbaExact(DAAItems.VOID_RING.get(), "void_ring", 4, lines(1, 2, 3, 4),
                stack -> voidRingArgs(false), stack -> voidRingArgs(true));

        exact(DAAItems.RAIN_CRYSTAL.get(), "rain_crystal", 2, lines(2),
                stack -> args(number(1), seconds(200)));
        exact(DAAItems.SUN_CRYSTAL.get(), "sun_crystal", 2, lines(2),
                stack -> args(number(1), seconds(200)));
        plain(DAAItems.ECHO_ACTIVATOR.get(), "echo_activator", 2);
        exact(DAAItems.NANO_MACHINE.get(), "nano_machine", 4, lines(2, 3, 4), stack -> args(
                seconds(60), seconds(2400), seconds(12000), seconds(24000)));
        exact(DAAItems.POSITION_SELECTOR.get(), "position_selector", 2, lines(1),
                stack -> args(seconds(10)));
        exact(DAAItems.ROCK_SOLID.get(), "rock_solid", 2, lines(1, 2), stack -> args(
                effect(4, 100), effect(1, 120), effect(2, 80),
                seconds(DustAndAshConfig.TOOLS.rockSolidCooldown.get()),
                seconds(DustAndAshConfig.TOOLS.indestructibleCooldown.get())));
        exact(DAAItems.INDESTRUCTIBLE.get(), "indestructible", 2, lines(1, 2), stack -> args(
                effect(6, 120), effect(4, 160), effect(1, 60),
                seconds(DustAndAshConfig.TOOLS.indestructibleCooldown.get()),
                seconds(DustAndAshConfig.TOOLS.rockSolidCooldown.get())));
    }

    private static void plain(Item item, String key, int detailLines) {
        DAATooltipApi.register(item, BASE + key, detailLines, false, NO_EXACT_LINES, stack -> new Object[0]);
    }

    private static void exact(
            Item item,
            String key,
            int detailLines,
            int[] exactDetailLines,
            DAATooltipApi.ArgumentProvider argumentProvider) {
        DAATooltipApi.register(item, BASE + key, detailLines, true, exactDetailLines, argumentProvider);
    }

        private static void imbaExact(
                        Item item,
                        String key,
                        int detailLines,
                        int[] exactDetailLines,
                        DAATooltipApi.ArgumentProvider argumentProvider,
                        DAATooltipApi.ArgumentProvider imbaArgumentProvider) {
                DAATooltipApi.registerImba(
                                item,
                                BASE + key,
                                BASE + key + ".imba",
                                detailLines,
                                true,
                                exactDetailLines,
                                argumentProvider,
                                imbaArgumentProvider);
        }

        private static Object[] galeOtaijutsuArgs(boolean imba) {
                return args(
                                number(ImbaRules.galeDamageMultiplier(imba)),
                                seconds(90), seconds(300), seconds(100), seconds(ImbaRules.galeCooldownTicks(imba)));
        }

        private static Object[] sunburnMegaSwordArgs(boolean imba) {
                return args(
                                seconds(ImbaRules.sunburnFireSeconds(imba) * 20),
                                seconds(ImbaRules.sunburnFireResistanceTicks(imba)),
                                imba
                                        ? number(ImbaRules.sunburnExplosionPower(true))
                                        : blocks(ImbaRules.sunburnExplosionPower(false)),
                                seconds(ImbaRules.sunburnCooldownTicks(imba)));
        }

        private static Object[] whiteLightningArgs(ItemStack stack, boolean imba) {
                return args(
                                number(WhiteLightning.getCharges(stack)), number(ImbaRules.whiteLightningMaxCharge(imba)),
                                number(WhiteLightning.getAdvCharges(stack)),
                                number(ImbaRules.whiteLightningMaxAdvancedCharge(imba)),
                                number(ImbaRules.whiteLightningConversionCost(imba)),
                                percent(DustAndAshConfig.WEAPONS.whiteLightningExtraPercentage.get()),
                                number(DustAndAshConfig.WEAPONS.whiteLightningExtraDamage.get() * 2d), number(5),
                                number(ImbaRules.whiteLightningHitsPerChargeGain(imba)),
                                number(ImbaRules.whiteLightningConversionGain()));
        }

        private static Object[] lordOfBloodArgs(boolean imba) {
                return args(
                                blocks(DustAndAshConfig.WEAPONS.lordOfBloodRadius.get()), number(3), number(2),
                                literal(imba ? "5" : "3 / 4 / 5"), seconds(15),
                                seconds(ImbaRules.lordOfBloodUseDurationTicks(imba)));
        }

        private static Object[] rottenBladeArgs(boolean imba) {
                return args(
                                number(ImbaRules.rottenBladeRememberedTargets(imba)),
                                number(DustAndAshConfig.WEAPONS.rottenBladeExtraDamage.get()),
                                number(DustAndAshConfig.WEAPONS.rottenBladeExtraDamage.get() / 2d),
                                number(ImbaRules.rottenBladeRecoilDamage(imba)),
                                blocks(DustAndAshConfig.WEAPONS.rottenBladeRadius.get()),
                                blocks(DustAndAshConfig.WEAPONS.rottenBladeHeight.get()),
                                number(8),
                                seconds(ImbaRules.rottenBladeAreaCooldownTicks(imba)),
                                seconds(ImbaRules.rottenBladeReleaseCooldownTicks(imba)));
        }

        private static Object[] judgementArgs(boolean imba) {
                return args(
                                seconds(DustAndAshConfig.CURIOS.judgementInvulnerabilityTicks.get()),
                                seconds(DustAndAshConfig.CURIOS.judgementCooldownTicks.get()),
                                percent(imba ? ImbaRules.judgementReflectionMultiplier() : 0d));
        }

        private static Object[] lightForgedHaloArgs(boolean imba) {
                return args(
                                percent(DustAndAshConfig.CURIOS.lightHaloAttackDamageBonus.get()),
                                number(DustAndAshConfig.CURIOS.lightHaloAttackSpeedBonus.get()),
                                number(DustAndAshConfig.CURIOS.lightHaloBrightnessThreshold.get()),
                                percent(DustAndAshConfig.CURIOS.lightStaffDirectDamageMultiplier.get()),
                                percent(DustAndAshConfig.CURIOS.lightStaffAreaDamageMultiplier.get()),
                                blocks(DustAndAshConfig.CURIOS.lightStaffRadius.get()),
                                blocks(DustAndAshConfig.CURIOS.lightStaffFallHeight.get()),
                                seconds(DustAndAshConfig.CURIOS.lightStaffFallTicks.get()),
                                seconds(ImbaRules.lightHaloCooldownTicks(imba)),
                                seconds(DustAndAshConfig.CURIOS.lightStaffImpactLingerTicks.get()));
        }

        private static Object[] darkForgedHaloArgs(boolean imba) {
                return args(
                                percent(DustAndAshConfig.CURIOS.darkHaloMaxHealthBonus.get()),
                                percent(DustAndAshConfig.CURIOS.darkHaloArmorBonus.get()),
                                seconds(DustAndAshConfig.CURIOS.darkHaloNightVisionIntervalTicks.get()),
                                seconds(DustAndAshConfig.CURIOS.darkHaloNightVisionDurationTicks.get()),
                                number(DustAndAshConfig.CURIOS.darkHaloBrightnessThreshold.get()),
                                number(DustAndAshConfig.CURIOS.darkHaloEffectCount.get()),
                                seconds(DustAndAshConfig.CURIOS.darkHaloEffectDurationTicks.get()),
                                range(DustAndAshConfig.CURIOS.darkHaloMinimumEffectLevel.get(),
                                                DustAndAshConfig.CURIOS.darkHaloMaximumEffectLevel.get()),
                                seconds(DustAndAshConfig.CURIOS.darkHaloMarkDurationTicks.get()),
                                percent(DustAndAshConfig.CURIOS.darkHaloLifeStealMultiplier.get()),
                                percent(DustAndAshConfig.CURIOS.darkHaloSourcelessHealingMultiplier.get()),
                                percent(1d - ImbaRules.darkHaloMarkedAttackerDamageMultiplier(imba)));
        }

        private static Object[] voidRingArgs(boolean imba) {
                return args(
                                percent(DustAndAshConfig.CURIOS.voidRingHealthRestoreMultiplier.get()),
                                number(DustAndAshConfig.CURIOS.voidRingFoodLevel.get()),
                                number(DustAndAshConfig.CURIOS.voidRingSaturationLevel.get()),
                                seconds(DustAndAshConfig.CURIOS.voidRingSpectatorDurationTicks.get()),
                                seconds(DustAndAshConfig.CURIOS.voidRingCooldownTicks.get()),
                                bool(DustAndAshConfig.CURIOS.voidRingBypassesHaloBrightness.get()),
                                effect(5, ImbaRules.voidRingResistanceDurationTicks()));
        }

    private static int[] lines(int... lines) {
        return lines;
    }

    private static Object[] args(Object... arguments) {
        return arguments;
    }

    private static MutableComponent literal(String value) {
        return Component.literal(value).withStyle(ChatFormatting.YELLOW);
    }

    private static MutableComponent number(double value) {
                return literal(format(value, 4));
    }

    private static MutableComponent percent(double value) {
                return Component.translatable("tooltip.dustandash.system.percent", format(value * 100d, 2))
                .withStyle(ChatFormatting.YELLOW);
    }

    private static MutableComponent seconds(int ticks) {
                return Component.translatable("tooltip.dustandash.system.seconds", format(ticks / 20d, 2))
                .withStyle(ChatFormatting.YELLOW);
    }

    private static MutableComponent blocks(double value) {
                return Component.translatable("tooltip.dustandash.system.blocks", format(value, 2))
                .withStyle(ChatFormatting.YELLOW);
    }

    private static MutableComponent range(int minimum, int maximum) {
        return Component.translatable("tooltip.dustandash.system.range", minimum, maximum)
                .withStyle(ChatFormatting.YELLOW);
    }

    private static MutableComponent bool(boolean value) {
        return Component.translatable(value
                        ? "tooltip.dustandash.system.enabled"
                        : "tooltip.dustandash.system.disabled")
                .withStyle(ChatFormatting.YELLOW);
    }

    private static MutableComponent effect(int level, int durationTicks) {
        return Component.translatable(
                        "tooltip.dustandash.system.effect",
                        level,
                                                Component.translatable("tooltip.dustandash.system.seconds", format(durationTicks / 20d, 2)))
                .withStyle(ChatFormatting.YELLOW);
    }

        private static String format(double value, int maxFractionDigits) {
                return BigDecimal.valueOf(value)
                                .setScale(maxFractionDigits, RoundingMode.HALF_UP)
                                .stripTrailingZeros()
                                .toPlainString();
    }
}
