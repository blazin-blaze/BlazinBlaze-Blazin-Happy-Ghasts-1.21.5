package net.blazinblaze.happyghastmod.entity.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.item.ItemStack;

public class HappyGhastRenderState extends LivingEntityRenderState {
    public ItemStack saddleStack;
    public boolean isBeingRidden;
    public boolean shooting;
    public boolean isCrowned;
    public int aged;
    public boolean shouldShowCrownGhast;
    public boolean inNether;

    public HappyGhastRenderState() {
        this.saddleStack = ItemStack.EMPTY;
        this.aged = 0;
    }
}