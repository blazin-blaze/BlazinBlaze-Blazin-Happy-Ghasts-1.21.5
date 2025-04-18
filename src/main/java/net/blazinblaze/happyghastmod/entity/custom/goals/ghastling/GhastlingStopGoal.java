package net.blazinblaze.happyghastmod.entity.custom.goals.ghastling;

import net.blazinblaze.happyghastmod.entity.custom.Ghastling;
import net.blazinblaze.happyghastmod.entity.custom.HappyGhast;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

import java.util.EnumSet;

public class GhastlingStopGoal extends Goal {
    private final Ghastling happyGhast;

    public GhastlingStopGoal(Ghastling happyGhast) {
        this.happyGhast = happyGhast;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        if (this.happyGhast.hasSaddleEquipped()) {
            Box happyGhastBox = this.happyGhast.getBoundingBox();
            happyGhastBox = happyGhastBox.offset(0D, happyGhastBox.getLengthY(), 0D).expand(1D);
            return !this.happyGhast.getWorld().getEntitiesByType(
                    this.happyGhast.getType(),
                    happyGhastBox,
                    entity -> entity instanceof PlayerEntity && entity.isAlive() && !entity.isSpectator()
            ).isEmpty();
        }
        return false;
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.happyGhast.stopMovement();
    }
}
