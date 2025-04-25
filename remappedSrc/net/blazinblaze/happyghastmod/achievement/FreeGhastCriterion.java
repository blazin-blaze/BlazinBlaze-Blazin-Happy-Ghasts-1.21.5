package net.blazinblaze.happyghastmod.achievement;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class FreeGhastCriterion extends AbstractCriterion<FreeGhastCriterion.Conditions> {

    @Override
    public Codec<net.blazinblaze.happyghastmod.achievement.FreeGhastCriterion.Conditions> getConditionsCodec() {
        return net.blazinblaze.happyghastmod.achievement.FreeGhastCriterion.Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate) implements AbstractCriterion.Conditions {
        public static Codec<FreeGhastCriterion.Conditions> CODEC = LootContextPredicate.CODEC.optionalFieldOf("player")
                .xmap(net.blazinblaze.happyghastmod.achievement.FreeGhastCriterion.Conditions::new, net.blazinblaze.happyghastmod.achievement.FreeGhastCriterion.Conditions::player).codec();

        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

        public boolean requirementsMet() {
            return true;
        }

    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, net.blazinblaze.happyghastmod.achievement.FreeGhastCriterion.Conditions::requirementsMet);
    }

}
