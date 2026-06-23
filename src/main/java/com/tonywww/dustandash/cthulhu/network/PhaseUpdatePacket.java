package com.tonywww.dustandash.cthulhu.network;

import com.tonywww.dustandash.cthulhu.client.ClientCthulhuPhaseState;
import com.tonywww.dustandash.cthulhu.fight.FightPhase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PhaseUpdatePacket {

    private final String phaseName;
    private final boolean clear;

    public PhaseUpdatePacket(FightPhase phase) {
        this.phaseName = phase.name();
        this.clear = false;
    }

    public PhaseUpdatePacket(boolean clear) {
        this.phaseName = "";
        this.clear = clear;
    }

    public PhaseUpdatePacket(FriendlyByteBuf buffer) {
        this.phaseName = buffer.readUtf(32);
        this.clear = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(phaseName, 32);
        buffer.writeBoolean(clear);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (clear) {
                ClientCthulhuPhaseState.clear();
                return;
            }
            try {
                ClientCthulhuPhaseState.setPhase(FightPhase.byName(phaseName));
            } catch (IllegalArgumentException exception) {
                ClientCthulhuPhaseState.clear();
            }
        });
        context.get().setPacketHandled(true);
    }
}
