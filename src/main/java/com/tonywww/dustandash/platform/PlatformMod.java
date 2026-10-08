package com.tonywww.dustandash.platform;

import com.tonywww.dustandash.DustAndAsh;
//? if forge {
import net.minecraftforge.fml.common.Mod;
//?} else {
/*import net.neoforged.fml.common.Mod;
*///?}

@Mod(DustAndAsh.MOD_ID)
public final class PlatformMod {
//? if forge {
public PlatformMod() {
    CommonBootstrap.initialize(new PlatformEventBus(net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus()));
}
//?} else {
/*static net.neoforged.fml.ModContainer container;
public PlatformMod(net.neoforged.bus.api.IEventBus bus, net.neoforged.fml.ModContainer modContainer) {
    container=modContainer;
    PlatformNbtIngredient.register(new PlatformEventBus(bus));
    CommonBootstrap.initialize(new PlatformEventBus(bus));
    bus.addListener(PlatformCapabilityRegistration::register);
    bus.addListener(PacketHandler::registerPayloads);
}
*///?}
}
