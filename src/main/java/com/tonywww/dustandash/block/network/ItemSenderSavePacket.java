package com.tonywww.dustandash.block.network;

import com.tonywww.dustandash.block.entity.ItemSenderEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ItemSenderSavePacket {

    private final BlockPos blockPos;
    private final byte[] targetSlots;

    public ItemSenderSavePacket(BlockPos blockPos, byte[] targetSlots) {
        this.blockPos = blockPos;
        this.targetSlots = targetSlots;

    }

    public ItemSenderSavePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readByteArray());

    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
        buffer.writeByteArray(this.targetSlots);

    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        BlockEntity block = context.get().getSender().level().getBlockEntity(this.blockPos);
        if (block instanceof ItemSenderEntity itemSender) {
            itemSender.setTargetSlots(this.targetSlots);

        } else {
            throw new IllegalStateException("Tile entity is not correct! " + this.blockPos);

        }


    }
}
