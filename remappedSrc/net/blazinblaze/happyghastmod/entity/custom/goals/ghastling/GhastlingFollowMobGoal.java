package net.blazinblaze.happyghastmod.entity.custom.goals.ghastling;

import net.blazinblaze.happyghastmod.entity.custom.Ghastling;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Random;
import java.util.function.Predicate;

public class GhastlingFollowMobGoal extends Goal {
    private static final TargetPredicate FOLLOW_TARGETING_PLAYER = TargetPredicate.createNonAttackable().ignoreVisibility();
    private static final TargetPredicate FOLLOW_TARGETING = TargetPredicate.createNonAttackable();
    private static final int MAX_FOLLOW_TICKS = 400;

    private final TargetPredicate playerTargetingConditions;
    private final TargetPredicate targetingConditions;
    private final Ghastling happyGhast;
    private final double yOffset;

    public double ex;
    public double ey;
    public double ez;
    @Nullable
    protected Entity target;
    private int followTicks;
    private int followCooldown;
    private boolean isRunning;

    public GhastlingFollowMobGoal(Ghastling happyGhast, double yOffset, Predicate<Entity> canFollow) {
        this.happyGhast = happyGhast;
        this.yOffset = yOffset;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        this.playerTargetingConditions = FOLLOW_TARGETING_PLAYER.copy().setPredicate((livingEntity, serverLevel) -> canFollow.test(livingEntity) && this.target != livingEntity);
        this.targetingConditions = FOLLOW_TARGETING.copy().setPredicate((livingEntity, serverLevel) -> canFollow.test(livingEntity) && this.target != livingEntity);
    }

    @Override
    public boolean canStart() {
        if (this.followTicks >= MAX_FOLLOW_TICKS) return false;
        if (this.followCooldown > 0) {
            this.followCooldown--;
            return false;
        } else {
            if (this.isRunning && this.target != null && this.target.getWorld() == this.happyGhast.getWorld()) return true;
            Entity owner = this.happyGhast.getOwner();
            if (owner != null && owner.getWorld() == this.happyGhast.getWorld() && this.target != owner) {
                if (owner.distanceTo(this.happyGhast) <= this.getSearchRange(true)) {
                    this.target = owner;
                    return true;
                }
            }

            PlayerEntity player = getServerWorld(this.happyGhast).getClosestPlayer(this.playerTargetingConditions.setBaseMaxDistance(this.getSearchRange(true)), this.happyGhast);
            if (player != null && (player.distanceTo(this.happyGhast) > 16D || this.happyGhast.getRandom().nextFloat() <= 0.25F) && this.target != player) {
                this.target = player;
                return true;
            }

            int searchRange = this.getSearchRange(false);
            LivingEntity livingEntity = getServerWorld(this.happyGhast).getClosestEntity(
                    LivingEntity.class,
                    this.targetingConditions.setBaseMaxDistance(searchRange),
                    this.happyGhast,
                    this.happyGhast.getX(),
                    this.happyGhast.getY(),
                    this.happyGhast.getZ(),
                    this.happyGhast.getBoundingBox().expand(searchRange)
            );
            if (livingEntity != null) {
                this.target = livingEntity;
                return true;
            }
        }
        return false;
    }

    public int getSearchRange(boolean searchingForPlayer) {
        if (this.happyGhast.isBaby()) return searchingForPlayer ? 32 : 16;
        return searchingForPlayer ? 64 : 32;
    }

    @Override
    public void start() {
        this.ex = this.target.getX();
        this.ey = this.target.getY();
        this.ez = this.target.getZ();
        this.isRunning = true;
    }

    @Override
    public void stop() {
        this.happyGhast.getNavigation().stop();
        this.followCooldown = toGoalTicks(100);
        this.followTicks = 0;
        this.isRunning = false;
    }

    @Override
    public void tick() {
        this.followTicks += 1;
        double xDistance = this.target.getX() - this.happyGhast.getX();
        double zDistance = this.target.getZ() - this.happyGhast.getZ();
        this.happyGhast.setYaw(-((float) MathHelper.atan2(xDistance, zDistance)) * MathHelper.DEGREES_PER_RADIAN);
        this.happyGhast.bodyYaw = this.happyGhast.getYaw();

        if (this.happyGhast.squaredDistanceTo(this.target) <= MathHelper.square(this.happyGhast.getBoundingBox().getAverageSideLength() * 2D)) {
            this.happyGhast.stopMovement();
            if(this.happyGhast.getWorld() instanceof ServerWorld world) {
                if(this.target instanceof PlayerEntity) {
                    Random random = new Random();
                    if(random.nextFloat() < 0.3F) {
                        world.spawnParticles(ParticleTypes.HEART, this.happyGhast.getX(), this.happyGhast.getY(), this.happyGhast.getZ(), 1, 0.2, 0.2, 0, 0.5);
                    }
                }
            }
        } else {
            this.happyGhast.getMoveControl().moveTo(this.target.getX(), this.target.getY() + this.yOffset, this.target.getZ(), 1D);
        }
    }
}
