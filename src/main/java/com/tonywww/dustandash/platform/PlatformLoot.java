package com.tonywww.dustandash.platform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootTable;
public final class PlatformLoot {
    private PlatformLoot() {}
    public static LootTable table(ServerLevel level, ResourceLocation id) {
        //? if forge {
        return level.getServer().getLootData().getLootTable(id);
        //?} else {
        /*return level.getServer().reloadableRegistries().getLootTable(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.LOOT_TABLE, id));
        *///?}
    }
}
