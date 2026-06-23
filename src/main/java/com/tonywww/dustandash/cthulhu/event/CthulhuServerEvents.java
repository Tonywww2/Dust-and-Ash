package com.tonywww.dustandash.cthulhu.event;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.cthulhu.api.HealthDrainAPI;
import com.tonywww.dustandash.cthulhu.api.ShaderHelper;
import com.tonywww.dustandash.cthulhu.config.CthulhuConfig;
import com.tonywww.dustandash.cthulhu.command.CthulhuCommand;
import com.tonywww.dustandash.cthulhu.fight.BossFightInstance;
import com.tonywww.dustandash.cthulhu.fight.BossFightManager;
import com.tonywww.dustandash.cthulhu.render.CthulhuMinionRenderHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DustAndAsh.MOD_ID)
public final class CthulhuServerEvents {

    private CthulhuServerEvents() {
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(CthulhuCommand.register());
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            BossFightManager.get().tick(event.getServer());
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        BossFightInstance instance = BossFightManager.get().getActiveFight((ServerLevel) player.level());
        if (instance != null) {
            if (instance.isBanished(player.getUUID())) {
                BossFightManager.get().clearClientLetters(player);
                BossFightManager.get().banishPlayer(player, instance);
            } else {
                instance.addParticipant(player);
            }
        } else {
            BossFightManager.get().clearClientLetters(player);
            ShaderHelper.clearShaders(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        BossFightInstance instance = BossFightManager.get().getActiveFight((ServerLevel) player.level());
        if (instance != null && !instance.isBanished(player.getUUID())) {
            instance.addParticipant(player);
        } else {
            BossFightManager.get().clearClientLetters(player);
            ShaderHelper.clearShaders(player);
        }
        HealthDrainAPI.reapply(player);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        BossFightInstance source = BossFightManager.get().findBanishSource(player);
        if (source != null && source.dimension().equals(player.level().dimension())) {
            BossFightManager.get().clearClientLetters(player);
            BossFightManager.get().banishPlayer(player, source);
        } else if (BossFightManager.get().getActiveFight((ServerLevel) player.level()) == null) {
            BossFightManager.get().clearClientLetters(player);
            ShaderHelper.clearShaders(player);
        }
        HealthDrainAPI.reapply(player);
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getOriginal() instanceof ServerPlayer oldPlayer && event.getEntity() instanceof ServerPlayer newPlayer) {
            HealthDrainAPI.copyPersistentDrain(oldPlayer, newPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            HealthDrainAPI.recoverSoulWither(player, CthulhuConfig.SOUL_RECOVERY_PER_SLEEP);
        }
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getTarget() instanceof LivingEntity livingEntity) {
            CthulhuMinionRenderHelper.syncRenderMode(player, livingEntity);
        }
    }

    @SubscribeEvent
    public static void onPlayerStopTracking(PlayerEvent.StopTracking event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getTarget() instanceof LivingEntity livingEntity) {
            CthulhuMinionRenderHelper.clearRenderMode(player, livingEntity);
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        int deaths = BossFightManager.get().recordDeath(player);
        if (deaths > 0) {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Azathoth deaths: " + deaths));
        }
    }

    @SubscribeEvent
    public static void onDimensionTravel(EntityTravelToDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (BossFightManager.get().isBlockedFromDimension(player, event.getDimension())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!CthulhuConfig.LOCK_BLOCK_INTERACTIONS_DURING_FIGHT || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        BossFightInstance instance = BossFightManager.get().getActiveFight(level);
        if (instance == null) {
            return;
        }

        event.setCanceled(true);
        if (event.getPlayer() instanceof ServerPlayer player) {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Azathoth rejects block breaking during the fight."));
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!CthulhuConfig.LOCK_BLOCK_INTERACTIONS_DURING_FIGHT || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        BossFightInstance instance = BossFightManager.get().getActiveFight(level);
        if (instance == null) {
            return;
        }

        event.setCanceled(true);
        if (event.getEntity() instanceof ServerPlayer player) {
            player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Azathoth rejects block placing during the fight."));
        }
    }
}
