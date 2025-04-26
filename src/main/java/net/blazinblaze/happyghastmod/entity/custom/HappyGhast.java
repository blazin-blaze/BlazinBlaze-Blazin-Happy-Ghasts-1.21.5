package net.blazinblaze.happyghastmod.entity.custom;

import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.achievement.HappyGhastCriteria;
import net.blazinblaze.happyghastmod.achievement.HappyGhastSpawnCriterion;
import net.blazinblaze.happyghastmod.attachments.HappyGhastAttachments;
//import net.blazinblaze.happyghastmod.item.custom.HarnessItem;
//import net.blazinblaze.happyghastmod.networking.HappyGhastScreenS2CPacket;
import net.blazinblaze.happyghastmod.entity.custom.goals.*;
import net.blazinblaze.happyghastmod.item.HappyGhastItems;
import net.blazinblaze.happyghastmod.screen.HappyGhastUpgradeScreenHandler;
import net.blazinblaze.happyghastmod.sound.HappyGhastSounds;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionTypes;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;

import javax.sound.midi.Track;
import java.util.*;

public class HappyGhast extends PathAwareEntity implements Ownable, Leashable, RideableInventory {

    private static final TrackedData<Boolean> SHOOTING;
    private static final TrackedData<Integer> COOLDOWN;
    private static final TrackedData<Boolean> SPEED_UPGRADE;
    private static final TrackedData<Boolean> STRENGTH_UPGRADE;
    private static final TrackedData<Boolean> HEART_UPGRADE;
    private static final TrackedData<Boolean> FIREBALL_UPGRADE;
    private static final TrackedData<Boolean> ROYALTY_UPGRADE;
    private static final TrackedData<Integer> AGED_VALUE;
    private static final TrackedData<Integer> AGED_COUNTER;
    private static final TrackedData<Boolean> IN_NETHER;
    private static final TrackedData<Boolean> IS_CONTROLLED;
    private int fireballStrength = 5;
    protected SimpleInventory items;
    private Optional<GlobalPos> homePosition = Optional.empty();

    public HappyGhast(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 5;
        this.moveControl = new GhastMoveControl(this);
        this.setupInventory();
        this.dataTracker.set(IS_CONTROLLED, this.isControlledByPlayer());

        this.items.addListener(new HappyGhastInventoryChanged(this));
    }

    public void updateHomePosition() {
        this.homePosition = Optional.of(new GlobalPos(this.getWorld().getRegistryKey(), this.getBlockPos()));
    }

    protected void initGoals() {
        this.goalSelector.add(1, new HappyGhastStopGoal(this));
        this.goalSelector.add(2, new HappyGhastTemptGoal(this, 1.25D, itemStack -> itemStack.isOf(Items.SNOWBALL), true, happyGhast -> true));
        this.goalSelector.add(2, new HappyGhastTemptGoal(
                        this,
                        1.25D,
                        itemStack -> {
                            EquippableComponent equippable = itemStack.getComponents().get(DataComponentTypes.EQUIPPABLE);
                            if (equippable != null) return equippable.allows(this.getType());
                            return false;
                        },
                        true,
                        happyGhast -> !happyGhast.hasSaddleEquipped()
                )
        );
        this.goalSelector.add(3, new HappyGhastGoHomeGoal(this));
        this.goalSelector.add(5, new HappyGhastFloatAroundGoal(this));
        this.goalSelector.add(6, new HappyGhastLookGoal(this));
    }

    public boolean isShooting() {
        return this.dataTracker.get(SHOOTING);
    }

    public void setShooting(boolean shooting) {
        this.dataTracker.set(SHOOTING, shooting);
    }

    public int getFireballStrength() {
        return this.fireballStrength;
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return false;
    }

    private static boolean isFireballFromPlayer(DamageSource damageSource) {
        return damageSource.getSource() instanceof HappyGhastFireballEntity && damageSource.getAttacker() instanceof PlayerEntity;
    }

