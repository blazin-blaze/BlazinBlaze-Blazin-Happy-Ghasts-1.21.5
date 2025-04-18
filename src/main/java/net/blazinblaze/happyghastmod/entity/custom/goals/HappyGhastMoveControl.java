package net.blazinblaze.happyghastmod.entity.custom.goals;

import net.blazinblaze.happyghastmod.entity.custom.HappyGhast;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class HappyGhastMoveControl extends MoveControl {
    private final HappyGhast happyGhast;
    private int floatDuration;

    public HappyGhastMoveControl(HappyGhast happyGhast) {
        super(happyGhast);
        this.happyGhast = happyGhast;
    }

    @Override
    public void tick() {
        if (this.state == State.MOVE_TO) {
            if (this.floatDuration-- <= 0) {
                this.floatDuration = this.floatDuration + this.happyGhast.getRandom().nextInt(5) + 2;
                Vec3d vec3 = new Vec3d(this.targetX - this.happyGhast.getX(), this.targetY - this.happyGhast.getY(), this.targetZ - this.happyGhast.getZ());
                double d = vec3.length();
                vec3 = vec3.normalize();
                if (this.canReach(vec3, MathHelper.ceil(d))) {
                    this.happyGhast.setVelocity(this.happyGhast.getVelocity().add(vec3.multiply(0.1D)));
                } else {
                    this.state = State.WAIT;
                }
            }
        }
    }

    private boolean canReach(Vec3d vec3, int i) {
        Box aABB = this.happyGhast.getBoundingBox();
        for (int j = 1; j < i; j++) {
            aABB = aABB.offset(vec3);
            if (!this.happyGhast.getWorld().canCollide(this.happyGhast, aABB)) return false;
        }
        return true;
    }
}
