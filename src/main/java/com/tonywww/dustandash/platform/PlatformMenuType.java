package com.tonywww.dustandash.platform;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
public final class PlatformMenuType {
    private PlatformMenuType() {}
    public interface Factory<T extends AbstractContainerMenu> { T create(int id, Inventory inventory, FriendlyByteBuf buffer); }
    public static <T extends AbstractContainerMenu> MenuType<T> create(Factory<T> factory) {
//? if forge {
return net.minecraftforge.common.extensions.IForgeMenuType.create(factory::create);
//?} else {
/*return net.neoforged.neoforge.common.extensions.IMenuTypeExtension.create(factory::create);
*///?}
    }
}
