package com.tonywww.dustandash.util;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModTags {

    public static class Blocks{

        public static final Tags.IOptionalNamedTag<Block> DUST_ABLE = createTag("dust_able");
        public static final Tags.IOptionalNamedTag<Block> NOT_DUST_ABLE = createTag("not_dust_able");

        private static Tags.IOptionalNamedTag<Block> createTag(String name){

            return BlockTags.createOptional(new ResourceLocation(DustAndAsh.MOD_ID, name));
        }

        private static Tags.IOptionalNamedTag<Block> createForgeTag(String name){

            return BlockTags.createOptional(new ResourceLocation("forge", name));
        }

    }

    public static class Items{

        public static final Tags.IOptionalNamedTag<Item> CRAFT_MATERIAL = createTag("craft_material");
        public static final Tags.IOptionalNamedTag<Item> TRIGGER_MATERIAL = createTag("trigger_material");
        public static final Tags.IOptionalNamedTag<Item> MILLING_INLAY = createTag("milling_inlay");

        private static Tags.IOptionalNamedTag<Item> createTag(String name){

            return ItemTags.createOptional(new ResourceLocation(DustAndAsh.MOD_ID, name));
        }

        private static Tags.IOptionalNamedTag<Item> createForgeTag(String name){

            return ItemTags.createOptional(new ResourceLocation("forge", name));
        }

    }

}
