package com.tonywww.dustandash.block.network;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(
            new ResourceLocation(DustAndAsh.MOD_ID, "main"))
            .serverAcceptedVersions(s -> true)
            .clientAcceptedVersions(s -> true)
            .networkProtocolVersion(()-> NetworkConstants.NETVERSION)
            .simpleChannel();


    public static void register() {
        INSTANCE.messageBuilder(ItemSenderSavePacket.class, NetworkDirection.PLAY_TO_SERVER.ordinal())
                .encoder(ItemSenderSavePacket::encode)
                .decoder(ItemSenderSavePacket::new)
                .consumerMainThread(ItemSenderSavePacket::handle)
                .add();


    }

    public static void sendToServer(Object msg) {
        INSTANCE.send(PacketDistributor.SERVER.noArg(), msg);

    }

}
