package com.tonywww.dustandash.client.tooltip;

import com.tonywww.dustandash.DustAndAshConfig;
import com.tonywww.dustandash.item.WhiteLightning;
import com.tonywww.dustandash.registry.DAAItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public final class DAATooltipCatalog {
    private static final String BASE = "tooltip.dustandash.gear.";

    private DAATooltipCatalog() {
    }

    public static void registerAll() {
        register(DAAItems.HAND_VACUUM.get(), "hand_vacuum", 2, (stack, tooltip) -> {
            addDurability(stack, tooltip);
            stat(tooltip, "success_chance", percent(DustAndAshConfig.TOOLS.handVacuumSuccessRate.get()));
            stat(tooltip, "success_cooldown", seconds(15));
            stat(tooltip, "failure_cooldown", seconds(10));
        });
        register(DAAItems.IRON_VACUUM.get(), "iron_vacuum", 2, (stack, tooltip) -> {
            addDurability(stack, tooltip);
            stat(tooltip, "fuel_consume_chance", percent(DustAndAshConfig.TOOLS.ironVacuumConsumeRate.get()));
            stat(tooltip, "cooldown", seconds(5));
        });
        register(DAAItems.SHARPEN_FLINT.get(), "sharpen_flint", 2, (stack, tooltip) -> {
            stat(tooltip, "self_damage", number(2));
            stat(tooltip, "output_count", number(1));
        });

        registerTiered(DAAItems.FLINT_PICKAXE.get(), "standard_tool");
        registerTiered(DAAItems.ASH_STEEL_SWORD.get(), "standard_weapon");
        registerTiered(DAAItems.ASH_STEEL_SHOVEL.get(), "standard_tool");
        registerTiered(DAAItems.ASH_STEEL_PICKAXE.get(), "standard_tool");
        registerTiered(DAAItems.ASH_STEEL_AXE.get(), "standard_tool");
        registerTiered(DAAItems.ASH_STEEL_HOE.get(), "standard_tool");
        registerTiered(DAAItems.TITANIUM_ALLOY_PICKAXE.get(), "standard_tool");

        register(DAAItems.TITANIUM_ALLOY_SWORD.get(), "titanium_alloy_sword", 2, (stack, tooltip) -> {
            addMainHandStats(stack, tooltip);
            addTierStats(stack, tooltip);
            stat(tooltip, "effect_duration", seconds(100));
        });
        register(DAAItems.TITANIUM_ALLOY_GREAT_SWORD.get(), "titanium_alloy_great_sword", 2,
                (stack, tooltip) -> {
                    addMainHandStats(stack, tooltip);
                    addTierStats(stack, tooltip);
                    stat(tooltip, "effect_duration", seconds(100));
                });
        register(DAAItems.GALE_OTAIJUTSU.get(), "gale_otaijutsu", 3, (stack, tooltip) -> {
            addMainHandStats(stack, tooltip);
            addTierStats(stack, tooltip);
            stat(tooltip, "fall_damage_multiplier",
                    percent(DustAndAshConfig.WEAPONS.galeOtaijutsuDamageRate.get()));
            stat(tooltip, "leap_cooldown", seconds(40));
        });
        register(DAAItems.SUNBURN_MEGA_SWORD.get(), "sunburn_mega_sword", 3, (stack, tooltip) -> {
            addMainHandStats(stack, tooltip);
            addTierStats(stack, tooltip);
            stat(tooltip, "explosion_radius", blocks(2.25d));
            stat(tooltip, "fire_duration", seconds(80));
            stat(tooltip, "cooldown", seconds(35));
        });
        register(DAAItems.WHITE_LIGHTNING.get(), "white_lightning", 4, (stack, tooltip) -> {
            addMainHandStats(stack, tooltip);
            addTierStats(stack, tooltip);
            stat(tooltip, "fixed_extra_damage",
                    number(DustAndAshConfig.WEAPONS.whiteLightningExtraDamage.get()));
            stat(tooltip, "health_damage",
                    percent(DustAndAshConfig.WEAPONS.whiteLightningExtraPercentage.get()));
            stat(tooltip, "normal_charges", number(WhiteLightning.getCharges(stack))
                    .append(Component.literal(" / 16")));
            stat(tooltip, "advanced_charges", number(WhiteLightning.getAdvCharges(stack))
                    .append(Component.literal(" / 10")));
            stat(tooltip, "charge_conversion", literal("8 -> 10"));
        });
        register(DAAItems.LORD_OF_BLOOD.get(), "lord_of_blood", 4, (stack, tooltip) -> {
            addMainHandStats(stack, tooltip);
            addTierStats(stack, tooltip);
            stat(tooltip, "radius", blocks(DustAndAshConfig.WEAPONS.lordOfBloodRadius.get()));
            stat(tooltip, "maximum_charges", number(3));
            stat(tooltip, "attack_healing", number(2));
            stat(tooltip, "ritual_damage", literal("3 / 4 / 5"));
            stat(tooltip, "ritual_cooldown", seconds(15));
        });
        register(DAAItems.ROTTEN_BLADE.get(), "rotten_blade", 4, (stack, tooltip) -> {
            addMainHandStats(stack, tooltip);
            addTierStats(stack, tooltip);
            stat(tooltip, "stored_targets", number(6));
            stat(tooltip, "extra_damage", number(DustAndAshConfig.WEAPONS.rottenBladeExtraDamage.get()));
            stat(tooltip, "radius", blocks(DustAndAshConfig.WEAPONS.rottenBladeRadius.get()));
            stat(tooltip, "height", blocks(DustAndAshConfig.WEAPONS.rottenBladeHeight.get()));
            stat(tooltip, "area_damage", number(8));
            stat(tooltip, "area_cooldown", seconds(200));
            stat(tooltip, "single_cooldown", seconds(60));
        });

        register(DAAItems.JUDGEMENT.get(), "judgement", 2, (stack, tooltip) -> {
            stat(tooltip, "invulnerability", seconds(DustAndAshConfig.CURIOS.judgementInvulnerabilityTicks.get()));
            stat(tooltip, "cooldown", seconds(DustAndAshConfig.CURIOS.judgementCooldownTicks.get()));
        });
        register(DAAItems.LIGHT_FORGED_HALO.get(), "light_forged_halo", 4, (stack, tooltip) -> {
            stat(tooltip, "attack_damage_bonus", percent(DustAndAshConfig.CURIOS.lightHaloAttackDamageBonus.get()));
            stat(tooltip, "attack_speed_bonus", number(DustAndAshConfig.CURIOS.lightHaloAttackSpeedBonus.get()));
            stat(tooltip, "brightness_threshold", number(DustAndAshConfig.CURIOS.lightHaloBrightnessThreshold.get()));
            stat(tooltip, "direct_damage", percent(DustAndAshConfig.CURIOS.lightStaffDirectDamageMultiplier.get()));
            stat(tooltip, "area_damage", percent(DustAndAshConfig.CURIOS.lightStaffAreaDamageMultiplier.get()));
            stat(tooltip, "radius", blocks(DustAndAshConfig.CURIOS.lightStaffRadius.get()));
            stat(tooltip, "fall_height", blocks(DustAndAshConfig.CURIOS.lightStaffFallHeight.get()));
            stat(tooltip, "fall_duration", seconds(DustAndAshConfig.CURIOS.lightStaffFallTicks.get()));
            stat(tooltip, "cooldown", seconds(DustAndAshConfig.CURIOS.lightHaloCooldownTicks.get()));
        });
        register(DAAItems.DARK_FORGED_HALO.get(), "dark_forged_halo", 5, (stack, tooltip) -> {
            stat(tooltip, "maximum_health_bonus", percent(DustAndAshConfig.CURIOS.darkHaloMaxHealthBonus.get()));
            stat(tooltip, "armor_bonus", percent(DustAndAshConfig.CURIOS.darkHaloArmorBonus.get()));
            stat(tooltip, "brightness_threshold", number(DustAndAshConfig.CURIOS.darkHaloBrightnessThreshold.get()));
            stat(tooltip, "effect_count", number(DustAndAshConfig.CURIOS.darkHaloEffectCount.get()));
            stat(tooltip, "effect_duration", seconds(DustAndAshConfig.CURIOS.darkHaloEffectDurationTicks.get()));
            stat(tooltip, "effect_level", range(
                    DustAndAshConfig.CURIOS.darkHaloMinimumEffectLevel.get(),
                    DustAndAshConfig.CURIOS.darkHaloMaximumEffectLevel.get()));
            stat(tooltip, "mark_duration", seconds(DustAndAshConfig.CURIOS.darkHaloMarkDurationTicks.get()));
            stat(tooltip, "lifesteal", percent(DustAndAshConfig.CURIOS.darkHaloLifeStealMultiplier.get()));
            stat(tooltip, "sourceless_healing", percent(DustAndAshConfig.CURIOS.darkHaloSourcelessHealingMultiplier.get()));
            stat(tooltip, "night_vision_refresh", seconds(DustAndAshConfig.CURIOS.darkHaloNightVisionIntervalTicks.get()));
            stat(tooltip, "night_vision_duration", seconds(DustAndAshConfig.CURIOS.darkHaloNightVisionDurationTicks.get()));
        });
        register(DAAItems.VOID_RING.get(), "void_ring", 4, (stack, tooltip) -> {
            stat(tooltip, "health_restored", percent(DustAndAshConfig.CURIOS.voidRingHealthRestoreMultiplier.get()));
            stat(tooltip, "food_restored", number(DustAndAshConfig.CURIOS.voidRingFoodLevel.get()));
            stat(tooltip, "saturation_restored", number(DustAndAshConfig.CURIOS.voidRingSaturationLevel.get()));
            stat(tooltip, "spectator_duration", seconds(DustAndAshConfig.CURIOS.voidRingSpectatorDurationTicks.get()));
            stat(tooltip, "cooldown", seconds(DustAndAshConfig.CURIOS.voidRingCooldownTicks.get()));
            stat(tooltip, "brightness_bypass", bool(DustAndAshConfig.CURIOS.voidRingBypassesHaloBrightness.get()));
        });

        register(DAAItems.RAIN_CRYSTAL.get(), "rain_crystal", 2, consumableWeatherStats());
        register(DAAItems.SUN_CRYSTAL.get(), "sun_crystal", 2, consumableWeatherStats());
        register(DAAItems.ECHO_ACTIVATOR.get(), "echo_activator", 2, DAATooltipCatalog::addDurability);
        register(DAAItems.NANO_MACHINE.get(), "nano_machine", 4, (stack, tooltip) -> {
            stat(tooltip, "use_duration", seconds(60));
            stat(tooltip, "short_effect_duration", seconds(2400));
            stat(tooltip, "long_effect_duration", seconds(12000));
            stat(tooltip, "extended_effect_duration", seconds(24000));
        });
        register(DAAItems.POSITION_SELECTOR.get(), "position_selector", 2,
                (stack, tooltip) -> stat(tooltip, "cooldown", seconds(10)));
        register(DAAItems.ROCK_SOLID.get(), "rock_solid", 2, (stack, tooltip) -> {
            stat(tooltip, "cooldown", seconds(DustAndAshConfig.TOOLS.rockSolidCooldown.get()));
            stat(tooltip, "absorption", effect(4, 100));
            stat(tooltip, "resistance", effect(1, 120));
            stat(tooltip, "slowness", effect(2, 80));
        });
        register(DAAItems.INDESTRUCTIBLE.get(), "indestructible", 2, (stack, tooltip) -> {
            stat(tooltip, "cooldown", seconds(DustAndAshConfig.TOOLS.indestructibleCooldown.get()));
            stat(tooltip, "absorption", effect(6, 120));
            stat(tooltip, "resistance", effect(4, 160));
            stat(tooltip, "slowness", effect(1, 60));
        });
    }

    private static DAATooltipApi.StatProvider consumableWeatherStats() {
        return (stack, tooltip) -> {
            stat(tooltip, "cooldown", seconds(200));
            stat(tooltip, "consumed", number(1));
        };
    }

    private static void registerTiered(Item item, String key) {
        register(item, key, 1, (stack, tooltip) -> {
            addMainHandStats(stack, tooltip);
            addTierStats(stack, tooltip);
        });
    }

    private static void register(
            Item item,
            String key,
            int detailLines,
            DAATooltipApi.StatProvider statProvider) {
        DAATooltipApi.register(item, BASE + key, detailLines, statProvider);
    }

    private static void addMainHandStats(ItemStack stack, List<Component> tooltip) {
        Collection<AttributeModifier> damage = stack
                .getAttributeModifiers(EquipmentSlot.MAINHAND)
                .get(Attributes.ATTACK_DAMAGE);
        Collection<AttributeModifier> speed = stack
                .getAttributeModifiers(EquipmentSlot.MAINHAND)
                .get(Attributes.ATTACK_SPEED);
        if (!damage.isEmpty()) {
            stat(tooltip, "attack_damage", number(attributeValue(1d, damage)));
        }
        if (!speed.isEmpty()) {
            stat(tooltip, "attack_speed", number(attributeValue(4d, speed)));
        }
    }

    private static double attributeValue(double baseValue, Collection<AttributeModifier> modifiers) {
        double value = baseValue;
        for (AttributeModifier modifier : modifiers) {
            if (modifier.getOperation() == AttributeModifier.Operation.ADDITION) {
                value += modifier.getAmount();
            }
        }
        double valueAfterAdditions = value;
        for (AttributeModifier modifier : modifiers) {
            if (modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE) {
                value += valueAfterAdditions * modifier.getAmount();
            }
        }
        for (AttributeModifier modifier : modifiers) {
            if (modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_TOTAL) {
                value *= 1d + modifier.getAmount();
            }
        }
        return value;
    }

    private static void addTierStats(ItemStack stack, List<Component> tooltip) {
        if (stack.getItem() instanceof TieredItem tieredItem) {
            stat(tooltip, "durability", number(tieredItem.getTier().getUses()));
            stat(tooltip, "mining_level", number(tieredItem.getTier().getLevel()));
            stat(tooltip, "mining_speed", number(tieredItem.getTier().getSpeed()));
            stat(tooltip, "enchantability", number(tieredItem.getTier().getEnchantmentValue()));
        } else {
            addDurability(stack, tooltip);
        }
    }

    private static void addDurability(ItemStack stack, List<Component> tooltip) {
        if (stack.getMaxDamage() > 0) {
            stat(tooltip, "durability", number(stack.getMaxDamage()));
        }
    }

    private static void stat(List<Component> tooltip, String label, Component value) {
        tooltip.add(Component.translatable(
                        "tooltip.dustandash.system.stat",
                        Component.translatable("tooltip.dustandash.stat." + label)
                                .withStyle(ChatFormatting.AQUA),
                        value)
                .withStyle(ChatFormatting.WHITE));
    }

    private static MutableComponent literal(String value) {
        return Component.literal(value).withStyle(ChatFormatting.YELLOW);
    }

    private static MutableComponent number(double value) {
        return literal(format(value));
    }

    private static MutableComponent percent(double value) {
        return Component.translatable("tooltip.dustandash.system.percent", format(value * 100d))
                .withStyle(ChatFormatting.YELLOW);
    }

    private static MutableComponent seconds(int ticks) {
        return Component.translatable("tooltip.dustandash.system.seconds", format(ticks / 20d))
                .withStyle(ChatFormatting.YELLOW);
    }

    private static MutableComponent blocks(double value) {
        return Component.translatable("tooltip.dustandash.system.blocks", format(value))
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
                        Component.translatable("tooltip.dustandash.system.seconds", format(durationTicks / 20d)))
                .withStyle(ChatFormatting.YELLOW);
    }

    private static String format(double value) {
        return BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
    }
}