package com.tonywww.dustandash.data.datagen;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.data.datagen.advancement.ModAdvancementProvider;
import com.tonywww.dustandash.data.datagen.loot.ModLootTableProvider;
import com.tonywww.dustandash.data.datagen.recipe.ModRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(modid = DustAndAsh.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators {
    private DataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        ModBlockTagsProvider blockTags = new ModBlockTagsProvider(output, event.getLookupProvider(), event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(),
                new ModItemTagsProvider(output, event.getLookupProvider(), blockTags.contentsGetter(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new ModLootTableProvider(output));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output));
        generator.addProvider(event.includeServer(), new ModAdvancementProvider(output, event.getLookupProvider()));
    }
}
