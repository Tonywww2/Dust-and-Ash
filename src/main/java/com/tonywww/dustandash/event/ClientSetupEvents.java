package com.tonywww.dustandash.event;

import com.tonywww.dustandash.entity.ModEntites;
import com.tonywww.dustandash.menu.ModContainerMenus;
import com.tonywww.dustandash.overlay.WhiteLightningOverlay;
import com.tonywww.dustandash.screen.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEvents {

    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModContainerMenus.INTEGRATED_BLOCK_CONTAINER.get(), IntegratedBlockScreen::new);
            MenuScreens.register(ModContainerMenus.ASH_COLLECTOR_CONTAINER.get(), AshCollectorScreen::new);
            MenuScreens.register(ModContainerMenus.MILLING_MACHINE_CONTAINER.get(), MillingMachineScreen::new);
            MenuScreens.register(ModContainerMenus.CENTRIFUGE_CONTAINER.get(), CentrifugeScreen::new);
            MenuScreens.register(ModContainerMenus.IONIZER_CONTAINER.get(), IonizerScreen::new);
            MenuScreens.register(ModContainerMenus.ITEM_SENDER_CONTAINER.get(), ItemSenderScreen::new);


        });

        EntityRenderers.register(ModEntites.LIGHTNING_BULLET.get(), ThrownItemRenderer::new);

    }

    @SubscribeEvent
    public static void registerGuiOverlay(RegisterGuiOverlaysEvent event) {
        event.registerBelowAll("dustandash.white_lightning", WhiteLightningOverlay.INSTANCE);

    }
}
