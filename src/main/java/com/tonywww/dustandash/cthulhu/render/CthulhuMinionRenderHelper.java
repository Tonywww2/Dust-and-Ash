package com.tonywww.dustandash.cthulhu.render;

import com.tonywww.dustandash.cthulhu.client.CthulhuRenderMode;
import com.tonywww.dustandash.cthulhu.network.CthulhuRenderModePacket;
import com.tonywww.dustandash.network.PacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public final class CthulhuMinionRenderHelper {

    private CthulhuMinionRenderHelper() {
    }

    public static void setRenderMode(LivingEntity entity, CthulhuRenderMode mode) {
        entity.getPersistentData().putString(CthulhuRenderMode.PERSISTENT_DATA_KEY, mode.serializedName());
        syncRenderMode(entity);
    }

    public static void syncRenderMode(LivingEntity entity) {
        Optional<CthulhuRenderMode> mode = CthulhuRenderMode.fromEntity(entity);
        mode.ifPresent(renderMode -> PacketHandler.sendToTrackingEntityAndSelf(
                entity,
                new CthulhuRenderModePacket(entity.getId(), renderMode.serializedName(), false)
        ));
    }

    public static void syncRenderMode(ServerPlayer player, LivingEntity entity) {
        Optional<CthulhuRenderMode> mode = CthulhuRenderMode.fromEntity(entity);
        mode.ifPresent(renderMode -> PacketHandler.sendToPlayer(
                player,
                new CthulhuRenderModePacket(entity.getId(), renderMode.serializedName(), false)
        ));
    }

    public static void clearRenderMode(ServerPlayer player, LivingEntity entity) {
        PacketHandler.sendToPlayer(player, new CthulhuRenderModePacket(entity.getId(), "", true));
    }
}
