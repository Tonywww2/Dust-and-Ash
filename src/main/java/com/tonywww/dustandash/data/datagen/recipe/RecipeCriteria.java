package com.tonywww.dustandash.data.datagen.recipe;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

/**
 * Standalone "unlockedBy" criterion helpers, mirroring {@code RecipeProvider#has(...)} without
 * needing access to its protected members (avoids a split-package with net.minecraft at runtime).
 */
public final class RecipeCriteria {
    private RecipeCriteria() {
    }

    public static InventoryChangeTrigger.TriggerInstance has(ItemLike item) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(item);
    }

    public static InventoryChangeTrigger.TriggerInstance has(TagKey<Item> tag) {
        return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
    }
}
