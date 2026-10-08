package com.tonywww.dustandash.platform;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
public class PlatformCurioItem extends PlatformItem implements ICurioItem {
    public PlatformCurioItem(Properties properties) { super(properties); }
    public enum Stat { ATTACK_DAMAGE, ATTACK_SPEED, MAX_HEALTH, ARMOR }
    protected void addModifiers(ModifierSink sink) {}
    public interface ModifierSink { void add(String name, Stat stat, double amount, boolean multiplyTotal); }
//? if forge {
@Override public Multimap<Attribute,AttributeModifier> getAttributeModifiers(SlotContext context, java.util.UUID slotId, ItemStack stack) {
    Multimap<Attribute,AttributeModifier> modifiers=HashMultimap.create();
    addModifiers((name,stat,amount,total)-> {
        Attribute attribute=switch(stat) { case ATTACK_DAMAGE -> Attributes.ATTACK_DAMAGE; case ATTACK_SPEED -> Attributes.ATTACK_SPEED; case MAX_HEALTH -> Attributes.MAX_HEALTH; case ARMOR -> Attributes.ARMOR; };
        var id=java.util.UUID.nameUUIDFromBytes((slotId+":"+PlatformRegistries.ITEMS.getKey(this).getPath()+":"+name).getBytes(java.nio.charset.StandardCharsets.UTF_8));
        modifiers.put(attribute,new AttributeModifier(id,name,amount,total ? AttributeModifier.Operation.MULTIPLY_TOTAL : AttributeModifier.Operation.ADDITION));
    });
    return modifiers;
}
//?} else {
/*@Override public Multimap<net.minecraft.core.Holder<Attribute>,AttributeModifier> getAttributeModifiers(SlotContext context, net.minecraft.resources.ResourceLocation slotId, ItemStack stack) {
    Multimap<net.minecraft.core.Holder<Attribute>,AttributeModifier> modifiers=HashMultimap.create();
    addModifiers((name,stat,amount,total)-> {
        var attribute=switch(stat) { case ATTACK_DAMAGE -> Attributes.ATTACK_DAMAGE; case ATTACK_SPEED -> Attributes.ATTACK_SPEED; case MAX_HEALTH -> Attributes.MAX_HEALTH; case ARMOR -> Attributes.ARMOR; };
        var id=PlatformIdentifier.id("dustandash",slotId.getNamespace()+"/"+slotId.getPath()+"/"+PlatformRegistries.ITEMS.getKey(this).getPath()+"/"+name);
        modifiers.put(attribute,new AttributeModifier(id,amount,total ? AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL : AttributeModifier.Operation.ADD_VALUE));
    });
    return modifiers;
}
*///?}
}
