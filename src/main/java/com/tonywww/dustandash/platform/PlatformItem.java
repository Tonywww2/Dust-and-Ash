package com.tonywww.dustandash.platform;

public class PlatformItem extends net.minecraft.world.item.Item {
 public PlatformItem(Properties properties) { super(properties); }

    public void appendTooltip(net.minecraft.world.item.ItemStack stack, net.minecraft.world.level.Level level,
                              java.util.List<net.minecraft.network.chat.Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {}
//? if forge {
@Override public void appendHoverText(net.minecraft.world.item.ItemStack stack, net.minecraft.world.level.Level level, java.util.List<net.minecraft.network.chat.Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
    super.appendHoverText(stack,level,tooltip,flag); appendTooltip(stack,level,tooltip,flag);
}
//?} else {
/*@Override public void appendHoverText(net.minecraft.world.item.ItemStack stack, net.minecraft.world.item.Item.TooltipContext context, java.util.List<net.minecraft.network.chat.Component> tooltip, net.minecraft.world.item.TooltipFlag flag) {
    super.appendHoverText(stack,context,tooltip,flag); appendTooltip(stack,context.level(),tooltip,flag);
}
*///?}

    public int useDuration(net.minecraft.world.item.ItemStack stack) { return 0; }
//? if forge {
@Override public int getUseDuration(net.minecraft.world.item.ItemStack stack) { return useDuration(stack); }
//?} else {
/*@Override public int getUseDuration(net.minecraft.world.item.ItemStack stack, net.minecraft.world.entity.LivingEntity entity) { return useDuration(stack); }
*///?}
}
