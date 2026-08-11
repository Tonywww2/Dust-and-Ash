package com.tonywww.dustandash.data.datagen.advancement;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModAdvancementProvider extends AdvancementProvider {
    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, List.of(new ModAdvancements()));
    }
}