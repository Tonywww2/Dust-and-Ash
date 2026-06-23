package com.tonywww.dustandash.network;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.cthulhu.network.GraphemeUpdatePacket;
import com.tonywww.dustandash.cthulhu.network.HintMessagePacket;
import com.tonywww.dustandash.cthulhu.network.CthulhuRenderModePacket;
import com.tonywww.dustandash.cthulhu.network.PhaseUpdatePacket;
import com.tonywww.dustandash.cthulhu.network.WordSpellPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static int packetId;

    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(
            DustAndAsh.prefix("main"))
            .serverAcceptedVersions(s -> true)
            .clientAcceptedVersions(s -> true)
            .networkProtocolVersion(()-> NetworkConstants.NETVERSION)
            .simpleChannel();


    public static void register() {
        INSTANCE.messageBuilder(ItemSenderSavePacket.class, nextPacketId(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(ItemSenderSavePacket::encode)
                .decoder(ItemSenderSavePacket::new)
                .consumerMainThread(ItemSenderSavePacket::handle)
                .add();

        INSTANCE.messageBuilder(GraphemeUpdatePacket.class, nextPacketId(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(GraphemeUpdatePacket::encode)
                .decoder(GraphemeUpdatePacket::new)
                .consumerMainThread(GraphemeUpdatePacket::handle)
                .add();

        INSTANCE.messageBuilder(WordSpellPacket.class, nextPacketId(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(WordSpellPacket::encode)
                .decoder(WordSpellPacket::new)
                .consumerMainThread(WordSpellPacket::handle)
                .add();

        INSTANCE.messageBuilder(HintMessagePacket.class, nextPacketId(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(HintMessagePacket::encode)
                .decoder(HintMessagePacket::new)
                .consumerMainThread(HintMessagePacket::handle)
                .add();

        INSTANCE.messageBuilder(CthulhuRenderModePacket.class, nextPacketId(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CthulhuRenderModePacket::encode)
                .decoder(CthulhuRenderModePacket::new)
                .consumerMainThread(CthulhuRenderModePacket::handle)
                .add();

        INSTANCE.messageBuilder(PhaseUpdatePacket.class, nextPacketId(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(PhaseUpdatePacket::encode)
                .decoder(PhaseUpdatePacket::new)
                .consumerMainThread(PhaseUpdatePacket::handle)
                .add();


    }

    public static void sendToServer(Object msg) {
        INSTANCE.send(PacketDistributor.SERVER.noArg(), msg);

    }

    public static void sendToPlayer(ServerPlayer player, Object msg) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);

    }

    public static void sendToTrackingEntityAndSelf(Entity entity, Object msg) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), msg);

    }

    private static int nextPacketId() {
        return packetId++;

    }

}
