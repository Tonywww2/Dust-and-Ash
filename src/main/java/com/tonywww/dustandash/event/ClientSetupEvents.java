package com.tonywww.dustandash.event;

import com.tonywww.dustandash.registeries.ModEntites;
import com.tonywww.dustandash.registeries.ModContainerMenus;
import com.tonywww.dustandash.cthulhu.client.CthulhuGraphemeOverlay;
import com.tonywww.dustandash.cthulhu.client.CthulhuHintOverlay;
import com.tonywww.dustandash.cthulhu.client.CthulhuPhaseOverlay;
import com.tonywww.dustandash.cthulhu.client.CthulhuTextEntityRenderer;
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
            MenuScreens.register(ModContainerMenus.FISSION_REACTOR_CONTROLLER_CONTAINER.get(), FissionReactorControllerScreen::new);
            MenuScreens.register(ModContainerMenus.FISSION_REACTOR_INTERFACE_CONTAINER.get(), FissionReactorInterfaceScreen::new);


        });

        EntityRenderers.register(ModEntites.LIGHTNING_BULLET.get(), ThrownItemRenderer::new);
        EntityRenderers.register(ModEntites.CTHULHU_BOSS_PHASE1.get(), CthulhuTextEntityRenderer::new);
        EntityRenderers.register(ModEntites.CTHULHU_PILLAR.get(), CthulhuTextEntityRenderer::new);
        EntityRenderers.register(ModEntites.CTHULHU_STORM_GOLEM.get(), CthulhuTextEntityRenderer::new);
        EntityRenderers.register(ModEntites.CTHULHU_GRAPHEME.get(), CthulhuTextEntityRenderer::new);
        EntityRenderers.register(ModEntites.CTHULHU_LAW_FIELD.get(), CthulhuTextEntityRenderer::new);

    }

    @SubscribeEvent
    public static void registerGuiOverlay(RegisterGuiOverlaysEvent event) {
        event.registerBelowAll("dustandash.white_lightning", WhiteLightningOverlay.INSTANCE);
        event.registerBelowAll("dustandash.cthulhu_phase", CthulhuPhaseOverlay.INSTANCE);
        event.registerBelowAll("dustandash.cthulhu_graphemes", CthulhuGraphemeOverlay.INSTANCE);
        event.registerBelowAll("dustandash.cthulhu_hints", CthulhuHintOverlay.INSTANCE);

    }
}
