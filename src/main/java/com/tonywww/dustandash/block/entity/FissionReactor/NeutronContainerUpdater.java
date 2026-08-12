package com.tonywww.dustandash.block.entity.FissionReactor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.regex.Pattern;

public final class NeutronContainerUpdater {
    public static final String NEUTRON_TAG = "neutron";
    public static final int MAX_NEUTRON = 1280;

    private static final Pattern LEGACY_LORE = Pattern.compile(
            "\\{\\\"text\\\":\\\"Neutron: \\d+/1280\\\"}");

    private NeutronContainerUpdater() {
    }

    public static int getNeutron(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag == null ? 0 : Math.max(0, Math.min(MAX_NEUTRON, tag.getInt(NEUTRON_TAG)));
    }

    public static boolean hasNeutronData(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(NEUTRON_TAG, Tag.TAG_INT);
    }

    public static boolean isLegacyLoreText(String text) {
        return text.matches("Neutron: \\d+/" + MAX_NEUTRON);
    }

    public static int getLegacyLoreCount(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("display", Tag.TAG_COMPOUND)) {
            return 0;
        }

        Tag loreTag = tag.getCompound("display").get("Lore");
        if (!(loreTag instanceof ListTag lore)) {
            return 0;
        }

        int count = 0;
        for (Tag line : lore) {
            if (line instanceof StringTag && LEGACY_LORE.matcher(line.getAsString()).matches()) {
                count++;
            }
        }
        return count;
    }

    public static UpdateResult absorbOne(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        boolean changed = removeLegacyLore(tag);
        int stored = tag.getInt(NEUTRON_TAG);
        int current = getNeutron(stack);
        if (!tag.contains(NEUTRON_TAG, Tag.TAG_INT) || stored != current) {
            tag.putInt(NEUTRON_TAG, current);
            changed = true;
        }
        if (current >= MAX_NEUTRON) {
            return changed ? UpdateResult.MIGRATED : UpdateResult.NONE;
        }

        tag.putInt(NEUTRON_TAG, current + 1);
        return UpdateResult.ABSORBED;
    }

    private static boolean removeLegacyLore(CompoundTag tag) {
        if (!tag.contains("display", Tag.TAG_COMPOUND)) {
            return false;
        }

        CompoundTag display = tag.getCompound("display");
        Tag loreTag = display.get("Lore");
        if (!(loreTag instanceof ListTag lore)) {
            return false;
        }

        boolean changed = lore.removeIf(line -> line instanceof StringTag
            && LEGACY_LORE.matcher(line.getAsString()).matches());
        if (lore.isEmpty()) {
            display.remove("Lore");
        }
        if (display.isEmpty()) {
            tag.remove("display");
        }
        return changed;
    }

    public enum UpdateResult {
        NONE(false, false),
        MIGRATED(true, false),
        ABSORBED(true, true);

        private final boolean changed;
        private final boolean absorbed;

        UpdateResult(boolean changed, boolean absorbed) {
            this.changed = changed;
            this.absorbed = absorbed;
        }

        public boolean changed() {
            return this.changed;
        }

        public boolean absorbed() {
            return this.absorbed;
        }
    }
}