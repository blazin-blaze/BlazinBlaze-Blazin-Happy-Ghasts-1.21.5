package net.blazinblaze.happyghastmod.achievement;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class HappyGhastNetherCriterion extends AbstractCriterion<HappyGhastNetherCriterion.Conditions> {

    @Override
    public Codec<HappyGhastNetherCriterion.Conditions> getConditionsCodec() {
        return HappyGhastNetherCriterion.Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate) implements AbstractCriterion.Conditions {
        public static Codec<HappyGhastNetherCriterion.Conditions> CODEC = LootContextPredicate.CODEC.optionalFieldOf("player")
                .xmap(HappyGhastNetherCriterion.Conditions::new, HappyGhastNetherCriterion.Conditions::player).codec();

        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

        public boolean requirementsMet() {
            return true;
        }

    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, HappyGhastNetherCriterion.Conditions::requirementsMet);
    }

}
