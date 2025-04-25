package net.blazinblaze.happyghastmod.mixin;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.entity.custom.Ghastling;
import net.blazinblaze.happyghastmod.entity.custom.HappyGhast;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SnowballItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.swing.*;

@Mixin(SnowballItem.class)
public abstract class SnowballMixin {

    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    public void useSnowballEntity(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(itemStack.getItem() instanceof SnowballItem) {
            ProjectileUtil.raycast(user, Vec3d.ZERO, new Vec3d(10,10,10), user.getBoundingBox(), )
            HitResult result = user.raycast(5.0D, 1.0F, false);
            HitResult.Type type = result.getType();

            if(type == HitResult.Type.ENTITY) {
                EntityHitResult entityResult = (EntityHitResult) result;
                Entity entity = entityResult.getEntity();

                if(entity instanceof Ghastling ghastling || entity instanceof HappyGhast ghast) {
                    cir.setReturnValue(ActionResult.SUCCESS);
                    HappyGhastMod.LOGGER.info("HELLO YES");
                }else {
                    cir.setReturnValue(ActionResult.PASS);
                    HappyGhastMod.LOGGER.info("HELLO NO");
                }
                HappyGhastMod.LOGGER.info("HELLO NO 2");
            }
            HappyGhastMod.LOGGER.info("HELLO NO 3");
        }
    }
}
