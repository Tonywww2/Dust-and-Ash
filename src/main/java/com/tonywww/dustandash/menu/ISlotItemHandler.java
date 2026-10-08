package com.tonywww.dustandash.menu;

import net.minecraft.world.entity.player.Player;
import com.tonywww.dustandash.platform.IItemHandler;
import com.tonywww.dustandash.platform.SlotItemHandler;

public class ISlotItemHandler extends SlotItemHandler {
    public ISlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPickup(Player player) {
        return true;
    }
}
