package net.blazinblaze.happyghastmod.entity.custom.goals.ghastling;

import net.blazinblaze.happyghastmod.entity.custom.Ghastling;
import net.blazinblaze.happyghastmod.entity.custom.HappyGhast;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.function.Predicate;

public class GhastlingTemptGoal extends Goal {
    private final Ghastling happyGhast;
    private final Predicate<Ghastling> shouldFollow;
    private static final TargetPredicate TEMPTING_ENTITY_PREDICATE = TargetPredicate.createNonAttackable().ignoreVisibility();
    private final TargetPredicate predicate;
    private final double speed;
    private double lastPlayerX;
    private double lastPlayerY;
    private double lastPlayerZ;
    private double lastPlayerPitch;
    private double lastPlayerYaw;
    @Nullable
    protected PlayerEntity closestPlayer;
    private int cooldown;
    private boolean active;
    private final Predicate<ItemStack> foodPredicate;
    private final boolean canBeScared;

    public GhastlingTemptGoal(Ghastling happyGhast, double speed, Predicate<ItemStack> items, boolean canBeScared, Predicate<Ghastling> shouldFollow) {
        this.happyGhast = happyGhast;
        this.speed = speed;
        this.foodPredicate = items;
        this.canBeScared = canBeScared;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        this.shouldFollow = shouldFollow;
        this.predicate = TEMPTING_ENTITY_PREDICATE.copy().setPredicate((entityx, world) -> this.isTemptedBy(entityx));
    }

    @Override
    public boolean canStart() {
        if (!this.shouldFollow.test(this.happyGhast)) return false;
        if (this.cooldown > 0) {
            this.cooldown--;
            return false;
        } else {
            this.closestPlayer = getServerWorld(this.happyGhast)
                    .getClosestPlayer(this.predicate.setBaseMaxDistance(this.happyGhast.getAttributeValue(EntityAttributes.TEMPT_RANGE)), this.happyGhast);
            return this.closestPlayer != null;
        }
    }

    @Override
    public boolean shouldContinue() {
        if (this.canBeScared()) {
            if (this.happyGhast.squaredDistanceTo(this.closestPlayer) < 36.0) {
                if (this.closestPlayer.squaredDistanceTo(this.lastPlayerX, this.lastPlayerY, this.lastPlayerZ) > 0.010000000000000002) {
                    return false;
                }

                if (Math.abs(this.closestPlayer.getPitch() - this.lastPlayerPitch) > 5.0 || Math.abs(this.closestPlayer.getYaw() - this.lastPlayerYaw) > 5.0) {
                    return false;
                }
            } else {
                this.lastPlayerX = this.closestPlayer.getX();
                this.lastPlayerY = this.closestPlayer.getY();
                this.lastPlayerZ = this.closestPlayer.getZ();
            }

            this.lastPlayerPitch = this.closestPlayer.getPitch();
            this.lastPlayerYaw = this.closestPlayer.getYaw();
        }

        return this.canStart();
    }

    private boolean isTemptedBy(LivingEntity entity) {
        return this.foodPredicate.test(entity.getMainHandStack()) || this.foodPredicate.test(entity.getOffHandStack());
    }

    protected boolean canBeScared() {
        return this.canBeScared;
    }

    @Override
    public void start() {
        this.lastPlayerX = this.closestPlayer.getX();
        this.lastPlayerY = this.closestPlayer.getY();
        this.lastPlayerZ = this.closestPlayer.getZ();
        this.active = true;
    }

    @Override
    public void stop() {
        this.closestPlayer = null;
        this.happyGhast.getNavigation().stop();
        this.cooldown = toGoalTicks(100);
        this.active = false;
    }

    @Override
    public void tick() {
        double xDistance = this.closestPlayer.getX() - this.happyGhast.getX();
        double zDistance = this.closestPlayer.getZ() - this.happyGhast.getZ();
        this.happyGhast.setYaw(-((float) MathHelper.atan2(xDistance, zDistance)) * MathHelper.DEGREES_PER_RADIAN);
        this.happyGhast.bodyYaw = this.happyGhast.getYaw();

        if (this.happyGhast.squaredDistanceTo(this.closestPlayer) <= 49D) {
            this.happyGhast.stopMovement();
        } else {
            this.happyGhast.getMoveControl().moveTo(this.closestPlayer.getX(), this.closestPlayer.getY() + 2D, this.closestPlayer.getZ(), this.speed);
        }
    }

    public boolean isActive() {
        return this.active;
    }
}
