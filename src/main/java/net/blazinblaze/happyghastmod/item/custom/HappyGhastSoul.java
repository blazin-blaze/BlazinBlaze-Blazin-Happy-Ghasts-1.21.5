package net.blazinblaze.happyghastmod.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HappyGhastSoul extends Item {
    public HappyGhastSoul(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
