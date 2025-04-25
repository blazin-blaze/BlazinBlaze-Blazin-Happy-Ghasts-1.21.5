package net.blazinblaze.happyghastmod.entity.custom.goals;

import net.blazinblaze.happyghastmod.entity.custom.HappyGhast;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

import java.util.EnumSet;

public class HappyGhastGoHomeGoal extends Goal {
    private final HappyGhast happyGhast;

    public HappyGhastGoHomeGoal(HappyGhast happyGhast) {
        this.happyGhast = happyGhast;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!this.happyGhast.hasControllingPassenger() && this.happyGhast.isInHomePositionDimension()) {
            return !this.happyGhast.getBlockPos().isWithinDistance(this.happyGhast.getHomeBlockPosition().get(), this.getHomeDistance());
        }
        return false;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    public double getHomeDistance() {
        return this.happyGhast.hasSaddleEquipped() ? 32D : 64D;
    }

    @Override
    public void start() {
        if (!this.happyGhast.hasControllingPassenger() && this.happyGhast.isInHomePositionDimension()) {
            BlockPos homePosition = this.happyGhast.getHomeBlockPosition().get();
            Random randomSource = this.happyGhast.getRandom();
            double d = homePosition.getX() + (randomSource.nextFloat() * 2F - 1F) * this.getHomeDistance();
            double e = homePosition.getY() + (randomSource.nextFloat() * 2F - 1F) * this.getHomeDistance();
            double f = homePosition.getZ() + (randomSource.nextFloat() * 2F - 1F) * this.getHomeDistance();
            this.happyGhast.getMoveControl().moveTo(d, e, f, 1D);
        }
    }
}
