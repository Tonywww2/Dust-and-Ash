package com.tonywww.dustandash.cthulhu.network;

import com.tonywww.dustandash.cthulhu.client.ClientHintManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class HintMessagePacket {

    private final Component component;
    private final int durationTicks;

    public HintMessagePacket(Component component, int durationTicks) {
        this.component = component;
        this.durationTicks = durationTicks;
    }

    public HintMessagePacket(FriendlyByteBuf buffer) {
        this(buffer.readComponent(), buffer.readVarInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeComponent(component);
        buffer.writeVarInt(durationTicks);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> ClientHintManager.add(component, durationTicks));
        context.get().setPacketHandled(true);
    }
}
