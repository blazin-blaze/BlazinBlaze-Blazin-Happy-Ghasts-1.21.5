package net.blazinblaze.happyghastmod.entity.custom;

import net.blazinblaze.happyghastmod.achievement.HappyGhastCriteria;
import net.blazinblaze.happyghastmod.entity.HappyGhastEntities;
import net.blazinblaze.happyghastmod.entity.custom.goals.*;
import net.blazinblaze.happyghastmod.entity.custom.goals.ghastling.*;
import net.blazinblaze.happyghastmod.sound.HappyGhastSounds;
import net.blazinblaze.happyghastmod.tag.HappyGhastTags;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionTypes;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;

public class Ghastling extends FlyingEntity implements Leashable, Ownable {
    private static final TrackedData<Integer> GHASTLING_GROWTH;
    private static final TrackedData<Boolean> IN_NETHER;
    private Optional<GlobalPos> homePosition = Optional.empty();

    public Ghastling(EntityType<? extends FlyingEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 5;
        this.moveControl = new Ghastling.GhastMoveControl(this);
        this.rewardAchievement();
    }

    public void updateHomePosition() {
        this.homePosition = Optional.of(new GlobalPos(this.getWorld().getRegistryKey(), this.getBlockPos()));
    }

    protected void initGoals() {
        this.goalSelector.add(1, new GhastlingTemptGoal(this, 1.25D, itemStack -> itemStack.isOf(Items.SNOWBALL), true, ghastling -> true));
        this.goalSelector.add(2, new GhastlingGoHomeGoal(this));
        this.goalSelector.add(3, new GhastlingFollowMobGoal(this, 1D, entity -> entity.getType().isIn(HappyGhastTags.GHASTLING_FOLLOWS)));
        this.goalSelector.add(4, new GhastlingFloatAroundGoal(this));
        this.goalSelector.add(5, new GhastlingLookGoal(this));
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
        return MobEntity.createMobAttributes().add(EntityAttributes.MAX_HEALTH, (double)12.0F).add(EntityAttributes.FOLLOW_RANGE, (double)100.0F).add(EntityAttributes.SCALE, .25F).add(EntityAttributes.TEMPT_RANGE, 25.0F);
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }

    protected SoundEvent getAmbientSound() {
        return HappyGhastSounds.GHASTLING_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return HappyGhastSounds.GHASTLING_HURT;
    }

    protected SoundEvent getDeathSound() {
        return HappyGhastSounds.GHASTLING_DEATH;
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
                this.heal(1.0F);
                this.playSound(SoundEvents.BLOCK_SNOW_BREAK, this.getSoundVolume(), 1.0f);
                serverWorld.spawnParticles(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 1, 0.2, 0.2, 0, 0.5);
                if(getGhastlingGrowth() >= 24000) {
                    this.convertTo(HappyGhastEntities.HAPPY_GHAST, EntityConversionContext.create(this, false, false), (happyGhast) -> happyGhast.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20*5, 1, true, true)));
                }else {
                    setGhastlingGrowth(getGhastlingGrowth() + 1700);
                    itemStack.decrementUnlessCreative(1, player);
                    serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER, this.getX(), this.getY(), this.getZ(), 1, 0.2, 0.2, 0, 0.5);

                    if(getGhastlingGrowth() >= 24000) {
                        this.convertTo(HappyGhastEntities.HAPPY_GHAST, EntityConversionContext.create(this, false, false), (happyGhast) -> happyGhast.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20*5, 1, true, true)));
                    }
                }
                player.getStackInHand(hand).decrementUnlessCreative(1, player);
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
            this.heal(0.003F);
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
        this.homePosition.ifPresent(globalPos -> nbt.put("HomePosition", GlobalPos.CODEC, globalPos));
        nbt.putInt("ghastling_growth", this.getGhastlingGrowth());
        nbt.putBoolean("in_nether", this.getInNether());
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.homePosition = nbt.get("HomePosition", GlobalPos.CODEC);
        this.setGhastlingGrowth(nbt.getInt("ghastling_growth").orElse(0));
        this.setInNether(nbt.getBoolean("in_nether").orElse(false));
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.getWorld() instanceof ServerWorld && this.isAlive()) {
            if (this.deathTime == 0) {
                if (this.getWorld().getRegistryKey() == World.OVERWORLD) {
                    if (this.getBoundingBox().contains(this.getX(), 195D, this.getZ()) || this.isRainingOrSnowingAt()) {
                        if (this.getActiveStatusEffects().get(StatusEffects.REGENERATION) == null) {
                            this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100, 0));
                        }
                    }
                }
            }

            if (this.isSubmergedInWater()) {
                this.setVelocity(this.getVelocity().add(0D, 0.04D, 0D));
            }
        }
    }

    public boolean isRainingOrSnowingAt() {
        World world = this.getWorld();
        BlockPos blockPos = this.getBlockPos();
        if (!world.isRaining()) {
            return false;
        } else if (!world.isSkyVisible(blockPos)) {
            return false;
        } else if (world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos).getY() > blockPos.getY()) {
            return false;
        } else {
            Biome biome = world.getBiome(blockPos).value();
            return biome.getPrecipitation(blockPos, world.getSeaLevel()) != Biome.Precipitation.NONE;
        }
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

    @Override
    public @Nullable Entity getOwner() {
        return null;
    }

    public boolean hasHomePosition() {
        return this.homePosition.isPresent();
    }

    public boolean isInHomePositionDimension() {
        return this.hasHomePosition() && this.getWorld().getRegistryKey() == this.homePosition.get().dimension();
    }

    public Optional<BlockPos> getHomeBlockPosition() {
        return this.homePosition.map(GlobalPos::pos);
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.isSubmergedInWater()) {
            this.updateVelocity(0.02F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.8F));
        } else if (this.isInLava()) {
            this.updateVelocity(0.02F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.5));
        } else {
            float f = 0.91F;
            if (this.isOnGround()) {
                f = this.getWorld().getBlockState(this.getVelocityAffectingPos()).getBlock().getSlipperiness() * 0.91F;
            }

            float g = 0.16277137F / (f * f * f);
            f = 0.91F;
            if (this.isOnGround()) {
                f = this.getWorld().getBlockState(this.getVelocityAffectingPos()).getBlock().getSlipperiness() * 0.91F;
            }

            this.updateVelocity(this.isOnGround() ? 0.1F * g : 0.02F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(f));
        }
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

    static {
        GHASTLING_GROWTH = DataTracker.registerData(Ghastling.class, TrackedDataHandlerRegistry.INTEGER);
        IN_NETHER = DataTracker.registerData(Ghastling.class, TrackedDataHandlerRegistry.BOOLEAN);
    }
}
