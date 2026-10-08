package com.tonywww.dustandash.item;

import com.tonywww.dustandash.platform.PlatformItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import java.util.List;

public class Judgement extends PlatformItem implements ICurioItem {

    public Judgement(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return "necklace".equals(slotContext.identifier());
    }

    @Override
    public void appendTooltip(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {


        super.appendTooltip(pStack, pLevel, pTooltip, pFlag);
    }

}
