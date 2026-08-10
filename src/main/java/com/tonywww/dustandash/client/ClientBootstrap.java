package com.tonywww.dustandash.client;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.overlay.WhiteLightningOverlay;
import com.tonywww.dustandash.registry.DAAContainerMenus;
import com.tonywww.dustandash.registry.DAAEntities;
import com.tonywww.dustandash.screen.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = DustAndAsh.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ClientBootstrap {
    private ClientBootstrap() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(DAAContainerMenus.INTEGRATED_BLOCK_CONTAINER.get(), IntegratedBlockScreen::new);
            MenuScreens.register(DAAContainerMenus.ASH_COLLECTOR_CONTAINER.get(), AshCollectorScreen::new);
            MenuScreens.register(DAAContainerMenus.MILLING_MACHINE_CONTAINER.get(), MillingMachineScreen::new);
            MenuScreens.register(DAAContainerMenus.CENTRIFUGE_CONTAINER.get(), CentrifugeScreen::new);
            MenuScreens.register(DAAContainerMenus.IONIZER_CONTAINER.get(), IonizerScreen::new);
            MenuScreens.register(DAAContainerMenus.ITEM_SENDER_CONTAINER.get(), ItemSenderScreen::new);
            MenuScreens.register(DAAContainerMenus.FISSION_REACTOR_CONTROLLER_CONTAINER.get(),
                    FissionReactorControllerScreen::new);
            MenuScreens.register(DAAContainerMenus.FISSION_REACTOR_INTERFACE_CONTAINER.get(),
                    FissionReactorInterfaceScreen::new);
            EntityRenderers.register(DAAEntities.LIGHTNING_BULLET.get(), ThrownItemRenderer::new);
        });
    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerBelowAll("dustandash.white_lightning", WhiteLightningOverlay.INSTANCE);
    }
}