package net.blazinblaze.happyghastmod.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GoldenSnowball extends Item {
    public GoldenSnowball(net.minecraft.item.Item.Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
