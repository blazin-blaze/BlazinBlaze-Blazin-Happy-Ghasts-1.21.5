package net.blazinblaze.happyghastmod.achievement;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class GhastlingSpawnCriterion extends AbstractCriterion<GhastlingSpawnCriterion.Conditions> {

    @Override
    public Codec<GhastlingSpawnCriterion.Conditions> getConditionsCodec() {
        return GhastlingSpawnCriterion.Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate) implements AbstractCriterion.Conditions {
        public static Codec<GhastlingSpawnCriterion.Conditions> CODEC = LootContextPredicate.CODEC.optionalFieldOf("player")
                .xmap(GhastlingSpawnCriterion.Conditions::new, GhastlingSpawnCriterion.Conditions::player).codec();

        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

        public boolean requirementsMet() {
            return true;
        }

    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, GhastlingSpawnCriterion.Conditions::requirementsMet);
    }

}
