package com.tonywww.dustandash.platform;

import com.tonywww.dustandash.platform.DustAndAshConfig;
import com.tonywww.dustandash.client.config.ClientImbaMode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;





import java.util.function.Supplier;

public final class ImbaModeSyncPacket {
    private final boolean enabled;

    public ImbaModeSyncPacket(boolean enabled) {
        this.enabled = enabled;
    }

    public ImbaModeSyncPacket(FriendlyByteBuf buffer) {
        this.enabled = buffer.readBoolean();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.enabled);
    }

    public void handle(Supplier<PlatformNetworkContext> contextSupplier) {
        PlatformNetworkContext context = contextSupplier.get();
        context.enqueueWork(() -> ClientImbaMode.update(this.enabled));
        context.setPacketHandled(true);
    }

    public static void send(ServerPlayer player) {
        PacketHandler.sendToPlayer(player, new ImbaModeSyncPacket(DustAndAshConfig.IMBA_MODE.get()));
    }
}
