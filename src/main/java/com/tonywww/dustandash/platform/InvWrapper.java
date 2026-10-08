package com.tonywww.dustandash.platform;

//? if forge {
public class InvWrapper extends net.minecraftforge.items.wrapper.InvWrapper implements IItemHandler {
//?} else {
/*public class InvWrapper extends net.neoforged.neoforge.items.wrapper.InvWrapper implements IItemHandler {
*///?}

    public InvWrapper(net.minecraft.world.Container inventory) { super(inventory); }
}
