package net.blazinblaze.happyghastmod.achievement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class GoldenSnowballCriterion extends AbstractCriterion<GoldenSnowballCriterion.Conditions> {

    @Override
    public Codec<GoldenSnowballCriterion.Conditions> getConditionsCodec() {
        return GoldenSnowballCriterion.Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate) implements AbstractCriterion.Conditions {
        public static Codec<GoldenSnowballCriterion.Conditions> CODEC = LootContextPredicate.CODEC.optionalFieldOf("player")
                .xmap(GoldenSnowballCriterion.Conditions::new, GoldenSnowballCriterion.Conditions::player).codec();

        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

        public boolean requirementsMet() {
            return true;
        }

    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, GoldenSnowballCriterion.Conditions::requirementsMet);
    }

}
