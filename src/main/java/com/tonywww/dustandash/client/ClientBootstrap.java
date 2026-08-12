package com.tonywww.dustandash.client;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.client.particle.DarkSmokeParticle;
import com.tonywww.dustandash.client.render.ForgedHaloRenderer;
import com.tonywww.dustandash.client.render.LightStaffRenderer;
import com.tonywww.dustandash.client.cooldown.CurioCooldownOverlayApi;
import com.tonywww.dustandash.client.tooltip.ClientNeutronTooltipComponent;
import com.tonywww.dustandash.client.tooltip.DAATooltipCatalog;
import com.tonywww.dustandash.client.tooltip.NeutronTooltipComponent;
import com.tonywww.dustandash.cooldown.CurioCooldownManager;
import com.tonywww.dustandash.overlay.CurioCooldownOverlay;
import com.tonywww.dustandash.overlay.WhiteLightningOverlay;
import com.tonywww.dustandash.registry.DAAContainerMenus;
import com.tonywww.dustandash.registry.DAAEntities;
import com.tonywww.dustandash.registry.DAAItems;
import com.tonywww.dustandash.registry.DAAParticles;
import com.tonywww.dustandash.screen.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

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
            EntityRenderers.register(DAAEntities.LIGHT_STAFF.get(), LightStaffRenderer::new);
            CuriosRendererRegistry.register(
                    DAAItems.LIGHT_FORGED_HALO.get(), () -> new ForgedHaloRenderer(true));
            CuriosRendererRegistry.register(
                    DAAItems.DARK_FORGED_HALO.get(), () -> new ForgedHaloRenderer(false));
                CurioCooldownOverlayApi.register(
                    CurioCooldownManager.JUDGEMENT,
                    DAAItems.JUDGEMENT,
                    0xB0303030,
                    0xFFE6C84F);
                CurioCooldownOverlayApi.register(
                    CurioCooldownManager.LIGHT_FORGED_HALO,
                    DAAItems.LIGHT_FORGED_HALO,
                    0xB0303030,
                    0xFF70D7FF);
                CurioCooldownOverlayApi.register(
                    CurioCooldownManager.VOID_RING,
                    DAAItems.VOID_RING,
                    0xB0303030,
                    0xFFF2F2F2);
                    DAATooltipCatalog.registerAll();
        });
    }

    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(DAAParticles.DARK_SMOKE.get(), DarkSmokeParticle.Provider::new);
    }

    @SubscribeEvent
    public static void onRegisterTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(NeutronTooltipComponent.class, ClientNeutronTooltipComponent::new);
    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerBelowAll("dustandash.white_lightning", WhiteLightningOverlay.INSTANCE);
        event.registerAboveAll("dustandash.curio_cooldowns", CurioCooldownOverlay.INSTANCE);
    }
}