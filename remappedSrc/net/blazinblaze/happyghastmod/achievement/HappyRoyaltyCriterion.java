package net.blazinblaze.happyghastmod.achievement;

import com.mojang.serialization.Codec;
import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class HappyRoyaltyCriterion extends AbstractCriterion<HappyRoyaltyCriterion.Conditions> {

    @Override
    public Codec<HappyRoyaltyCriterion.Conditions> getConditionsCodec() {
        return HappyRoyaltyCriterion.Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> playerPredicate) implements AbstractCriterion.Conditions {
        public static Codec<HappyRoyaltyCriterion.Conditions> CODEC = LootContextPredicate.CODEC.optionalFieldOf("player")
                .xmap(HappyRoyaltyCriterion.Conditions::new, HappyRoyaltyCriterion.Conditions::player).codec();

        @Override
        public Optional<LootContextPredicate> player() {
            return playerPredicate;
        }

        public boolean requirementsMet() {
            return true;
        }

    }

    public void trigger(ServerPlayerEntity player) {
        MinecraftServer server = HappyGhastMod.serverRef;
        if(server != null) {
            ServerAdvancementLoader loader = server.getAdvancementLoader();
            PlayerAdvancementTracker tracker = player.getAdvancementTracker();
            if(tracker.getProgress(loader.get(Identifier.of(HappyGhastMod.MOD_ID, "spawn_ghastling"))).isDone() &&
                    tracker.getProgress(loader.get(Identifier.of(HappyGhastMod.MOD_ID, "spawn_happy_ghast"))).isDone() &&
                    tracker.getProgress(loader.get(Identifier.of(HappyGhastMod.MOD_ID, "ghast_speed_upgrade"))).isDone() &&
                    tracker.getProgress(loader.get(Identifier.of(HappyGhastMod.MOD_ID, "thousand_snowball"))).isDone() &&
                    tracker.getProgress(loader.get(Identifier.of(HappyGhastMod.MOD_ID, "golden_snowball"))).isDone() &&
                    tracker.getProgress(loader.get(Identifier.of(HappyGhastMod.MOD_ID, "ghast_strength_upgrade"))).isDone() &&
                    tracker.getProgress(loader.get(Identifier.of(HappyGhastMod.MOD_ID, "ghast_heart_upgrade"))).isDone() &&
                    tracker.getProgress(loader.get(Identifier.of(HappyGhastMod.MOD_ID, "ghast_fireball_upgrade"))).isDone() &&
                    tracker.getProgress(loader.get(Identifier.of(HappyGhastMod.MOD_ID, "happy_ghast_nether"))).isDone() &&
                    tracker.getProgress(loader.get(Identifier.of(HappyGhastMod.MOD_ID, "absolute_betrayal"))).isDone() &&
                    tracker.getProgress(loader.get(Identifier.of(HappyGhastMod.MOD_ID, "happy_ghast_end"))).isDone() &&
                    tracker.getProgress(loader.get(Identifier.of(HappyGhastMod.MOD_ID, "free_ghast"))).isDone() &&
                    tracker.getProgress(loader.get(Identifier.of(HappyGhastMod.MOD_ID, "friendly_fire"))).isDone()) {
                trigger(player, HappyRoyaltyCriterion.Conditions::requirementsMet);
            }
        }
    }

}
