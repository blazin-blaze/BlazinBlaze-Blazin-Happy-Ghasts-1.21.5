package net.blazinblaze.happyghastmod.achievement;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class FriendlyFireCriterion extends AbstractCriterion<FriendlyFireCriterion.Conditions> {

    @Override
    public Codec<FriendlyFireCriterion.Conditions> getConditionsCodec() {
        return FriendlyFireCriterion.Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate) implements AbstractCriterion.Conditions {
        public static Codec<FriendlyFireCriterion.Conditions> CODEC = LootContextPredicate.CODEC.optionalFieldOf("player")
                .xmap(FriendlyFireCriterion.Conditions::new, FriendlyFireCriterion.Conditions::player).codec();

        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

        public boolean requirementsMet() {
            return true;
        }

    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, FriendlyFireCriterion.Conditions::requirementsMet);
    }

}
