package com.tonywww.dustandash.network;

import com.tonywww.dustandash.client.cooldown.ClientCurioCooldowns;
import com.tonywww.dustandash.cooldown.CurioCooldownManager.CooldownSnapshot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class CurioCooldownSyncPacket {
    private static final int MAX_COOLDOWNS = 64;
    private final List<CooldownSnapshot> cooldowns;

    public CurioCooldownSyncPacket(List<CooldownSnapshot> cooldowns) {
        this.cooldowns = List.copyOf(cooldowns);
    }

    public CurioCooldownSyncPacket(FriendlyByteBuf buffer) {
        int size = buffer.readVarInt();
        if (size < 0 || size > MAX_COOLDOWNS) {
            throw new IllegalArgumentException("Invalid curio cooldown count: " + size);
        }
        List<CooldownSnapshot> decoded = new ArrayList<>(size);
        for (int index = 0; index < size; index++) {
            decoded.add(new CooldownSnapshot(
                    buffer.readResourceLocation(),
                    buffer.readVarInt(),
                    buffer.readVarInt()));
        }
        this.cooldowns = List.copyOf(decoded);
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.cooldowns.size());
        for (CooldownSnapshot cooldown : this.cooldowns) {
            buffer.writeResourceLocation(cooldown.id());
            buffer.writeVarInt(cooldown.remainingTicks());
            buffer.writeVarInt(cooldown.durationTicks());
        }
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(
                Dist.CLIENT,
                () -> () -> ClientCurioCooldowns.replace(this.cooldowns)));
        context.setPacketHandled(true);
    }

    public static void send(ServerPlayer player, List<CooldownSnapshot> cooldowns) {
        PacketHandler.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> player),
                new CurioCooldownSyncPacket(cooldowns));
    }
}