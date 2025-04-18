package net.blazinblaze.happyghastmod.entity.custom;

import net.blazinblaze.happyghastmod.achievement.HappyGhastCriteria;
import net.blazinblaze.happyghastmod.entity.HappyGhastEntities;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.conversion.EntityConversionContext;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.GameEventTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.EnumSet;
import java.util.Objects;

public class Ghastling extends FlyingEntity implements Leashable {
    private static final TrackedData<Integer> GHASTLING_GROWTH;
    private static final TrackedData<Boolean> IN_NETHER;

    public Ghastling(EntityType<? extends FlyingEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 5;
        this.moveControl = new Ghastling.GhastMoveControl(this);
        this.rewardAchievement();
    }

    protected void initGoals() {
        this.goalSelector.add(5, new Ghastling.FlyRandomlyGoal(this));
        this.goalSelector.add(7, new Ghastling.LookAtTargetGoal(this));
        this.targetSelector.add(1, new ActiveTargetGoal(this, PlayerEntity.class, 10, true, false, (entity, world) -> Math.abs(entity.getY() - this.getY()) <= (double)4.0F));
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return false;
    }

    private static boolean isFireballFromPlayer(DamageSource damageSource) {
        return damageSource.getSource() instanceof FireballEntity && damageSource.getAttacker() instanceof PlayerEntity;
    }

