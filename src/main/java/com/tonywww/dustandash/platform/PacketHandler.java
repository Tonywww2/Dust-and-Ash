package com.tonywww.dustandash.platform;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.server.level.ServerPlayer;
//? if forge {
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;
//?} else {
/*import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.PacketDistributor;
*///?}

public final class PacketHandler {
    private static final String PROTOCOL_VERSION="3";
//? if forge {
public static final SimpleChannel INSTANCE=NetworkRegistry.ChannelBuilder.named(PlatformIdentifier.id(DustAndAsh.MOD_ID,"main"))
    .serverAcceptedVersions(PROTOCOL_VERSION::equals).clientAcceptedVersions(PROTOCOL_VERSION::equals).networkProtocolVersion(()->PROTOCOL_VERSION).simpleChannel();
public static void register() {
    INSTANCE.messageBuilder(ItemSenderSavePacket.class,0,NetworkDirection.PLAY_TO_SERVER).encoder(ItemSenderSavePacket::encode).decoder(ItemSenderSavePacket::new).consumerMainThread((packet,context)-> packet.handle(()->adapt(context.get()))).add();
    INSTANCE.messageBuilder(CurioCooldownSyncPacket.class,1,NetworkDirection.PLAY_TO_CLIENT).encoder(CurioCooldownSyncPacket::encode).decoder(CurioCooldownSyncPacket::new).consumerMainThread((packet,context)-> packet.handle(()->adapt(context.get()))).add();
    INSTANCE.messageBuilder(ImbaModeSyncPacket.class,2,NetworkDirection.PLAY_TO_CLIENT).encoder(ImbaModeSyncPacket::encode).decoder(ImbaModeSyncPacket::new).consumerMainThread((packet,context)-> packet.handle(()->adapt(context.get()))).add();
}
private static PlatformNetworkContext adapt(NetworkEvent.Context context) { return new PlatformNetworkContext(context.getSender(),context::enqueueWork,context::setPacketHandled); }
public static void sendToServer(Object message) { INSTANCE.send(PacketDistributor.SERVER.noArg(),message); }
public static void sendToPlayer(ServerPlayer player,Object message) { INSTANCE.send(PacketDistributor.PLAYER.with(()->player),message); }
//?} else {
/*public static void register() {}
public static void registerPayloads(RegisterPayloadHandlersEvent event) {
    var registrar=event.registrar(PROTOCOL_VERSION);
    registrar.playToServer(ServerPayload.TYPE,ServerPayload.CODEC,(payload,context)->payload.packet().handle(()->new PlatformNetworkContext(context.player() instanceof ServerPlayer player ? player : null, context::enqueueWork, ignored->{})));
    registrar.playToClient(ClientPayload.TYPE,ClientPayload.CODEC,(payload,context)-> {
        var adapted=new PlatformNetworkContext(null,context::enqueueWork,ignored->{});
        if (payload.packet() instanceof CurioCooldownSyncPacket packet) packet.handle(()->adapted);
        else if (payload.packet() instanceof ImbaModeSyncPacket packet) packet.handle(()->adapted);
    });
}
public static void sendToServer(Object message) { PacketDistributor.sendToServer(new ServerPayload((ItemSenderSavePacket)message)); }
public static void sendToPlayer(ServerPlayer player,Object message) { PacketDistributor.sendToPlayer(player,new ClientPayload(message)); }
public record ServerPayload(ItemSenderSavePacket packet) implements CustomPacketPayload {
    static final Type<ServerPayload> TYPE=new Type<>(PlatformIdentifier.id(DustAndAsh.MOD_ID,"settings"));
    static final StreamCodec<RegistryFriendlyByteBuf,ServerPayload> CODEC=StreamCodec.of((buf,payload)->payload.packet.encode(buf),buf->new ServerPayload(new ItemSenderSavePacket(buf)));
    public Type<ServerPayload> type() { return TYPE; }
}
public record ClientPayload(Object packet) implements CustomPacketPayload {
    static final Type<ClientPayload> TYPE=new Type<>(PlatformIdentifier.id(DustAndAsh.MOD_ID,"state"));
    static final StreamCodec<RegistryFriendlyByteBuf,ClientPayload> CODEC=StreamCodec.of((buf,payload)-> {
        if (payload.packet instanceof CurioCooldownSyncPacket packet) { buf.writeVarInt(0); packet.encode(buf); }
        else if (payload.packet instanceof ImbaModeSyncPacket packet) { buf.writeVarInt(1); packet.encode(buf); }
        else throw new IllegalArgumentException("Unsupported client packet");
    },buf->new ClientPayload(switch(buf.readVarInt()) { case 0 -> new CurioCooldownSyncPacket(buf); case 1 -> new ImbaModeSyncPacket(buf); default -> throw new IllegalArgumentException("Unknown client packet"); }));
    public Type<ClientPayload> type() { return TYPE; }
}
*///?}
}
