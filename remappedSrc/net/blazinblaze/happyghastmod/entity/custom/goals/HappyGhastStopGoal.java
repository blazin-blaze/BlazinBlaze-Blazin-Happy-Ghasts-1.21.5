package net.blazinblaze.happyghastmod.entity.custom.goals;

import net.blazinblaze.happyghastmod.entity.HappyGhastEntities;
import net.blazinblaze.happyghastmod.entity.custom.HappyGhast;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.Box;

import javax.swing.*;
import java.util.EnumSet;

public class HappyGhastStopGoal extends Goal {
    private final HappyGhast happyGhast;

    public HappyGhastStopGoal(HappyGhast happyGhast) {
        this.happyGhast = happyGhast;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
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
