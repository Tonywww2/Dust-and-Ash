package com.tonywww.dustandash.platform;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.client.particle.DarkSmokeParticle;
import com.tonywww.dustandash.client.render.ForgedHaloRenderer;
import com.tonywww.dustandash.client.render.LightStaffRenderer;
import com.tonywww.dustandash.client.cooldown.CurioCooldownOverlayApi;
import com.tonywww.dustandash.client.tooltip.ClientNeutronTooltipComponent;
import com.tonywww.dustandash.client.tooltip.DAATooltipCatalog;
import com.tonywww.dustandash.client.tooltip.NeutronTooltipComponent;
import com.tonywww.dustandash.cooldown.CurioCooldownManager;
import com.tonywww.dustandash.platform.CurioCooldownOverlay;
import com.tonywww.dustandash.platform.WhiteLightningOverlay;
import com.tonywww.dustandash.registry.DAAContainerMenus;
import com.tonywww.dustandash.registry.DAAEntities;
import com.tonywww.dustandash.registry.DAAItems;
import com.tonywww.dustandash.registry.DAAParticles;
import com.tonywww.dustandash.screen.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
//? if forge {
import net.minecraftforge.api.distmarker.Dist;
//?} else {
/*import net.neoforged.api.distmarker.Dist;
*///?}

//? if forge {
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
//?} else {
/*import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
*///?}

//? if forge {
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
//?} else {
/*import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
*///?}

//? if forge {
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
//?} else {
/*import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
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

//? if forge {
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
//?} else {
/*import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
*///?}

import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

//? if forge {
@Mod.EventBusSubscriber(modid = DustAndAsh.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
//?} else {
/*@net.neoforged.fml.common.EventBusSubscriber(modid = DustAndAsh.MOD_ID, value = Dist.CLIENT, bus = net.neoforged.fml.common.EventBusSubscriber.Bus.MOD)
*///?}

public final class ClientBootstrap {
    private ClientBootstrap() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            //? if forge {
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
            //?}
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

    //? if forge {
    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerBelowAll("dustandash.white_lightning", (gui, graphics, tick, width, height) -> WhiteLightningOverlay.INSTANCE.render(graphics, tick, width, height));
        event.registerAboveAll("dustandash.curio_cooldowns", (gui, graphics, tick, width, height) -> CurioCooldownOverlay.INSTANCE.render(graphics, tick, width, height));
    }
    //?} else {
    /*@SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiLayersEvent event) {
        event.registerBelowAll(PlatformIdentifier.id(DustAndAsh.MOD_ID, "white_lightning"), (graphics, tick) -> WhiteLightningOverlay.INSTANCE.render(graphics, tick.getGameTimeDeltaPartialTick(false), graphics.guiWidth(), graphics.guiHeight()));
        event.registerAboveAll(PlatformIdentifier.id(DustAndAsh.MOD_ID, "curio_cooldowns"), (graphics, tick) -> CurioCooldownOverlay.INSTANCE.render(graphics, tick.getGameTimeDeltaPartialTick(false), graphics.guiWidth(), graphics.guiHeight()));
    }
    @SubscribeEvent
    public static void onRegisterMenuScreens(net.neoforged.neoforge.client.event.RegisterMenuScreensEvent event) {
            event.register(DAAContainerMenus.INTEGRATED_BLOCK_CONTAINER.get(), IntegratedBlockScreen::new);
            event.register(DAAContainerMenus.ASH_COLLECTOR_CONTAINER.get(), AshCollectorScreen::new);
            event.register(DAAContainerMenus.MILLING_MACHINE_CONTAINER.get(), MillingMachineScreen::new);
            event.register(DAAContainerMenus.CENTRIFUGE_CONTAINER.get(), CentrifugeScreen::new);
            event.register(DAAContainerMenus.IONIZER_CONTAINER.get(), IonizerScreen::new);
            event.register(DAAContainerMenus.ITEM_SENDER_CONTAINER.get(), ItemSenderScreen::new);
            event.register(DAAContainerMenus.FISSION_REACTOR_CONTROLLER_CONTAINER.get(),
                    FissionReactorControllerScreen::new);
            event.register(DAAContainerMenus.FISSION_REACTOR_INTERFACE_CONTAINER.get(),
                    FissionReactorInterfaceScreen::new);
    }
    @SubscribeEvent
    public static void onRegisterClientExtensions(net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent event) {
        ((PlatformGaleOtaijutsu) DAAItems.GALE_OTAIJUTSU.get()).provideClientExtensions(extensions -> event.registerItem(extensions, DAAItems.GALE_OTAIJUTSU.get()));
        ((PlatformLordOfBlood) DAAItems.LORD_OF_BLOOD.get()).provideClientExtensions(extensions -> event.registerItem(extensions, DAAItems.LORD_OF_BLOOD.get()));
    }
    *///?}
}
