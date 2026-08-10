package com.tonywww.dustandash.network;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.block.entity.ItemSenderEntity;
import com.tonywww.dustandash.menu.ItemSenderContainerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Arrays;
import java.util.function.Supplier;

public class ItemSenderSavePacket {

    private final BlockPos blockPos;
    private final byte[] targetSlots;

    public ItemSenderSavePacket(BlockPos blockPos, byte[] targetSlots) {
        this.blockPos = blockPos;
        this.targetSlots = Arrays.copyOf(targetSlots, targetSlots.length);

    }

    public ItemSenderSavePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readByteArray(ItemSenderEntity.TARGET_SLOT_COUNT));

    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
        buffer.writeByteArray(this.targetSlots);

    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context networkContext = context.get();
        ServerPlayer sender = networkContext.getSender();
        if (sender == null || this.targetSlots.length != ItemSenderEntity.TARGET_SLOT_COUNT) {
            DustAndAsh.getLogger().warn("Rejected malformed item sender settings packet");
            networkContext.setPacketHandled(true);
            return;
        }

        if (!(sender.containerMenu instanceof ItemSenderContainerMenu menu)
                || !menu.stillValid(sender)
                || !menu.getTileEntity().getBlockPos().equals(this.blockPos)) {
            DustAndAsh.getLogger().warn("Rejected item sender settings packet from {} for {}", sender.getGameProfile().getName(), this.blockPos);
            networkContext.setPacketHandled(true);
            return;
        }

        BlockEntity block = sender.level().getBlockEntity(this.blockPos);
        ItemSenderEntity itemSender = menu.getTileEntity();
        if (block == itemSender) {
            itemSender.setTargetSlots(this.targetSlots);
        }

        networkContext.setPacketHandled(true);

    }
}
