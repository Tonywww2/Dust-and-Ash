package com.tonywww.dustandash.platform;

//? if forge {
import net.minecraftforge.eventbus.api.IEventBus;
//?} else {
/*import net.neoforged.bus.api.IEventBus;
*///?}

public record PlatformEventBus(IEventBus nativeBus) {}
