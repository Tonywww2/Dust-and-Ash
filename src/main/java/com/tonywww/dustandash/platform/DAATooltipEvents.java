package com.tonywww.dustandash.platform;

import com.tonywww.dustandash.client.tooltip.DAATooltipApi;
import com.tonywww.dustandash.client.tooltip.NeutronTooltipComponent;
import com.mojang.datafixers.util.Either;
import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.entity.FissionReactor.NeutronContainerUpdater;
import com.tonywww.dustandash.tag.ModTags;
//? if forge {
import net.minecraftforge.api.distmarker.Dist;
//?} else {
/*import net.neoforged.api.distmarker.Dist;
*///?}

//? if forge {
import net.minecraftforge.client.event.RenderTooltipEvent;
//?} else {
/*import net.neoforged.neoforge.client.event.RenderTooltipEvent;
*///?}

//? if forge {
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
//?} else {
/*import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
*///?}

//? if forge {
import net.minecraftforge.eventbus.api.SubscribeEvent;
//?} else {
/*import net.neoforged.bus.api.SubscribeEvent;
*///?}

//? if forge {
import net.minecraftforge.fml.common.Mod;
//?} else {
/*import net.neoforged.fml.common.Mod;
*///?}


import java.util.List;

//? if forge {
@Mod.EventBusSubscriber(modid = DustAndAsh.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
//?} else {
/*@net.neoforged.fml.common.EventBusSubscriber(modid = DustAndAsh.MOD_ID, value = Dist.CLIENT, bus = net.neoforged.fml.common.EventBusSubscriber.Bus.GAME)
*///?}

public final class DAATooltipEvents {
    private DAATooltipEvents() {
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (isChargedNeutronContainer(event.getItemStack())) {
            removeLegacyLoreLines(
                    event.getToolTip(),
                    NeutronContainerUpdater.getLegacyLoreCount(event.getItemStack()));
        }
        DAATooltipApi.apply(event.getItemStack(), event.getToolTip());
    }

    @SubscribeEvent
    public static void onGatherTooltipComponents(RenderTooltipEvent.GatherComponents event) {
        if (!isChargedNeutronContainer(event.getItemStack())) {
            return;
        }

        event.getTooltipElements().add(Either.right(new NeutronTooltipComponent(
                NeutronContainerUpdater.getNeutron(event.getItemStack()),
                NeutronContainerUpdater.MAX_NEUTRON)));
    }

    private static boolean isChargedNeutronContainer(net.minecraft.world.item.ItemStack stack) {
        return stack.is(ModTags.Items.NEUTRON_CONTAINER)
                && NeutronContainerUpdater.hasNeutronData(stack);
    }

    private static void removeLegacyLoreLines(List<net.minecraft.network.chat.Component> tooltip, int count) {
        for (int index = 1; index < tooltip.size() && count > 0; index++) {
            if (NeutronContainerUpdater.isLegacyLoreText(tooltip.get(index).getString())) {
                tooltip.remove(index--);
                count--;
            }
        }
    }
}
