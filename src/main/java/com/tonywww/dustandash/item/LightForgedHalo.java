package com.tonywww.dustandash.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.tonywww.dustandash.DustAndAshConfig;
import net.minecraft.network.chat.Component;
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

public final class LightForgedHalo extends Item implements ICurioItem {
    public LightForgedHalo(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return "back".equals(slotContext.identifier());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(
            SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(
            modifierUuid(uuid, "attack_damage"),
                "Light forged halo attack damage",
                DustAndAshConfig.CURIOS.lightHaloAttackDamageBonus.get(),
                AttributeModifier.Operation.MULTIPLY_TOTAL));
        modifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(
            modifierUuid(uuid, "attack_speed"),
                "Light forged halo attack speed",
                DustAndAshConfig.CURIOS.lightHaloAttackSpeedBonus.get(),
                AttributeModifier.Operation.ADDITION));
        return modifiers;
    }

    private static UUID modifierUuid(UUID slotUuid, String attributeName) {
        return UUID.nameUUIDFromBytes(
                (slotUuid + ":light_forged_halo:" + attributeName).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
    }
}