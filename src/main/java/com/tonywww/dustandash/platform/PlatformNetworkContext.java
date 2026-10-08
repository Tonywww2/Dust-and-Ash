package com.tonywww.dustandash.platform;

import net.minecraft.server.level.ServerPlayer;
import java.util.function.Consumer;
public record PlatformNetworkContext(ServerPlayer getSender, Consumer<Runnable> work, Consumer<Boolean> handled) {
    public ServerPlayer getSender() { return getSender; }
    public void enqueueWork(Runnable action) { work.accept(action); }
    public void setPacketHandled(boolean value) { handled.accept(value); }
}
