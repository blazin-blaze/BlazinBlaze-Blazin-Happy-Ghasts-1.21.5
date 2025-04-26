package net.blazinblaze.happyghastmod.screen;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.item.HappyGhastItems;
import net.blazinblaze.happyghastmod.slot.HappyGhastUpgradeSlot;
import net.blazinblaze.happyghastmod.sound.HappyGhastSounds;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;

public class HappyGhastUpgradeScreenHandler extends ScreenHandler {
    public static final int SLOT_COUNT = 5;
    private final Inventory inventory;
    private final ArrayList<Item> itemTypes = new ArrayList<Item>();

    public HappyGhastUpgradeScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(5));
    }

    public HappyGhastUpgradeScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(HappyGhastMod.GHAST_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        checkSize(inventory, 5);
        inventory.onOpen(playerInventory.player);

        itemTypes.add(HappyGhastItems.HAPPY_GHAST_SPEED_UPGRADE);
        itemTypes.add(HappyGhastItems.HAPPY_GHAST_STRENGTH_UPGRADE);
        itemTypes.add(HappyGhastItems.HAPPY_GHAST_HEART_UPGRADE);
        itemTypes.add(HappyGhastItems.HAPPY_GHAST_FIREBALL_UPGRADE);
        itemTypes.add(HappyGhastItems.HAPPY_GHAST_ROYALTY_UPGRADE);

        for(int i = 0; i < 5; ++i) {
            this.addSlot(new HappyGhastUpgradeSlot(inventory, i, 44 + i * 18, 20, itemTypes.get(i)));
        }

        this.addPlayerSlots(playerInventory, 8, 51);
    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot2 = (Slot)this.slots.get(slot);
        if (slot2 != null && slot2.hasStack()) {
            ItemStack itemStack2 = slot2.getStack();
            itemStack = itemStack2.copy();
            if (slot < this.inventory.size()) {
                if (!this.insertItem(itemStack2, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot2.setStack(ItemStack.EMPTY);
            } else {
                slot2.markDirty();

            }
        }

        if(!(this.getSlot(slot) instanceof HappyGhastUpgradeSlot)) {
            player.playSoundToPlayer(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, SoundCategory.NEUTRAL, 1.0F, 0.3F);
        }

        return itemStack;
    }

    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }
}
