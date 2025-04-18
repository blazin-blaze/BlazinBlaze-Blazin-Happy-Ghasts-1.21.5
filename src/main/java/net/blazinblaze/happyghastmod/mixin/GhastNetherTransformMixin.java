package net.blazinblaze.happyghastmod.mixin;


import net.blazinblaze.happyghastmod.achievement.HappyGhastCriteria;
import net.blazinblaze.happyghastmod.entity.HappyGhastEntities;
import net.blazinblaze.happyghastmod.entity.custom.Ghastling;
import net.blazinblaze.happyghastmod.entity.custom.HappyGhast;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.conversion.EntityConversionContext;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.CatVariant;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionTypes;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Optional;

@Mixin(MobEntity.class)
public abstract class GhastNetherTransformMixin {
    @Shadow @Nullable public abstract <T extends MobEntity> T convertTo(EntityType<T> entityType, EntityConversionContext context, EntityConversionContext.Finalizer<T> finalizer);

    @Shadow protected abstract void dropEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer);

    @Shadow
    public abstract @Nullable Leashable.LeashData getLeashData();

    @Shadow @Nullable private Leashable.@Nullable LeashData leashData;

    @Shadow public abstract void setLeashData(Leashable.@Nullable LeashData leashData);

    @Unique
    int happyGhastCounter = 0;

    @Inject(method = "mobTick", at = @At("HEAD"))
    private void zombify2(ServerWorld world, CallbackInfo info) {
        if((Object)this instanceof HappyGhast) {
            if(happyGhastCounter >= 120*20) {
                DynamicRegistryManager registryManager = world.getRegistryManager();
                if(Objects.equals(world.getDimensionEntry(), registryManager.getOrThrow(RegistryKeys.DIMENSION_TYPE).getEntry(DimensionTypes.THE_NETHER.getValue()).get())) {
                    this.dropEquipment(world, world.getDamageSources().create(DamageTypes.GENERIC), false);
                    ((HappyGhast)(Object)this).dropInventory(world);
                    if(this.leashData != null) {
                        this.setLeashData(null);
                    }
                    this.convertTo(EntityType.GHAST, EntityConversionContext.create((MobEntity) ((Object)this), false, true), (happyGhast) -> happyGhast.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20*5, 1, true, true)));
                    for(int i = 0; i < world.getPlayers().size(); i++) {
                        HappyGhastCriteria.ABSOLUTE_BETRAYAL.trigger(world.getPlayers().get(i));
                    }
                    happyGhastCounter = 0;
                }
            }else {
                happyGhastCounter++;
            }
        }
    }
}
