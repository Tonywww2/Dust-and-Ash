package com.tonywww.dustandash.registry;

import net.minecraftforge.eventbus.api.IEventBus;

public final class DAARegistries {
    private DAARegistries() {
    }

    public static void registerAll(IEventBus eventBus) {
        DAABlocks.bootstrap();
        DAAItems.bootstrap();
        DAABlockItems.bootstrap();

        DAACreativeModTabs.register(eventBus);
        DAAItems.register(eventBus);
        DAABlocks.register(eventBus);
        DAABlockEntities.register(eventBus);
        DAAEntities.register(eventBus);
        DAAParticles.register(eventBus);
        DAAContainerMenus.register(eventBus);
        DAARecipe.register(eventBus);
    }
}