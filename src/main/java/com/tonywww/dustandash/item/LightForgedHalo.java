package com.tonywww.dustandash.item;

import com.tonywww.dustandash.platform.DustAndAshConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;

public final class LightForgedHalo extends com.tonywww.dustandash.platform.PlatformCurioItem {
    public LightForgedHalo(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return "back".equals(slotContext.identifier());
    }

    @Override protected void addModifiers(ModifierSink sink) {
        sink.add("attack_damage", Stat.ATTACK_DAMAGE, DustAndAshConfig.CURIOS.lightHaloAttackDamageBonus.get(), true);
        sink.add("attack_speed", Stat.ATTACK_SPEED, DustAndAshConfig.CURIOS.lightHaloAttackSpeedBonus.get(), false);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendTooltip(stack, level, tooltip, flag);
    }
}
