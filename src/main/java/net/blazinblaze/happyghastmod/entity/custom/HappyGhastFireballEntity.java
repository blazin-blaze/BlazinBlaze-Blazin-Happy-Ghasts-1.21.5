package net.blazinblaze.happyghastmod.entity.custom;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.achievement.HappyGhastCriteria;
import net.blazinblaze.happyghastmod.entity.HappyGhastEntities;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class HappyGhastFireballEntity extends AbstractHappyGhastFireballEntity {
    private static final byte DEFAULT_EXPLOSION_POWER = 5;
    private static final TrackedData<String> PLAYER_REWARD;
    private int explosionPower = 5;

    public HappyGhastFireballEntity(EntityType<? extends HappyGhastFireballEntity> entityType, World world) {
        super(entityType, world);
    }

    public HappyGhastFireballEntity(World world, LivingEntity owner, Vec3d velocity, int explosionPower, String playerUUID) {
        super(HappyGhastEntities.HAPPY_GHAST_FIREBALL_ENTITY, owner, velocity, world);
        this.explosionPower = explosionPower;
        this.setPlayerReward(playerUUID);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            boolean bl = serverWorld.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), this.explosionPower, bl, World.ExplosionSourceType.TNT);
            this.discard();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            Entity var6 = entityHitResult.getEntity();
            Entity entity2 = this.getOwner();
            DamageSource damageSource = this.getDamageSources().fireball(this, entity2);
            var6.damage(serverWorld, damageSource, 6.0F);
            EnchantmentHelper.onTargetDamaged(serverWorld, var6, damageSource);
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(PLAYER_REWARD, "");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putByte("ExplosionPower", (byte)this.explosionPower);
        nbt.putString("player_reward", getPlayerReward());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.explosionPower = nbt.getByte("ExplosionPower", (byte)5);
        this.setPlayerReward(nbt.getString("player_reward", ""));
    }

    public String getPlayerReward() {
        return this.dataTracker.get(PLAYER_REWARD);
    }

    public void setPlayerReward(String UUID) {
        this.dataTracker.set(PLAYER_REWARD, UUID);
    }

    static {
        PLAYER_REWARD = DataTracker.registerData(HappyGhastFireballEntity.class, TrackedDataHandlerRegistry.STRING);
    }
}
