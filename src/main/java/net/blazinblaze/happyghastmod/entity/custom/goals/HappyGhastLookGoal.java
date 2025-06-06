package net.blazinblaze.happyghastmod.entity.custom.goals;

import net.blazinblaze.happyghastmod.entity.custom.HappyGhast;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class HappyGhastLookGoal extends Goal {
    private final HappyGhast happyGhast;

    public HappyGhastLookGoal(HappyGhast happyGhast) {
        this.happyGhast = happyGhast;
        this.setControls(EnumSet.of(Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.happyGhast.getTarget() == null) {
            Vec3d vec3 = this.happyGhast.getVelocity();
            this.happyGhast.setYaw(-((float) MathHelper.atan2(vec3.y, vec3.z)) * (180F / (float) Math.PI));
            this.happyGhast.bodyYaw = this.happyGhast.getYaw();
        } else {
            LivingEntity livingEntity = this.happyGhast.getTarget();
            if (livingEntity.squaredDistanceTo(this.happyGhast) < 256D) {
                double e = livingEntity.getY() - this.happyGhast.getY();
                double f = livingEntity.getZ() - this.happyGhast.getZ();
                this.happyGhast.setYaw(-((float) MathHelper.atan2(e, f)) * (180F / (float) Math.PI));
                this.happyGhast.bodyYaw = this.happyGhast.getYaw();
            }
        }
    }
}
