package net.blazinblaze.happyghastmod.mixin;

import net.blazinblaze.happyghastmod.entity.custom.Ghastling;
import net.blazinblaze.happyghastmod.entity.custom.HappyGhast;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SnowballItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.swing.*;

@Mixin(Item.class)
public class SnowballMixin {
    @Inject(method = "useOnEntity", at = @At(value = "HEAD"), cancellable = true)
    public void useSnowballEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if(stack.getItem() instanceof SnowballItem) {
            if(entity instanceof Ghastling || entity instanceof HappyGhast) {
                cir.setReturnValue(ActionResult.SUCCESS);
            }else {
                cir.setReturnValue(ActionResult.PASS);
            }
        }else {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
