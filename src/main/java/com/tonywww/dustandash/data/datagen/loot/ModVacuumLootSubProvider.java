package com.tonywww.dustandash.data.datagen.loot;

import com.tonywww.dustandash.loottables.ModLootTables;
import com.tonywww.dustandash.registry.DAAItems;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class ModVacuumLootSubProvider implements LootTableSubProvider {

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output) {
        output.accept(ModLootTables.HAND_VACUUM, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(2))
                        .add(LootItem.lootTableItem(DAAItems.METAL_DUST.get()).setWeight(2))
                        .add(LootItem.lootTableItem(DAAItems.ORDER_DUST.get()).setWeight(2))
                        .add(LootItem.lootTableItem(DAAItems.EARTH_DUST.get()).setWeight(2))
                        .add(EmptyLootItem.emptyItem().setWeight(1))));

        output.accept(ModLootTables.IRON_VACUUM, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(5))
                        .add(LootItem.lootTableItem(DAAItems.METAL_DUST.get()).setWeight(3))
                        .add(LootItem.lootTableItem(DAAItems.ORDER_DUST.get()).setWeight(2))
                        .add(LootItem.lootTableItem(DAAItems.EARTH_DUST.get()).setWeight(2))
                        .add(LootItem.lootTableItem(DAAItems.LIFE_DUST.get()).setWeight(1))
                        .add(LootItem.lootTableItem(DAAItems.FIRE_DUST.get()).setWeight(1))
                        .add(EmptyLootItem.emptyItem().setWeight(1))));
    }
}
