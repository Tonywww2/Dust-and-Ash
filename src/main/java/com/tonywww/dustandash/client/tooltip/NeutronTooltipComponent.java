package com.tonywww.dustandash.client.tooltip;

import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record NeutronTooltipComponent(int neutron, int maximum) implements TooltipComponent {
    public NeutronTooltipComponent {
        maximum = Math.max(1, maximum);
        neutron = Mth.clamp(neutron, 0, maximum);
    }

    public float progress() {
        return (float) this.neutron / this.maximum;
    }
}