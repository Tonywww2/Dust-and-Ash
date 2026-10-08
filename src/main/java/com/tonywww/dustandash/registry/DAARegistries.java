package com.tonywww.dustandash.registry;

import com.tonywww.dustandash.platform.PlatformEventBus;

public final class DAARegistries {
    private DAARegistries() {
    }

    public static void registerAll(PlatformEventBus eventBus) {
        com.tonywww.dustandash.platform.PlatformEquipment.register(eventBus);
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