    public boolean isInvulnerableTo(ServerWorld world, DamageSource source) {
        return this.isInvulnerable() && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY) || !isFireballFromPlayer(source) && super.isInvulnerableTo(world, source);
    }

    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (isFireballFromPlayer(source)) {
            super.damage(world, source, 1000.0F);
            return true;
        } else {
            return this.isInvulnerableTo(world, source) ? false : super.damage(world, source, amount);
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SHOOTING, false);
        builder.add(COOLDOWN, 0);
        builder.add(SPEED_UPGRADE, false);
        builder.add(STRENGTH_UPGRADE, false);
        builder.add(HEART_UPGRADE, false);
        builder.add(FIREBALL_UPGRADE, false);
        builder.add(ROYALTY_UPGRADE, false);
        builder.add(AGED_VALUE, 0);
        builder.add(AGED_COUNTER, 0);
        builder.add(IN_NETHER, false);
        builder.add(IS_CONTROLLED, false);
    }

    public static DefaultAttributeContainer.Builder createGhastAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.MAX_HEALTH, (double)20.0F).add(EntityAttributes.FOLLOW_RANGE, (double)100.0F).add(EntityAttributes.MOVEMENT_SPEED, 0.7F).add(EntityAttributes.TEMPT_RANGE, 25.0F).add(EntityAttributes.ENTITY_INTERACTION_RANGE, 20.0F);
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return HappyGhastSounds.HAPPY_GHAST_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return HappyGhastSounds.HAPPY_GHAST_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return HappyGhastSounds.HAPPY_GHAST_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 5.0F;
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("speed_upgrade", this.getSpeedUpg());
        nbt.putBoolean("strength_upgrade", this.getStrength());
        nbt.putBoolean("heart_upgrade", this.getHeart());
        nbt.putBoolean("fireball_upgrade", this.getFireball());
        nbt.putBoolean("royalty_upgrade", this.getRoyalty());
        nbt.putByte("ExplosionPower", (byte)this.getFireballStrength());
        nbt.putInt("aged_value", this.getAgedValue());
        nbt.putInt("aged_counter", this.getAgedCounter());
        nbt.putBoolean("in_nether", this.getInNether());
        this.homePosition.ifPresent(globalPos -> nbt.put("HomePosition", GlobalPos.CODEC, globalPos));

        NbtList nbtList = new NbtList();

        for(int i = 0; i < this.items.size(); ++i) {
            ItemStack itemStack = this.items.getStack(i);
            if (!itemStack.isEmpty()) {
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putByte("Slot", (byte)i);
                nbtList.add(itemStack.toNbt(this.getRegistryManager(), nbtCompound));
            }
        }

        nbt.put("Items", nbtList);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setSpeed(nbt.getBoolean("speed_upgrade").orElse(false));
        this.setStrength(nbt.getBoolean("strength_upgrade").orElse(false));
        this.setHeart(nbt.getBoolean("heart_upgrade").orElse(false));
        this.setFireball(nbt.getBoolean("fireball_upgrade").orElse(false));
        this.setRoyalty(nbt.getBoolean("royalty_upgrade").orElse(false));
        this.fireballStrength = nbt.getByte("ExplosionPower", (byte)5);
        this.setAgedValue(nbt.getInt("aged_value").orElse(0));
        this.setAgedCounter(nbt.getInt("aged_counter").orElse(0));
        this.setInNether(nbt.getBoolean("in_nether").orElse(false));
        this.homePosition = nbt.get("HomePosition", GlobalPos.CODEC);

        NbtList nbtList = nbt.getListOrEmpty("Items");

        for(int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompoundOrEmpty(i);
            int j = nbtCompound.getByte("Slot", (byte)0) & 255;
            if (j < this.items.size()) {
                this.items.setStack(j, (ItemStack)ItemStack.fromNbt(this.getRegistryManager(), nbtCompound).orElse(ItemStack.EMPTY));
            }
        }
    }

    @Override
    public void stopMovement() {
        this.moveControl.moveTo(this.lastX,this.lastY,this.lastZ, 0D);
        super.stopMovement();
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
    public boolean collidesWith(Entity other) {
        return canCollide(this, other);
    }

    public boolean canCollide(Entity entity, Entity other) {
        if(entity instanceof HappyGhastFireballEntity fireball) {
            if(fireball.getOwner() != this) {
                return (other.isCollidable() || other.isPushable()) && !entity.isConnectedThroughVehicle(other);
            }else {
                return false;
            }
        }
        return (other.isCollidable() || other.isPushable()) && !entity.isConnectedThroughVehicle(other);
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        super.onPlayerCollision(player);
        this.stopMovement();
    }

    @Override
    public boolean isCollidable() {
        return this.hasSaddleEquipped();
    }

    protected void shootFireball(PlayerEntity player) {
        if(this.getCooldown() == 0 && this.getFireball()) {
            if(!this.getWorld().isClient() ) {
                this.setCooldown(20*5);
                this.setShooting(true);
                Vec3d vec3d = this.getRotationVec(1.0F);
                HitResult result = player.raycast(30, 1.0F, true);
                double f = result.getPos().x - (this.getX() + vec3d.x * (double)4.0F);
                double g = result.getPos().y - ((double)0.5F) - ((double)0.5F + this.getBodyY((double)0.5F));
                double h = result.getPos().z - (this.getZ() + vec3d.z * (double)4.0F);
                Vec3d vec3d2 = new Vec3d(f, g, h);
                ServerWorld world = (ServerWorld) this.getWorld();
                HappyGhastFireballEntity fireballEntity = new HappyGhastFireballEntity(world, this, vec3d2.normalize(), this.getFireballStrength(), player.getUuidAsString());
                if(this.getSpeedUpg()) {
                    fireballEntity.setPosition(this.getX() + vec3d.x * (double)5.5F, this.getBodyY((double)0.5F) + (double)0.5F, fireballEntity.getZ() + vec3d.z * (double)5.5F);
                }else {
                    fireballEntity.setPosition(this.getX() + vec3d.x * (double)4.0F, this.getBodyY((double)0.5F) + (double)0.5F, fireballEntity.getZ() + vec3d.z * (double)4.0F);
                }
                world.spawnEntity(fireballEntity);
            }
        }
    }

    @Override
    protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
        super.tickControlled(controllingPlayer, movementInput);
        this.setRotation(controllingPlayer.getHeadYaw(), controllingPlayer.getPitch() * 0.5F);
        this.lastYaw = this.bodyYaw = this.headYaw = this.getYaw();
        if(Boolean.TRUE.equals(controllingPlayer.getAttached(HappyGhastAttachments.SPRINT_INPUT))) {
            shootFireball(controllingPlayer);
        }
    }

    @Override
    public boolean handleFallDamage(double fallDistance, float damagePerDistance, DamageSource damageSource) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        Random random = Random.create();

        if(!this.getWorld().isClient()) {
            if(this.getStrength()) {
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 1, 2, false, false));
            }

            if(this.getHeart()) {
                this.heal(0.03F);
            }else {
                this.heal(0.003F);
            }
        }

        if(this.getCooldown() > 0) {
            this.setCooldown(this.getCooldown() - 1);
        }else if(this.getCooldown() == 0){
            this.setShooting(false);
        }

        if(!(this.getAgedCounter() >= (14400*20))) {
            if(this.getAgedCounter() >= (7200*20)) {
                if (this.getAgedValue() != 1) {
                    if(random.nextFloat() < 0.4F) {
                        this.setAgedValue(1);
                    }
                }else {
                    this.setAgedCounter(this.getAgedCounter() + 1);
                }
            }else {
                this.setAgedCounter(this.getAgedCounter() + 1);
            }
        }else {
            if(this.getAgedValue() != 2) {
                if(random.nextFloat() < 0.4F) {
                    this.setAgedValue(2);
                }
            }
        }

        if(this.isControlledByPlayer() != this.getIsControlled()) {
            this.setIsControlled(this.isControlledByPlayer());
            if(this.getIsControlled()) {
                this.playSound(HappyGhastSounds.HAPPY_GHAST_GOGGLES_DOWN, this.getSoundVolume(), 1.0f);
            }else {
                this.playSound(HappyGhastSounds.HAPPY_GHAST_GOGGLES_UP, this.getSoundVolume(), 1.0f);
            }
        }

        if (this.hasControllingPassenger()) this.updateHomePosition();
        this.dimensionGhast();
    }

    @Override
    public boolean canUseSlot(EquipmentSlot slot) {
        if (slot != EquipmentSlot.SADDLE) {
            return super.canUseSlot(slot);
        } else {
            return this.isAlive() && !this.isBaby();
        }
    }

    @Override
    protected boolean canDispenserEquipSlot(EquipmentSlot slot) {
        return slot == EquipmentSlot.SADDLE || super.canDispenserEquipSlot(slot);
    }

    @Override
    protected RegistryEntry<SoundEvent> getEquipSound(EquipmentSlot slot, ItemStack stack, EquippableComponent equippableComponent) {
        return (RegistryEntry<SoundEvent>)(slot == EquipmentSlot.SADDLE ? SoundEvents.ENTITY_STRIDER_SADDLE : super.getEquipSound(slot, stack, equippableComponent));
    }

    @Override
    public LivingEntity getControllingPassenger() {
        if (this.hasSaddleEquipped()) {
            Entity var2 = this.getFirstPassenger();
            if (var2 instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity)var2;
                return playerEntity;
            }
        }

        return super.getControllingPassenger();
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Vec3d dismountLocation = super.updatePassengerForDismount(passenger);
        if(this.getPassengerList().indexOf(passenger) == 1) {
            this.playSound(HappyGhastSounds.HAPPY_GHAST_GOGGLES_DOWN, this.getSoundVolume(), 1.0f);
        }
        return dismountLocation.add(0D, 0.2D, 0D);
    }

    @Override
    protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
        float f = controllingPlayer.sidewaysSpeed;
        float g = controllingPlayer.forwardSpeed;
        if (g <= 0.0F) {
            g *= 0.25F;
        }

        float horizontalSpeed = 0.0f;
        if(Boolean.TRUE.equals(controllingPlayer.getAttached(HappyGhastAttachments.JUMPED_INPUT))) {
            horizontalSpeed = 1F;
        }else if(speed != 0F) {
            horizontalSpeed = (float) controllingPlayer.getFacing().getOffsetY();
            if (horizontalSpeed > 0F) horizontalSpeed *= 0.5F;
        }

        return new Vec3d((double)f, (double)Math.clamp(horizontalSpeed * 2F, -1F, 1F), (double)g);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (this.hasSaddleEquipped() && !this.hasPassengers()) {
            if(player.getStackInHand(hand).getItem() == Items.SHEARS) {
                this.dropItem(this.getEquippedStack(EquipmentSlot.SADDLE).copyAndEmpty(), true, false);
                this.playSound(HappyGhastSounds.HAPPY_GHAST_HARNESS_UNEQUIP, this.getSoundVolume(), 1.0f);
                return ActionResult.SUCCESS;
            } else if(player.getStackInHand(hand).getItem() == Items.SNOWBALL) {
                if(this.getWorld() instanceof ServerWorld serverWorld) {
                    this.heal(1F);
                    this.playSound(SoundEvents.BLOCK_SNOW_BREAK, this.getSoundVolume(), 1.0f);
                    serverWorld.spawnParticles(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 1, 0, 0.5, 2.3, 0.5);
                    ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                    int data = serverPlayer.getAttachedOrCreate(HappyGhastAttachments.SNOWBALL_COUNT, HappyGhastAttachments.SNOWBALL_COUNT.initializer());

                    //HappyGhastMod.LOGGER.info(String.valueOf(data));

                    if(data >= 1000) {
                        HappyGhastCriteria.THOUSAND_SNOWBALL.trigger(serverPlayer);
                    }

                    if(data >= 10000) {
                        HappyGhastCriteria.GOLDEN_SNOWBALL.trigger(serverPlayer);
                    }else {
                        serverPlayer.setAttached(HappyGhastAttachments.SNOWBALL_COUNT, data + 1);
                    }
                    player.getStackInHand(hand).decrementUnlessCreative(1, player);
                    return ActionResult.SUCCESS;
                }

                return ActionResult.PASS;
            }else if(player.getStackInHand(hand).getItem() == HappyGhastItems.SNOWBALL_GOLDEN_ITEM) {
                if (this.getWorld() instanceof ServerWorld serverWorld) {
                    this.playSound(SoundEvents.BLOCK_SNOW_BREAK, this.getSoundVolume(), 0.5f);
                    serverWorld.spawnParticles(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 1, 0, 0.5, 2.3, 0.5);
                    this.heal(24.0F);
                }

                return ActionResult.SUCCESS;
            } else if(!player.shouldCancelInteraction()) {
                if(this.getPassengerList().size() < 4) {
                    this.playSound(HappyGhastSounds.HAPPY_GHAST_RIDE, this.getSoundVolume(), 1.0f);
                    ridePlayer(player);
                    return ActionResult.SUCCESS;
                }
            }else {
                this.openInventory(player);
                return ActionResult.SUCCESS;
            }
            return super.interactMob(player, hand);
        } else {
            if(player.getStackInHand(hand).getItem() == Items.SNOWBALL) {
                if(this.getWorld() instanceof ServerWorld serverWorld) {
                    this.heal(1F);
                    this.playSound(SoundEvents.BLOCK_SNOW_BREAK, this.getSoundVolume(), 1.0f);
                    serverWorld.spawnParticles(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 1, 0, 0.5, 2.3, 0.5);
                    ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                    int data = serverPlayer.getAttachedOrCreate(HappyGhastAttachments.SNOWBALL_COUNT, HappyGhastAttachments.SNOWBALL_COUNT.initializer());

                    //HappyGhastMod.LOGGER.info(String.valueOf(data));

                    if(data >= 1000) {
                        HappyGhastCriteria.THOUSAND_SNOWBALL.trigger(serverPlayer);
                    }

                    if(data >= 10000) {
                        HappyGhastCriteria.GOLDEN_SNOWBALL.trigger(serverPlayer);
                    }else {
                        serverPlayer.setAttached(HappyGhastAttachments.SNOWBALL_COUNT, data + 1);
                    }
                    player.getStackInHand(hand).decrementUnlessCreative(1, player);
                    return ActionResult.SUCCESS;
                }

                return ActionResult.PASS;
            }else if(player.getStackInHand(hand).getItem() == HappyGhastItems.SNOWBALL_GOLDEN_ITEM) {
                if(this.getWorld() instanceof ServerWorld serverWorld) {
                    this.playSound(SoundEvents.BLOCK_SNOW_BREAK, this.getSoundVolume(), 0.5f);
                    serverWorld.spawnParticles(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 1, 0, 0.5, 2.3, 0.5);
                    this.heal(24.0F);
                }

                return ActionResult.SUCCESS;
            }
            ActionResult actionResult = super.interactMob(player, hand);
            if (!actionResult.isAccepted()) {
                ItemStack itemStack = player.getStackInHand(hand);
                if(this.canEquip(itemStack, EquipmentSlot.SADDLE)) {
                    this.playSound(HappyGhastSounds.HAPPY_GHAST_HARNESS_EQUIP, this.getSoundVolume(), 1.0f);
                }
                return (ActionResult)(this.canEquip(itemStack, EquipmentSlot.SADDLE) ? itemStack.useOnEntity(player, this, hand) : ActionResult.PASS);
            } else {
                return actionResult;
            }
        }
    }

    protected void ridePlayer(PlayerEntity player) {
        if(!this.getWorld().isClient()) {
            player.setYaw(this.getYaw());
            player.startRiding(this);
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (!this.hasControllingPassenger()) {
            this.stopMovement();
        }
    }
    static {
        SHOOTING = DataTracker.registerData(HappyGhast.class, TrackedDataHandlerRegistry.BOOLEAN);
        COOLDOWN = DataTracker.registerData(HappyGhast.class, TrackedDataHandlerRegistry.INTEGER);
        SPEED_UPGRADE = DataTracker.registerData(HappyGhast.class, TrackedDataHandlerRegistry.BOOLEAN);
        STRENGTH_UPGRADE = DataTracker.registerData(HappyGhast.class, TrackedDataHandlerRegistry.BOOLEAN);
        HEART_UPGRADE = DataTracker.registerData(HappyGhast.class, TrackedDataHandlerRegistry.BOOLEAN);
        FIREBALL_UPGRADE = DataTracker.registerData(HappyGhast.class, TrackedDataHandlerRegistry.BOOLEAN);
        ROYALTY_UPGRADE = DataTracker.registerData(HappyGhast.class, TrackedDataHandlerRegistry.BOOLEAN);
        AGED_VALUE = DataTracker.registerData(HappyGhast.class, TrackedDataHandlerRegistry.INTEGER);
        AGED_COUNTER = DataTracker.registerData(HappyGhast.class, TrackedDataHandlerRegistry.INTEGER);
        IN_NETHER = DataTracker.registerData(HappyGhast.class, TrackedDataHandlerRegistry.BOOLEAN);
        IS_CONTROLLED = DataTracker.registerData(HappyGhast.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
    }

    @Override
    public @Nullable Entity getOwner() {
        return null;
    }

    @Override
    public void openInventory(PlayerEntity player) {
        if (!this.getWorld().isClient && (!this.hasPassengers() || this.hasPassenger(player))) {
            player.openHandledScreen(new SimpleNamedScreenHandlerFactory(((syncId, playerInventory, playerEntity) -> new HappyGhastUpgradeScreenHandler(syncId, playerInventory, this.items)), this.getName()));
        }
    }

    public final int getInventorySize() {
        return 5;
    }

    protected void setupInventory() {
        SimpleInventory simpleInventory = this.items;
        this.items = new SimpleInventory(getInventorySize());
        if (simpleInventory != null) {
            int i = Math.min(simpleInventory.size(), this.items.size());

            for(int j = 0; j < i; ++j) {
                ItemStack itemStack = simpleInventory.getStack(j);
                if (!itemStack.isEmpty()) {
                    this.items.setStack(j, itemStack.copy());
                }
            }

            this.items.addListener(new HappyGhastInventoryChanged(this));
        }
    }

    public StackReference getStackReference(int mappedIndex) {
        int i = mappedIndex - 500;
        return i >= 0 && i < this.items.size() ? StackReference.of(this.items, i) : super.getStackReference(mappedIndex);
    }

    public boolean areInventoriesDifferent(Inventory inventory) {
        return this.items != inventory;
    }

    public int getInventoryColumns() {
        return 3;
    }

    @Override
    public void dropInventory(ServerWorld world) {
        super.dropInventory(world);
        if (this.items != null) {
            for(int i = 0; i < this.items.size(); ++i) {
                ItemStack itemStack = this.items.getStack(i);
                if (!itemStack.isEmpty() && !EnchantmentHelper.hasAnyEnchantmentsWith(itemStack, EnchantmentEffectComponentTypes.PREVENT_EQUIPMENT_DROP)) {
                    this.dropStack(world, itemStack);
                }
            }

        }
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    public int getCooldown() {
        return this.dataTracker.get(COOLDOWN);
    }

    public void setCooldown(int value) {
        this.dataTracker.set(COOLDOWN, value);
    }

    public boolean getRoyalty() {
        return this.dataTracker.get(ROYALTY_UPGRADE);
    }

    public boolean getSpeedUpg() {
        return this.dataTracker.get(SPEED_UPGRADE);
    }

    public boolean getStrength() {
        return this.dataTracker.get(STRENGTH_UPGRADE);
    }

    public boolean getHeart() {
        return this.dataTracker.get(HEART_UPGRADE);
    }

    public boolean getFireball() {
        return this.dataTracker.get(FIREBALL_UPGRADE);
    }

    public int getAgedValue() {
        return this.dataTracker.get(AGED_VALUE);
    }

    public int getAgedCounter() {
        return this.dataTracker.get(AGED_COUNTER);
    }

    public boolean getInNether() {
        return this.dataTracker.get(IN_NETHER);
    }

    public boolean getIsControlled() {
        return this.dataTracker.get(IS_CONTROLLED);
    }

    private void setRoyalty(boolean value) {
        this.dataTracker.set(ROYALTY_UPGRADE, value);
    }

    private void setSpeed(boolean value) {
        this.dataTracker.set(SPEED_UPGRADE, value);
    }

    private void setStrength(boolean value) {
        this.dataTracker.set(STRENGTH_UPGRADE, value);
    }

    private void setFireball(boolean value) {
        this.dataTracker.set(FIREBALL_UPGRADE, value);
    }

    private void setHeart(boolean value) {
        this.dataTracker.set(HEART_UPGRADE, value);
        if(Boolean.TRUE.equals(value)) {
            if(Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MAX_HEALTH)).getModifier(Identifier.of(HappyGhastMod.MOD_ID, "ghast_max_health_modifier")) == null) {
                Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.MAX_HEALTH)).addPersistentModifier(new EntityAttributeModifier(Identifier.of(HappyGhastMod.MOD_ID, "ghast_max_health_modifier"), 20.0F, EntityAttributeModifier.Operation.ADD_VALUE));
            }
        }
    }

    private void setAgedValue(int value) {
        this.dataTracker.set(AGED_VALUE, value);
    }

    private void setAgedCounter(int value) {
        this.dataTracker.set(AGED_COUNTER, value);
    }

    private void setInNether(boolean value) {
        this.dataTracker.set(IN_NETHER, value);
    }

    private void setIsControlled(boolean value) {
        this.dataTracker.set(IS_CONTROLLED, value);
    }

    private void dimensionGhast() {
        if(this.getWorld() instanceof ServerWorld world) {
            DynamicRegistryManager registryManager = world.getRegistryManager();
            for(int i = 0; i < world.getPlayers().size(); i++) {
                HappyGhastCriteria.SPAWN_HAPPY_GHAST.trigger(world.getPlayers().get(i));
                if(Objects.equals(world.getDimensionEntry(), registryManager.getOrThrow(RegistryKeys.DIMENSION_TYPE).getEntry(DimensionTypes.THE_NETHER.getValue()).get())) {
                    HappyGhastCriteria.HAPPY_GHAST_NETHER.trigger(world.getPlayers().get(i));
                    setInNether(true);
                }else if(Objects.equals(world.getDimensionEntry(), registryManager.getOrThrow(RegistryKeys.DIMENSION_TYPE).getEntry(DimensionTypes.THE_END.getValue()).get())) {
                    HappyGhastCriteria.HAPPY_GHAST_END.trigger(world.getPlayers().get(i));
                    setInNether(false);
                }else {
                    setInNether(false);
                }
            }
        }
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
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.isSubmergedInWater()) {
            this.updateVelocity(0.02F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 100, 1));
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

            if(this.getSpeedUpg() && this.hasControllingPassenger()) {
                this.updateVelocity(this.isOnGround() ? 0.1F * g : 0.02F * 1.06F, movementInput.multiply(1.06F));
                this.move(MovementType.SELF, this.getVelocity().multiply(1.06F));
                this.setVelocity(this.getVelocity().multiply(f).multiply(1.06F));
            }else {
                this.updateVelocity(this.isOnGround() ? 0.1F * g : 0.02F, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(f));
            }
        }
    }

    static class GhastMoveControl extends MoveControl {
        private final HappyGhast ghast;
        private int collisionCheckCooldown;

        public GhastMoveControl(HappyGhast ghast) {
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
                    double s = 0.1;
                    if (this.willCollide(vec3d, MathHelper.ceil(d))) {
                        this.ghast.setVelocity(this.ghast.getVelocity().add(vec3d.multiply(s)));
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

    static class HappyGhastInventoryChanged implements InventoryChangedListener {
        private final HappyGhast ghast;

        public HappyGhastInventoryChanged(HappyGhast ghast) {
            this.ghast = ghast;
        }

        @Override
        public void onInventoryChanged(Inventory sender) {
            for(int i = 0; i < sender.size(); i++) {
                if(sender.getStack(i).isOf(HappyGhastItems.HAPPY_GHAST_SPEED_UPGRADE)) {
                    ghast.setSpeed(true);
                }else if(sender.getStack(i).isOf(HappyGhastItems.HAPPY_GHAST_STRENGTH_UPGRADE)) {
                    ghast.setStrength(true);
                }else if(sender.getStack(i).isOf(HappyGhastItems.HAPPY_GHAST_HEART_UPGRADE)) {
                    ghast.setHeart(true);
                }else if(sender.getStack(i).isOf(HappyGhastItems.HAPPY_GHAST_FIREBALL_UPGRADE)) {
                    ghast.setFireball(true);
                }else if(sender.getStack(i).isOf(HappyGhastItems.HAPPY_GHAST_ROYALTY_UPGRADE)) {
                    ghast.setRoyalty(true);
                }
            }
        }
    }
}
