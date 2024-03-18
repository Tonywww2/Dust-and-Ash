package com.tonywww.dustandash.item.custom;

import com.mojang.serialization.Codec;
import com.tonywww.dustandash.util.ModTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class NanoMachine extends Item {
    public NanoMachine(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            if (hand == InteractionHand.MAIN_HAND) {
                ItemStack nano = player.getItemInHand(InteractionHand.MAIN_HAND);
                ItemStack stack = player.getItemInHand(InteractionHand.OFF_HAND);
                String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).toString();

                if (!stack.isEmpty() & stack.getCount() > 0 && nano.getCount() > 0) {
                    switch (name) {
                        case "umapyoi:uma_factor_item":
                            CompoundTag compoundtag = stack.getOrCreateTag();
                            ListTag factors = compoundtag.getList("factors", Tag.TAG_COMPOUND);
                            int blueLV = factors.getCompound(0).getInt("level");
                            int redLV = factors.getCompound(1).getInt("level");

                            boolean flag = false;
                            if (blueLV < 5) {
                                factors.getCompound(0).putInt("level", blueLV + 1);
                                flag = true;

                            }
                            if (redLV < 3) {
                                factors.getCompound(1).putInt("level", redLV + 1);
                                flag = true;

                            }

                            if (flag) {
                                nano.setCount(nano.getCount() - 1);
                                player.sendSystemMessage(Component.literal("Agens Tachyon"));
                            }

                            break;

                        case "create:brass_casing":
                            exchange(player, stack, ForgeRegistries.ITEMS.getValue(new ResourceLocation("create:brass_block")).getDefaultInstance());
                            player.sendSystemMessage(Component.literal("Gears and Brass"));
                            break;

                        case "botania:mana_pool":
                            exchange(player, stack, ForgeRegistries.ITEMS.getValue(new ResourceLocation("botania:fabulous_pool")).getDefaultInstance());
                            player.sendSystemMessage(Component.literal("Dancing Plants"));
                            break;

                        default:
                            if (!stack.is(ModTags.Items.NANO_BLACKLIST)) {
                                stack.setDamageValue(0);
                                nano.setCount(nano.getCount() - 1);
                                player.sendSystemMessage(Component.literal("Item Fixed"));

                            }
                            break;
                    }
                }

            } else if (hand == InteractionHand.OFF_HAND) {
                player.startUsingItem(hand);
                
            }

        }


        return super.use(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide()) {
            if (stack.getCount() > 0) {
                entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 5));
                entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 1200, 2));

                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 1));
                entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000, 2));

                entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 12000, 0));
                entity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 12000, 0));
                entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 12000, 2));
                entity.addEffect(new MobEffectInstance(MobEffects.JUMP, 12000, 1));

                stack.setCount(stack.getCount() - 1);
            }


        }

        return super.finishUsingItem(stack, level, entity);
    }

    private void exchange(Player player, ItemStack input, ItemStack output) {
        if (input.getCount() > 0) {
            player.addItem(output);
            input.setCount(input.getCount() - 1);

        }

    }

    @Override
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 80;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {

        pTooltip.add(Component.translatable("tooltip.dustandash.nano_machine"));

        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }
}
