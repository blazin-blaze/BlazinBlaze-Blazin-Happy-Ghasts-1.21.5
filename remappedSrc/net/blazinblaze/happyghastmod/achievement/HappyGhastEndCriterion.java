package net.blazinblaze.happyghastmod.achievement;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class HappyGhastEndCriterion extends AbstractCriterion<HappyGhastEndCriterion.Conditions> {

    @Override
    public Codec<HappyGhastEndCriterion.Conditions> getConditionsCodec() {
        return HappyGhastEndCriterion.Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate) implements AbstractCriterion.Conditions {
        public static Codec<HappyGhastEndCriterion.Conditions> CODEC = LootContextPredicate.CODEC.optionalFieldOf("player")
                .xmap(HappyGhastEndCriterion.Conditions::new, HappyGhastEndCriterion.Conditions::player).codec();

        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

        public boolean requirementsMet() {
            return true;
        }

    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, HappyGhastEndCriterion.Conditions::requirementsMet);
    }

}
