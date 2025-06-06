package net.blazinblaze.happyghastmod.slot;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.component.HappyGhastComponents;
import net.blazinblaze.happyghastmod.entity.custom.HappyGhast;
import net.blazinblaze.happyghastmod.item.HappyGhastItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;

import java.util.Objects;

public class HappyGhastUpgradeSlot extends Slot {

    Item itemStackType;

    public HappyGhastUpgradeSlot(Inventory inventory, int index, int x, int y, Item itemType) {
        super(inventory, index, x, y);
        this.itemStackType = itemType;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return itemStackType != null && stack.isOf(itemStackType);
    }

    @Override
    public void setStack(ItemStack stack) {
        if(stack.getComponents().contains(HappyGhastComponents.IS_IN_UPGRADE_SLOT)) {
            stack.set(HappyGhastComponents.IS_IN_UPGRADE_SLOT, true);
        }
        super.setStack(stack);
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        return false;
    }
}
