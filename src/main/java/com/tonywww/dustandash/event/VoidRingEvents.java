package com.tonywww.dustandash.event;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.DustAndAshConfig;
import com.tonywww.dustandash.cooldown.CurioCooldownManager;
import com.tonywww.dustandash.game.TemporaryGameModeController;
import com.tonywww.dustandash.registry.DAAItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = DustAndAsh.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class VoidRingEvents {
    private VoidRingEvents() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || !isVoidRingEquipped(player)
                || CurioCooldownManager.isOnCooldown(player, CurioCooldownManager.VOID_RING)) {
            return;
        }

        event.setCanceled(true);
        float restoredHealth = player.getMaxHealth()
                * DustAndAshConfig.CURIOS.voidRingHealthRestoreMultiplier.get().floatValue();
        player.setHealth(Math.max(1f, Math.min(player.getMaxHealth(), restoredHealth)));
        player.removeAllEffects();
        player.getFoodData().setFoodLevel(DustAndAshConfig.CURIOS.voidRingFoodLevel.get());
        player.getFoodData().setSaturation(
                DustAndAshConfig.CURIOS.voidRingSaturationLevel.get().floatValue());

        int cooldown = DustAndAshConfig.CURIOS.voidRingCooldownTicks.get();
        CurioCooldownManager.start(player, CurioCooldownManager.VOID_RING, cooldown);
        player.getCooldowns().addCooldown(DAAItems.VOID_RING.get(), cooldown);
        TemporaryGameModeController.enterSpectator(
                player,
                DustAndAshConfig.CURIOS.voidRingSpectatorDurationTicks.get());
    }

    public static boolean isVoidRingEquipped(ServerPlayer player) {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.isEquipped(DAAItems.VOID_RING.get()))
                .orElse(false);
    }

        public static boolean bypassesHaloBrightness(ServerPlayer player) {
                return DustAndAshConfig.CURIOS.voidRingBypassesHaloBrightness.get()
                                && isVoidRingEquipped(player);
        }
}