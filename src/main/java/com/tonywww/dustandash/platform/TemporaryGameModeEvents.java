package com.tonywww.dustandash.platform;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.cooldown.CurioCooldownManager;
import com.tonywww.dustandash.game.TemporaryGameModeController;
import com.tonywww.dustandash.platform.ImbaModeSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
//? if forge {
import net.minecraftforge.event.TickEvent;
//?} else {
/*import net.neoforged.neoforge.event.tick.PlayerTickEvent;
*///?}

//? if forge {
import net.minecraftforge.event.entity.player.PlayerEvent;
//?} else {
/*import net.neoforged.neoforge.event.entity.player.PlayerEvent;
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
@Mod.EventBusSubscriber(modid = DustAndAsh.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
//?} else {
/*@net.neoforged.fml.common.EventBusSubscriber(modid = DustAndAsh.MOD_ID, bus = net.neoforged.fml.common.EventBusSubscriber.Bus.GAME)
*///?}

public final class TemporaryGameModeEvents {
    private TemporaryGameModeEvents() {
    }

    //? if forge {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayer player) {
            TemporaryGameModeController.reconcile(player);
        }
    }

    //?} else {
    /*@SubscribeEvent public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) TemporaryGameModeController.reconcile(player);
    }
    *///?}
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        reconcile(event);
        syncCooldowns(event);
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        reconcile(event);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        reconcile(event);
        syncCooldowns(event);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        reconcile(event);
        syncCooldowns(event);
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        TemporaryGameModeController.copyState(event.getOriginal(), event.getEntity());
        CurioCooldownManager.copyState(event.getOriginal(), event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerChangeGameMode(PlayerEvent.PlayerChangeGameModeEvent event) {
        if (event.getEntity() instanceof ServerPlayer
                && TemporaryGameModeController.isActive(event.getEntity())
                && event.getNewGameMode() != GameType.SPECTATOR) {
            event.setCanceled(true);
        }
    }

    private static void reconcile(PlayerEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            TemporaryGameModeController.reconcile(player);
        }
    }

    private static void syncCooldowns(PlayerEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CurioCooldownManager.sync(player);
            ImbaModeSyncPacket.send(player);
        }
    }
}
