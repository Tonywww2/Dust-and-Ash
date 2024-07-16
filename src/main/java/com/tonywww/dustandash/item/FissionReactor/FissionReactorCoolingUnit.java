package com.tonywww.dustandash.item.FissionReactor;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class FissionReactorCoolingUnit extends Item {

    public double baseCoolingRate;

    public FissionReactorCoolingUnit(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        pTooltip.add(Component.translatable("tooltip.dustandash.maxDurability"));
        pTooltip.add(Component.literal(String.valueOf(pStack.getMaxDamage())));

        pTooltip.add(Component.translatable("tooltip.dustandash.baseCoolingRate"));
        pTooltip.add(Component.literal(String.valueOf(baseCoolingRate)));

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

    public double getBaseCoolingRate() {
        return baseCoolingRate;
    }
}
