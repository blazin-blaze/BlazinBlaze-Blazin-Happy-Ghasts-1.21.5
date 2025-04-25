package net.blazinblaze.happyghastmod.achievement;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.minecraft.advancement.criterion.Criteria;

public class HappyGhastCriteria {
    public static final FreeGhastCriterion FREE_GHAST = Criteria.register(HappyGhastMod.MOD_ID + ":free_ghast_criterion", new FreeGhastCriterion());
    public static final HappyGhastSpawnCriterion SPAWN_HAPPY_GHAST = Criteria.register(HappyGhastMod.MOD_ID + ":spawn_happy_ghast_criterion", new HappyGhastSpawnCriterion());
    public static final GhastlingSpawnCriterion SPAWN_GHASTLING = Criteria.register(HappyGhastMod.MOD_ID + ":spawn_ghastling_criterion", new GhastlingSpawnCriterion());
    public static final HappyGhastNetherCriterion HAPPY_GHAST_NETHER = Criteria.register(HappyGhastMod.MOD_ID + ":happy_ghast_nether_criterion", new HappyGhastNetherCriterion());
    public static final HappyGhastEndCriterion HAPPY_GHAST_END = Criteria.register(HappyGhastMod.MOD_ID + ":happy_ghast_end_criterion", new HappyGhastEndCriterion());
    public static final AbsoluteBetrayalCriterion ABSOLUTE_BETRAYAL = Criteria.register(HappyGhastMod.MOD_ID + ":absolute_betrayal_criterion", new AbsoluteBetrayalCriterion());
    public static final ThousandSnowballCriterion THOUSAND_SNOWBALL = Criteria.register(HappyGhastMod.MOD_ID + ":thousand_snowball_criterion", new ThousandSnowballCriterion());
    public static final GoldenSnowballCriterion GOLDEN_SNOWBALL = Criteria.register(HappyGhastMod.MOD_ID + ":golden_snowball_criterion", new GoldenSnowballCriterion());
    public static final FriendlyFireCriterion FRIENDLY_FIRE = Criteria.register(HappyGhastMod.MOD_ID + ":friendly_fire_criterion", new FriendlyFireCriterion());
    public static final HappyRoyaltyCriterion HAPPY_ROYALTY = Criteria.register(HappyGhastMod.MOD_ID + ":happy_royalty_criterion", new HappyRoyaltyCriterion());

    public static void init() {

    }

}
