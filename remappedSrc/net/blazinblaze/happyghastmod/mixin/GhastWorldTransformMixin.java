package net.blazinblaze.happyghastmod.mixin;


import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.achievement.HappyGhastCriteria;
import net.blazinblaze.happyghastmod.entity.HappyGhastEntities;
import net.blazinblaze.happyghastmod.entity.custom.Ghastling;
import net.blazinblaze.happyghastmod.entity.custom.HappyGhast;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.conversion.EntityConversionContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.structure.NetherFossilStructure;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(MobEntity.class)
public abstract class GhastWorldTransformMixin {
    @Shadow @Nullable public abstract <T extends MobEntity> T convertTo(EntityType<T> entityType, EntityConversionContext context, EntityConversionContext.Finalizer<T> finalizer);
    @Unique
    int happyGhastCounter = 0;

    @Inject(method = "mobTick", at = @At("HEAD"))
    private void zombify(ServerWorld world, CallbackInfo info) {
        if((Object)this instanceof GhastEntity) {
            if(happyGhastCounter >= 120*20) {
                DynamicRegistryManager registryManager = world.getRegistryManager();
                if(Objects.equals(world.getDimensionEntry(), registryManager.getOrThrow(RegistryKeys.DIMENSION_TYPE).getEntry(DimensionTypes.OVERWORLD.getValue()).get())) {
                    this.convertTo(HappyGhastEntities.HAPPY_GHAST, EntityConversionContext.create((MobEntity) ((Object)this), false, false), (happyGhast) -> happyGhast.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 20*5, 1, true, true)));
                    for(int i = 0; i < world.getPlayers().size(); i++) {
                        HappyGhastCriteria.FREE_GHAST.trigger(world.getPlayers().get(i));
                    }
                    happyGhastCounter = 0;
                }
            }else {
                happyGhastCounter++;
            }
        }
    }
}
