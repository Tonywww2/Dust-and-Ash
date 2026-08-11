package com.tonywww.dustandash.client.tooltip;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DustAndAsh.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class DAATooltipEvents {
    private DAATooltipEvents() {
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        DAATooltipApi.apply(event.getItemStack(), event.getToolTip());
    }
}