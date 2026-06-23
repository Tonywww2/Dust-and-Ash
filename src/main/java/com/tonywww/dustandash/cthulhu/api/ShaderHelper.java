package com.tonywww.dustandash.cthulhu.api;

import com.tonywww.dustandash.cthulhu.fight.BossFightInstance;
import com.tonywww.dustandash.cthulhu.fight.FightPhase;
import com.tonywww.dustandash.cthulhu.network.PhaseUpdatePacket;
import com.tonywww.dustandash.network.PacketHandler;
import net.minecraft.server.level.ServerPlayer;

public final class ShaderHelper {

    private ShaderHelper() {
    }

    public static void applyPhaseShader(ServerPlayer player, FightPhase phase) {
        PacketHandler.sendToPlayer(player, new PhaseUpdatePacket(phase));
    }

    public static void broadcastPhaseShader(BossFightInstance instance, FightPhase phase) {
        for (ServerPlayer player : instance.level().players()) {
            if (instance.isParticipant(player.getUUID())) {
                applyPhaseShader(player, phase);
            }
        }
    }

    public static void clearShaders(ServerPlayer player) {
        PacketHandler.sendToPlayer(player, new PhaseUpdatePacket(true));
    }
}
