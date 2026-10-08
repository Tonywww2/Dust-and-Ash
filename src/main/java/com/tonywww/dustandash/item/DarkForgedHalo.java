package com.tonywww.dustandash.item;

import com.tonywww.dustandash.platform.DustAndAshConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;

public final class DarkForgedHalo extends com.tonywww.dustandash.platform.PlatformCurioItem {
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

    @Override protected void addModifiers(ModifierSink sink) {
        sink.add("maximum_health", Stat.MAX_HEALTH, DustAndAshConfig.CURIOS.darkHaloMaxHealthBonus.get(), true);
        sink.add("armor", Stat.ARMOR, DustAndAshConfig.CURIOS.darkHaloArmorBonus.get(), true);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendTooltip(stack, level, tooltip, flag);
    }
}
