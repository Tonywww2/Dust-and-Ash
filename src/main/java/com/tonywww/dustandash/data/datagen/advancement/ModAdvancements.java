package com.tonywww.dustandash.data.datagen.advancement;

import com.tonywww.dustandash.DustAndAsh;
import com.tonywww.dustandash.registry.DAABlocks;
import com.tonywww.dustandash.registry.DAAItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public final class ModAdvancements implements AdvancementSubProvider {
    private static final ResourceLocation BACKGROUND = id("textures/gui/advancements/backgrounds/dust_and_ash.png");

    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> output) {
        Advancement entry = Advancement.Builder.advancement()
                .display(DAAItems.ASH.get(), title("entry"), description("entry"), BACKGROUND,
                        FrameType.GOAL, true, true, false)
                .addCriterion("requirement", PlayerTrigger.TriggerInstance.tick())
                .save(output, id("entry").toString());

        Advancement dustSource = itemAdvancement(output, "dust_source", entry,
                DAABlocks.DUST_SOURCE.get(), FrameType.TASK);
        itemAdvancement(output, "mantle_mixture", dustSource, DAAItems.MANTLE_MIXTURE.get(), FrameType.TASK);

        Advancement sharpenFlint = itemAdvancement(output, "sharpen_flint", entry,
                DAAItems.SHARPEN_FLINT.get(), FrameType.TASK);
        Advancement bloodyFlint = itemAdvancement(output, "bloody_flint", sharpenFlint,
                DAAItems.BLOODY_FLINT.get(), FrameType.TASK);
        itemAdvancement(output, "flint_pickaxe", bloodyFlint, DAAItems.FLINT_PICKAXE.get(), FrameType.CHALLENGE);
        Advancement handVacuum = itemAdvancement(output, "hand_vacuum", bloodyFlint,
                DAAItems.HAND_VACUUM.get(), FrameType.TASK);
        Advancement energizedCobblestone = itemAdvancement(output, "energized_cobblestone", handVacuum,
                DAABlocks.ENERGIZED_COBBLESTONE.get(), FrameType.TASK);
        itemAdvancement(output, "iron_vacuum", handVacuum, DAAItems.IRON_VACUUM.get(), FrameType.CHALLENGE);

        Advancement integratedBlock = itemAdvancement(output, "integrated_block", energizedCobblestone,
                DAABlocks.INTEGRATED_BLOCK.get(), FrameType.GOAL);
        itemAdvancement(output, "ash_collector", integratedBlock, DAABlocks.ASH_COLLECTOR.get(), FrameType.GOAL);
        itemAdvancement(output, "milling_machine", integratedBlock,
                DAABlocks.MILLING_MACHINE.get(), FrameType.CHALLENGE);
        Advancement integratedFrame1 = itemAdvancement(output, "integrated_frame_1", integratedBlock,
                DAABlocks.INTEGRATED_FRAME_1.get(), FrameType.CHALLENGE);
        Advancement ashSteelIngot = itemAdvancement(output, "ash_steel_ingot", integratedFrame1,
                DAAItems.ASH_STEEL_INGOT.get(), FrameType.TASK);
        itemAdvancement(output, "reproduce_dust", integratedFrame1,
                DAAItems.REPRODUCE_DUST.get(), FrameType.TASK);
        itemAdvancement(output, "centrifuge", ashSteelIngot, DAABlocks.CENTRIFUGE.get(), FrameType.CHALLENGE);
        Advancement darkEnergyCollapser = itemAdvancement(output, "dark_energy_collapser", ashSteelIngot,
                DAAItems.DARK_ENERGY_COLLAPSER.get(), FrameType.TASK);
        itemAdvancement(output, "integrated_frame_2", ashSteelIngot,
                DAABlocks.INTEGRATED_FRAME_2.get(), FrameType.CHALLENGE);
        itemAdvancement(output, "integrated_frame_3", darkEnergyCollapser,
                DAABlocks.INTEGRATED_FRAME_3.get(), FrameType.CHALLENGE);
        Advancement ionizer = itemAdvancement(output, "ionizer", darkEnergyCollapser,
                DAABlocks.IONIZER.get(), FrameType.TASK);
        Advancement titaniumScrap = itemAdvancement(output, "titanium_scrap", ionizer,
                DAAItems.TITANIUM_SCRAP.get(), FrameType.CHALLENGE);
        itemAdvancement(output, "netherite_mud", titaniumScrap, DAABlocks.NETHERITE_MUD.get(), FrameType.TASK);
        itemAdvancement(output, "titanium_aluminum_alloy", titaniumScrap,
                DAAItems.TITANIUM_ALUMINUM_ALLOY.get(), FrameType.TASK);
        itemAdvancement(output, "titanium_tungsten_alloy", titaniumScrap,
                DAAItems.TITANIUM_TUNGSTEN_ALLOY.get(), FrameType.TASK);
    }

    private static Advancement itemAdvancement(Consumer<Advancement> output, String name, Advancement parent,
                                               ItemLike item, FrameType frame) {
        return Advancement.Builder.advancement()
                .parent(parent)
                .display(item, title(name), description(name), null, frame, true, true, false)
                .rewards(AdvancementRewards.Builder.experience(10))
                .addCriterion("requirement", InventoryChangeTrigger.TriggerInstance.hasItems(item))
                .save(output, id(name).toString());
    }

    private static Component title(String name) {
        return Component.translatable("dustandash.advancements." + name + ".title");
    }

    private static Component description(String name) {
        return Component.translatable("dustandash.advancements." + name + ".description");
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(DustAndAsh.MOD_ID, path);
    }
}