package com.tonywww.dustandash.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
public final class PlatformMenus {
    private PlatformMenus() {}
    public static void open(ServerPlayer player, MenuProvider provider, BlockPos pos) {
        //? if forge {
        net.minecraftforge.network.NetworkHooks.openScreen(player, provider, pos);
        //?} else {
        /*player.openMenu(provider, pos);
        *///?}
    }
}
