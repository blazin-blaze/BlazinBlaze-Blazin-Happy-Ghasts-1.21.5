package net.blazinblaze.happyghastmod.item.custom;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.component.HappyGhastComponents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HappyGhastUpgrade extends Item {
    public HappyGhastUpgrade(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        if(stack.getComponents().get(HappyGhastComponents.IS_IN_UPGRADE_SLOT) != null) {
            if(Boolean.TRUE.equals(stack.getComponents().get(HappyGhastComponents.IS_IN_UPGRADE_SLOT))) {
                return true;
            }
        }
        return super.hasGlint(stack);
    }
}
