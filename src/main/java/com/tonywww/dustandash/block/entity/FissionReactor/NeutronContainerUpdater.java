package com.tonywww.dustandash.block.entity.FissionReactor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;

public final class NeutronContainerUpdater {
    private NeutronContainerUpdater() {
    }

    public static boolean absorbOne(ItemStack stack, String neutronTag, int maximumNeutron) {
        CompoundTag tag = stack.getOrCreateTag();
        int current = tag.getInt(neutronTag);
        if (current >= maximumNeutron) {
            return false;
        }

        int updated = current + 1;
        tag.putInt(neutronTag, updated);

        ListTag lore = new ListTag();
        lore.add(StringTag.valueOf("{\"text\":\"Neutron: " + updated + '/' + maximumNeutron + "\"}"));
        CompoundTag display = tag.contains("display", CompoundTag.TAG_COMPOUND)
                ? tag.getCompound("display")
                : new CompoundTag();
        display.put("Lore", lore);
        tag.put("display", display);
        return true;
    }
}