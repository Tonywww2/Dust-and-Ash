package com.tonywww.dustandash.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tonywww.dustandash.DustAndAshConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public final class DarkForgedHalo extends Item implements ICurioItem {
    public DarkForgedHalo(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return "back".equals(slotContext.identifier());
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity wearer = slotContext.entity();
        int interval = DustAndAshConfig.CURIOS.darkHaloNightVisionIntervalTicks.get();
        if (!wearer.level().isClientSide() && wearer.tickCount % interval == 0) {
            wearer.addEffect(new MobEffectInstance(
                    MobEffects.NIGHT_VISION,
                    DustAndAshConfig.CURIOS.darkHaloNightVisionDurationTicks.get(),
                    0,
                    true,
                    false));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(
            SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(Attributes.MAX_HEALTH, new AttributeModifier(
            modifierUuid(uuid, "maximum_health"),
                "Dark forged halo maximum health",
                DustAndAshConfig.CURIOS.darkHaloMaxHealthBonus.get(),
                AttributeModifier.Operation.MULTIPLY_TOTAL));
        modifiers.put(Attributes.ARMOR, new AttributeModifier(
            modifierUuid(uuid, "armor"),
                "Dark forged halo armor",
                DustAndAshConfig.CURIOS.darkHaloArmorBonus.get(),
                AttributeModifier.Operation.MULTIPLY_TOTAL));
        return modifiers;
    }

    private static UUID modifierUuid(UUID slotUuid, String attributeName) {
        return UUID.nameUUIDFromBytes(
                (slotUuid + ":dark_forged_halo:" + attributeName).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.dustandash.dark_forged_halo"));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}