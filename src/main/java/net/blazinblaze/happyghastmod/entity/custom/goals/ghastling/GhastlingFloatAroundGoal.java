package net.blazinblaze.happyghastmod.entity.custom.goals.ghastling;

import net.blazinblaze.happyghastmod.entity.custom.Ghastling;
import net.blazinblaze.happyghastmod.entity.custom.HappyGhast;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.random.Random;

import java.util.EnumSet;

public class GhastlingFloatAroundGoal extends Goal {
    private final Ghastling happyGhast;

    public GhastlingFloatAroundGoal(Ghastling happyGhast) {
        this.happyGhast = happyGhast;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        MoveControl moveControl = this.happyGhast.getMoveControl();
        if (!moveControl.isMoving()) {
            return true;
        } else {
            double d = moveControl.getTargetX() - this.happyGhast.getX();
            double e = moveControl.getTargetY() - this.happyGhast.getY();
            double f = moveControl.getTargetZ() - this.happyGhast.getZ();
            double g = d * d + e * e + f * f;
            return g < 1D || g > 3600D;
        }
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    @Override
    public void start() {
        int distance = this.happyGhast.hasSaddleEquipped() ? 8 : 16;
        Random randomSource = this.happyGhast.getRandom();
        double d = this.happyGhast.getX() + (randomSource.nextFloat() * 2F - 1F) * distance;
        double e = this.happyGhast.getY() + (randomSource.nextFloat() * 2F - 1F) * distance;
        double f = this.happyGhast.getZ() + (randomSource.nextFloat() * 2F - 1F) * distance;
        this.happyGhast.getMoveControl().moveTo(d, e, f, 1D);
    }
}
