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
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
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
            double d = Math.max(0, 10);
            double e = MathHelper.square(d);
            Vec3d vec3d = user.getCameraPosVec(1.0F);
            HitResult result = user.raycast(5.0D, 1.0F, false);
            double f = result.getPos().squaredDistanceTo(vec3d);
            if (result.getType() != HitResult.Type.MISS) {
                e = f;
                d = Math.sqrt(f);
            }
            Vec3d vec3d2 = user.getRotationVec(1.0F);
            Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
            Box box = user.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1.0, 1.0, 1.0);
            EntityHitResult entityResult = ProjectileUtil.raycast(user, vec3d, vec3d3, box, EntityPredicates.VALID_ENTITY, e);

            if(entityResult != null) {
                Entity entity = entityResult.getEntity();
                if(entity instanceof Ghastling ghastling || entity instanceof HappyGhast ghast) {
                    cir.setReturnValue(ActionResult.SUCCESS);
                }
            }
        }
    }
}