    public boolean isInvulnerableTo(ServerWorld world, DamageSource source) {
        return this.isInvulnerable() && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY) || !isFireballFromPlayer(source) && super.isInvulnerableTo(world, source);
    }

    public static DefaultAttributeContainer.Builder createGhastAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.MAX_HEALTH, (double)10.0F).add(EntityAttributes.FOLLOW_RANGE, (double)100.0F).add(EntityAttributes.SCALE, .25F);
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_GHAST_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_GHAST_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_GHAST_DEATH;
    }

    protected float getSoundVolume() {
        return 5.0F;
    }

    public static boolean canSpawn(EntityType<GhastEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return random.nextInt(20) == 0 && canMobSpawn(type, world, spawnReason, pos, random);
    }

    public int getLimitPerChunk() {
        return 1;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();
        if(player.getWorld() instanceof ServerWorld serverWorld) {
            if(item == Items.SNOWBALL) {
                if(getGhastlingGrowth() >= 24000) {
                    this.convertTo(HappyGhastEntities.HAPPY_GHAST, EntityConversionContext.create(this, false, false), (happyGhast) -> happyGhast.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20*5, 1, true, true)));
                }else {
                    setGhastlingGrowth(getGhastlingGrowth() + 1700);
                    itemStack.decrementUnlessCreative(1, player);
                    serverWorld.addParticleClient(ParticleTypes.HAPPY_VILLAGER, (this.getX() + .5), (this.getY() + .3), this.getZ(), 1.0, 1.0, 1.0);

                    if(getGhastlingGrowth() >= 24000) {
                        this.convertTo(HappyGhastEntities.HAPPY_GHAST, EntityConversionContext.create(this, false, false), (happyGhast) -> happyGhast.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20*5, 1, true, true)));
                    }
                }
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        }else {
            return ActionResult.PASS;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if(this.getWorld() instanceof ServerWorld world) {
            DynamicRegistryManager registryManager = world.getRegistryManager();
            for (int i = 0; i < world.getPlayers().size(); i++) {
                if(Objects.equals(world.getDimensionEntry(), registryManager.getOrThrow(RegistryKeys.DIMENSION_TYPE).getEntry(DimensionTypes.THE_NETHER.getValue()).get())) {
                    setInNether(true);
                }else {
                    setInNether(false);
                }
            }
        }

        setGhastlingGrowth(getGhastlingGrowth() + 1);
        if(getGhastlingGrowth() >= 24000) {
            this.convertTo(HappyGhastEntities.HAPPY_GHAST, EntityConversionContext.create(this, false, false), (happyGhast) -> happyGhast.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20*5, 1, true, true)));
        }
    }

    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(GHASTLING_GROWTH, 0);
        builder.add(IN_NETHER, false);
    }

    public int getGhastlingGrowth() {
        return this.dataTracker.get(GHASTLING_GROWTH);
    }

    public void setGhastlingGrowth(int growth) {
        this.dataTracker.set(GHASTLING_GROWTH, growth);
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("ghastling_growth", this.getGhastlingGrowth());
        nbt.putBoolean("in_nether", this.getInNether());
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setGhastlingGrowth(nbt.getInt("ghastling_growth").orElse(0));
        this.setInNether(nbt.getBoolean("in_nether").orElse(false));
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    private void rewardAchievement() {
        if(this.getWorld() instanceof ServerWorld world) {
            for(int i = 0; i < world.getPlayers().size(); i++) {
                HappyGhastCriteria.SPAWN_GHASTLING.trigger(world.getPlayers().get(i));
            }
        }
    }

    public boolean getInNether() {
        return this.dataTracker.get(IN_NETHER);
    }

    private void setInNether(boolean value) {
        this.dataTracker.set(IN_NETHER, value);
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    static class GhastMoveControl extends MoveControl {
        private final Ghastling ghast;
        private int collisionCheckCooldown;

        public GhastMoveControl(Ghastling ghast) {
            super(ghast);
            this.ghast = ghast;
        }

        public void tick() {
            if (this.state == State.MOVE_TO) {
                if (this.collisionCheckCooldown-- <= 0) {
                    this.collisionCheckCooldown += this.ghast.getRandom().nextInt(5) + 2;
                    Vec3d vec3d = new Vec3d(this.targetX - this.ghast.getX(), this.targetY - this.ghast.getY(), this.targetZ - this.ghast.getZ());
                    double d = vec3d.length();
                    vec3d = vec3d.normalize();
                    if (this.willCollide(vec3d, MathHelper.ceil(d))) {
                        this.ghast.setVelocity(this.ghast.getVelocity().add(vec3d.multiply(0.1)));
                    } else {
                        this.state = State.WAIT;
                    }
                }

            }
        }

        private boolean willCollide(Vec3d direction, int steps) {
            Box box = this.ghast.getBoundingBox();

            for(int i = 1; i < steps; ++i) {
                box = box.offset(direction);
                if (!this.ghast.getWorld().isSpaceEmpty(this.ghast, box)) {
                    return false;
                }
            }

            return true;
        }
    }

    static class FlyRandomlyGoal extends Goal {
        private final Ghastling ghast;

        public FlyRandomlyGoal(Ghastling ghast) {
            this.ghast = ghast;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        public boolean canStart() {
            MoveControl moveControl = this.ghast.getMoveControl();
            if (!moveControl.isMoving()) {
                return true;
            } else {
                double d = moveControl.getTargetX() - this.ghast.getX();
                double e = moveControl.getTargetY() - this.ghast.getY();
                double f = moveControl.getTargetZ() - this.ghast.getZ();
                double g = d * d + e * e + f * f;
                return g < (double)1.0F || g > (double)3600.0F;
            }
        }

        public boolean shouldContinue() {
            return false;
        }

        public void start() {
            Random random = this.ghast.getRandom();
            double d = this.ghast.getX() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double e = this.ghast.getY() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double f = this.ghast.getZ() + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.ghast.getMoveControl().moveTo(d, e, f, (double)1.0F);
        }
    }

    static class LookAtTargetGoal extends Goal {
        private final Ghastling ghast;

        public LookAtTargetGoal(Ghastling ghast) {
            this.ghast = ghast;
            this.setControls(EnumSet.of(Control.LOOK));
        }

        public boolean canStart() {
            return true;
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            if (this.ghast.getTarget() == null) {
                Vec3d vec3d = this.ghast.getVelocity();
                this.ghast.setYaw(-((float)MathHelper.atan2(vec3d.x, vec3d.z)) * (180F / (float)Math.PI));
                this.ghast.bodyYaw = this.ghast.getYaw();
            } else {
                LivingEntity livingEntity = this.ghast.getTarget();
                double d = (double)64.0F;
                if (livingEntity.squaredDistanceTo(this.ghast) < (double)4096.0F) {
                    double e = livingEntity.getX() - this.ghast.getX();
                    double f = livingEntity.getZ() - this.ghast.getZ();
                    this.ghast.setYaw(-((float)MathHelper.atan2(e, f)) * (180F / (float)Math.PI));
                    this.ghast.bodyYaw = this.ghast.getYaw();
                }
            }

        }
    }

    static {
        GHASTLING_GROWTH = DataTracker.registerData(Ghastling.class, TrackedDataHandlerRegistry.INTEGER);
        IN_NETHER = DataTracker.registerData(Ghastling.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}
