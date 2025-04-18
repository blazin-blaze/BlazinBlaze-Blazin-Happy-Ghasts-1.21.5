package net.blazinblaze.happyghastmod.achievement;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class AbsoluteBetrayalCriterion extends AbstractCriterion<AbsoluteBetrayalCriterion.Conditions> {

    @Override
    public Codec<AbsoluteBetrayalCriterion.Conditions> getConditionsCodec() {
        return AbsoluteBetrayalCriterion.Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate) implements AbstractCriterion.Conditions {
        public static Codec<AbsoluteBetrayalCriterion.Conditions> CODEC = LootContextPredicate.CODEC.optionalFieldOf("player")
                .xmap(AbsoluteBetrayalCriterion.Conditions::new, AbsoluteBetrayalCriterion.Conditions::player).codec();

        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

        public boolean requirementsMet() {
            return true;
        }

    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, AbsoluteBetrayalCriterion.Conditions::requirementsMet);
    }

}
