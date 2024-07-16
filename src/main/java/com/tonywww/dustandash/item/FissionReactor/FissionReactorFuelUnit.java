package com.tonywww.dustandash.item.FissionReactor;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;


public class FissionReactorFuelUnit extends Item {

    public double baseHeatRate;
    public double baseNeutronRate;
    public double idealHeat;

    public FissionReactorFuelUnit(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        pTooltip.add(Component.translatable("tooltip.dustandash.maxDurability"));
        pTooltip.add(Component.literal(String.valueOf(pStack.getMaxDamage())));

        pTooltip.add(Component.translatable("tooltip.dustandash.baseHeatRate"));
        pTooltip.add(Component.literal(String.valueOf(baseHeatRate)));

        pTooltip.add(Component.translatable("tooltip.dustandash.baseNeutronRate"));
        pTooltip.add(Component.literal(String.valueOf(baseNeutronRate)));

        pTooltip.add(Component.translatable("tooltip.dustandash.idealHeat"));
        pTooltip.add(Component.literal(String.valueOf(idealHeat)));

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

    public double getBaseHeatRate() {
        return baseHeatRate;
    }

    public double getBaseNeutronRate() {
        return baseNeutronRate;
    }

    public double getIdealHeat() {
        return idealHeat;
    }
}
