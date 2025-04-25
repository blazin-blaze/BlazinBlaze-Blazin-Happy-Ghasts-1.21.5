package net.blazinblaze.happyghastmod.achievement;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class HappyGhastSpawnCriterion extends AbstractCriterion<HappyGhastSpawnCriterion.Conditions> {

    @Override
    public Codec<HappyGhastSpawnCriterion.Conditions> getConditionsCodec() {
        return HappyGhastSpawnCriterion.Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate) implements AbstractCriterion.Conditions {
        public static Codec<HappyGhastSpawnCriterion.Conditions> CODEC = LootContextPredicate.CODEC.optionalFieldOf("player")
                .xmap(HappyGhastSpawnCriterion.Conditions::new, HappyGhastSpawnCriterion.Conditions::player).codec();

        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

        public boolean requirementsMet() {
            return true;
        }

    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, HappyGhastSpawnCriterion.Conditions::requirementsMet);
    }

}
