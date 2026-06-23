package com.tonywww.dustandash.cthulhu.network;

import com.tonywww.dustandash.cthulhu.client.CthulhuClientRenderState;
import com.tonywww.dustandash.cthulhu.client.CthulhuRenderMode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

public class CthulhuRenderModePacket {

    private final int entityId;
    private final String modeName;
    private final boolean remove;

    public CthulhuRenderModePacket(int entityId, String modeName, boolean remove) {
        this.entityId = entityId;
        this.modeName = modeName;
        this.remove = remove;
    }

    public CthulhuRenderModePacket(FriendlyByteBuf buffer) {
        this(buffer.readVarInt(), buffer.readUtf(32), buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(entityId);
        buffer.writeUtf(modeName, 32);
        buffer.writeBoolean(remove);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (remove) {
                CthulhuClientRenderState.removeMode(entityId);
                return;
            }

            Optional<CthulhuRenderMode> mode = CthulhuRenderMode.fromName(modeName);
            mode.ifPresent(renderMode -> CthulhuClientRenderState.setMode(entityId, renderMode));
        });
        context.get().setPacketHandled(true);
    }
}
