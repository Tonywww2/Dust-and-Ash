package com.tonywww.dustandash.network;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "3";

    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(
            new ResourceLocation(DustAndAsh.MOD_ID, "main"))
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();


    public static void register() {
        int messageId = 0;
        INSTANCE.messageBuilder(ItemSenderSavePacket.class, messageId++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ItemSenderSavePacket::encode)
                .decoder(ItemSenderSavePacket::new)
                .consumerMainThread(ItemSenderSavePacket::handle)
                .add();

        INSTANCE.messageBuilder(CurioCooldownSyncPacket.class, messageId++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(CurioCooldownSyncPacket::encode)
            .decoder(CurioCooldownSyncPacket::new)
            .consumerMainThread(CurioCooldownSyncPacket::handle)
            .add();

        INSTANCE.messageBuilder(ImbaModeSyncPacket.class, messageId++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(ImbaModeSyncPacket::encode)
            .decoder(ImbaModeSyncPacket::new)
            .consumerMainThread(ImbaModeSyncPacket::handle)
            .add();

    }

    public static void sendToServer(Object msg) {
        INSTANCE.send(PacketDistributor.SERVER.noArg(), msg);

    }

}
