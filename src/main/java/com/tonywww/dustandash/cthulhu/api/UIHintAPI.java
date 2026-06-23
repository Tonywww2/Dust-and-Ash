package com.tonywww.dustandash.cthulhu.api;

import com.tonywww.dustandash.cthulhu.fight.BossFightInstance;
import com.tonywww.dustandash.cthulhu.network.HintMessagePacket;
import com.tonywww.dustandash.network.PacketHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class UIHintAPI {

    private UIHintAPI() {
    }

    public static void showHint(ServerPlayer player, Component component, int durationTicks) {
        PacketHandler.sendToPlayer(player, new HintMessagePacket(component, durationTicks));
        player.sendSystemMessage(component);
    }

    public static void broadcastHint(BossFightInstance instance, Component component, int durationTicks) {
        for (ServerPlayer player : instance.level().players()) {
            if (instance.isParticipant(player.getUUID())) {
                showHint(player, component, durationTicks);
            }
        }
    }
}
