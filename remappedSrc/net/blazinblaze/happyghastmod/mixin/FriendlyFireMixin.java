package net.blazinblaze.happyghastmod.mixin;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.achievement.HappyGhastCriteria;
import net.blazinblaze.happyghastmod.entity.HappyGhastEntities;
import net.blazinblaze.happyghastmod.entity.custom.HappyGhastFireballEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.conversion.EntityConversionContext;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.UUID;

@Mixin(LivingEntity.class)
public abstract class FriendlyFireMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void rewardFriendlyFire(DamageSource damageSource, CallbackInfo ci) {
        if((Object)this instanceof GhastEntity) {
            Entity attacker = damageSource.getSource();
            if(attacker instanceof HappyGhastFireballEntity fireballEntity) {
                MinecraftServer server = HappyGhastMod.serverRef;

                if(server != null) {
                    UUID uuid = UUID.fromString(fireballEntity.getPlayerReward());
                    ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(uuid);

                    if(serverPlayer != null) {
                        HappyGhastCriteria.FRIENDLY_FIRE.trigger(serverPlayer);
                    }
                }
            }
        }
    }
}
