package net.blazinblaze.happyghastmod.achievement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class ThousandSnowballCriterion extends AbstractCriterion<ThousandSnowballCriterion.Conditions> {

    @Override
    public Codec<ThousandSnowballCriterion.Conditions> getConditionsCodec() {
        return ThousandSnowballCriterion.Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate) implements AbstractCriterion.Conditions {
        public static Codec<ThousandSnowballCriterion.Conditions> CODEC = LootContextPredicate.CODEC.optionalFieldOf("player")
                .xmap(ThousandSnowballCriterion.Conditions::new, ThousandSnowballCriterion.Conditions::player).codec();

        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

        public boolean requirementsMet() {
            return true;
        }

    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, ThousandSnowballCriterion.Conditions::requirementsMet);
    }

}
