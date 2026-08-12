package com.tonywww.dustandash.network;

import com.tonywww.dustandash.DustAndAshConfig;
import com.tonywww.dustandash.client.config.ClientImbaMode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

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

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(
                Dist.CLIENT,
                () -> () -> ClientImbaMode.update(this.enabled)));
        context.setPacketHandled(true);
    }

    public static void send(ServerPlayer player) {
        PacketHandler.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> player),
                new ImbaModeSyncPacket(DustAndAshConfig.IMBA_MODE.get()));
    }
}