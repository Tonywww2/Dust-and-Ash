package com.tonywww.dustandash.util;

import com.tonywww.dustandash.DustAndAsh;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

public class ModTags {

    public static class Blocks{

        public static final TagKey<Block> DUST_ABLE = createTag("dust_able");
        public static final TagKey<Block> NOT_DUST_ABLE = createTag("not_dust_able");

        private static TagKey<Block> createTag(String name){

            return BlockTags.create(new ResourceLocation(DustAndAsh.MOD_ID, name));
        }

        private static TagKey<Block> createForgeTag(String name){

            return BlockTags.create(new ResourceLocation("forge", name));
        }

    }

    public static class Items{

        public static final TagKey<Item> CRAFT_MATERIAL = createTag("craft_material");
        public static final TagKey<Item> TRIGGER_MATERIAL = createTag("trigger_material");
        public static final TagKey<Item> MILLING_INLAY = createTag("milling_inlay");
        public static final TagKey<Item> CENTRIFUGE_CATALYST = createTag("centrifuge_catalyst");

        private static TagKey<Item> createTag(String name){

            return ItemTags.create(new ResourceLocation(DustAndAsh.MOD_ID, name));
        }

        private static TagKey<Item> createForgeTag(String name){

            return ItemTags.create(new ResourceLocation("forge", name));
        }

    }

}
