package net.blazinblaze.happyghastmod.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.swing.*;

public abstract class AbstractHappyGhastFireballEntity extends AbstractFireballEntity {
    private static final float MAX_RENDER_DISTANCE_WHEN_NEWLY_SPAWNED = 24.5F;
    private static final TrackedData<ItemStack> ITEM = DataTracker.registerData(AbstractHappyGhastFireballEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    public AbstractHappyGhastFireballEntity(EntityType<? extends AbstractFireballEntity> entityType, World world) {
        super(entityType, world);
    }

    public AbstractHappyGhastFireballEntity(EntityType<? extends AbstractFireballEntity> entityType, double d, double e, double f, Vec3d vec3d, World world) {
        super(entityType, d, e, f, vec3d, world);
    }

    public AbstractHappyGhastFireballEntity(EntityType<? extends AbstractFireballEntity> entityType, LivingEntity livingEntity, Vec3d vec3d, World world) {
        super(entityType, livingEntity, vec3d, world);
    }

    @Override
    public void setItem(ItemStack stack) {
        if (stack.isEmpty()) {
            this.getDataTracker().set(ITEM, this.getItem());
        } else {
            this.getDataTracker().set(ITEM, stack.copyWithCount(1));
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ITEM, this.getItem());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        RegistryOps<NbtElement> registryOps = this.getRegistryManager().getOps(NbtOps.INSTANCE);
        nbt.put("Item", ItemStack.CODEC, registryOps, this.getStack());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        RegistryOps<NbtElement> registryOps = this.getRegistryManager().getOps(NbtOps.INSTANCE);
        this.setItem((ItemStack)nbt.get("Item", ItemStack.CODEC, registryOps).orElse(this.getItem()));
    }

    private ItemStack getItem() {
        return ItemStack.EMPTY;
    }

}
