package net.blazinblaze.happyghastmod.datagen;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.achievement.*;
import net.blazinblaze.happyghastmod.block.HappyGhastBlocks;
import net.blazinblaze.happyghastmod.block.custom.HydratedGhast;
import net.blazinblaze.happyghastmod.item.HappyGhastItems;
import net.blazinblaze.happyghastmod.loot.HappyGhastLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.*;
import net.minecraft.advancement.criterion.*;
import net.minecraft.data.DataOutput;
import net.minecraft.data.advancement.AdvancementProvider;
import net.minecraft.data.advancement.vanilla.VanillaAdvancementProviders;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class HappyGhastAdvancementProvider extends FabricAdvancementProvider {
    public HappyGhastAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup wrapperLookup, Consumer<AdvancementEntry> consumer) {
        AdvancementEntry happyGhast = Advancement.Builder.create()
                .display(
                        HappyGhastItems.HAPPY_GHAST_ICON,
                        Text.literal("Happy Ghasts!"),
                        Text.literal("This is the start of a happy journey."),
                        Identifier.of(HappyGhastMod.MOD_ID, "gui/advancements/backgrounds/happy_ghast_advancements"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("happy_ghast_criterion", Criteria.TICK.create(TickCriterion.Conditions.createTick().conditions()))
                .build(consumer, HappyGhastMod.MOD_ID + ":happy_ghast");

        AdvancementEntry spawnGhastling = Advancement.Builder.create()
                .parent(happyGhast)
                .display(
                        HappyGhastBlocks.HYDRATED_GHAST.asItem(),
                        Text.literal("Rehydrated!"),
                        Text.literal("Spawn a Ghastling."),
                        Identifier.of(HappyGhastMod.MOD_ID, "gui/advancements/backgrounds/happy_ghast_advancements"),
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("spawn_ghastling_criterion", HappyGhastCriteria.SPAWN_GHASTLING.create(new GhastlingSpawnCriterion.Conditions(Optional.empty())))
                .build(consumer, HappyGhastMod.MOD_ID + ":spawn_ghastling");

        AdvancementEntry spawnHappyGhast = Advancement.Builder.create()
                .parent(spawnGhastling)
                .display(
                        HappyGhastItems.HAPPY_GHAST_GROWN_ICON,
                        Text.literal("A Happy Companion"),
                        Text.literal("Spawn a Happy Ghast."),
                        Identifier.of(HappyGhastMod.MOD_ID, "gui/advancements/backgrounds/happy_ghast_advancements"),
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("spawn_happy_ghast_criterion", HappyGhastCriteria.SPAWN_HAPPY_GHAST.create(new HappyGhastSpawnCriterion.Conditions(Optional.empty())))
                .build(consumer, HappyGhastMod.MOD_ID + ":spawn_happy_ghast");

        final RegistryWrapper.Impl<Item> itemLookup = wrapperLookup.getOrThrow(RegistryKeys.ITEM);
        AdvancementEntry ghastSpeedUpgrade = Advancement.Builder.create()
                .parent(spawnHappyGhast)
                .display(
                        HappyGhastItems.HAPPY_GHAST_SPEED_UPGRADE,
                        Text.literal("Happy Speed++"),
                        Text.literal("Acquire a Happy Ghast Speed Upgrade."),
                        Identifier.of(HappyGhastMod.MOD_ID, "gui/advancements/backgrounds/happy_ghast_advancements"),
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("ghast_strength_upgrade_criterion", Criteria.INVENTORY_CHANGED.create(new InventoryChangedCriterion.Conditions(Optional.empty(), InventoryChangedCriterion.Conditions.Slots.ANY, Collections.singletonList(ItemPredicate.Builder.create().items(itemLookup, HappyGhastItems.HAPPY_GHAST_SPEED_UPGRADE).build()))))
                .build(consumer, HappyGhastMod.MOD_ID + ":ghast_speed_upgrade");

        AdvancementEntry thousandSnowball = Advancement.Builder.create()
                .parent(spawnHappyGhast)
                .display(
                        Items.SNOWBALL,
                        Text.literal("Hungry Ghast"),
                        Text.literal("Feed a Happy Ghast 1,000 Snowballs"),
                        Identifier.of(HappyGhastMod.MOD_ID, "gui/advancements/backgrounds/happy_ghast_advancements"),
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("thousand_snowball_criterion", HappyGhastCriteria.THOUSAND_SNOWBALL.create(new ThousandSnowballCriterion.Conditions(Optional.empty())))
                .build(consumer, HappyGhastMod.MOD_ID + ":thousand_snowball");

        AdvancementEntry goldenSnowball = Advancement.Builder.create()
                .parent(thousandSnowball)
                .display(
                        HappyGhastItems.SNOWBALL_GOLDEN_ITEM,
                        Text.literal("Starving Ghast"),
                        Text.literal("Feed a Happy Ghast 10,000 Snowballs"),
                        Identifier.of(HappyGhastMod.MOD_ID, "gui/advancements/backgrounds/happy_ghast_advancements"),
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .criterion("golden_snowball_criterion", HappyGhastCriteria.GOLDEN_SNOWBALL.create(new GoldenSnowballCriterion.Conditions(Optional.empty())))
                .rewards(AdvancementRewards.Builder.loot(HappyGhastLootTables.GOLDEN_SNOWBALL_LOOT_TABLE).build())
                .build(consumer, HappyGhastMod.MOD_ID + ":golden_snowball");

        AdvancementEntry ghastStrengthUpgrade = Advancement.Builder.create()
                .parent(ghastSpeedUpgrade)
                .display(
                        HappyGhastItems.HAPPY_GHAST_STRENGTH_UPGRADE,
                        Text.literal("Happy Strength++"),
                        Text.literal("Acquire a Happy Ghast Strength Upgrade."),
                        Identifier.of(HappyGhastMod.MOD_ID, "gui/advancements/backgrounds/happy_ghast_advancements"),
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("ghast_strength_upgrade_criterion", Criteria.INVENTORY_CHANGED.create(new InventoryChangedCriterion.Conditions(Optional.empty(), InventoryChangedCriterion.Conditions.Slots.ANY, Collections.singletonList(ItemPredicate.Builder.create().items(itemLookup, HappyGhastItems.HAPPY_GHAST_STRENGTH_UPGRADE).build()))))
                .build(consumer, HappyGhastMod.MOD_ID + ":ghast_strength_upgrade");

        AdvancementEntry ghastHeartUpgrade = Advancement.Builder.create()
                .parent(ghastStrengthUpgrade)
                .display(
                        HappyGhastItems.HAPPY_GHAST_HEART_UPGRADE,
                        Text.literal("Happy Heart++"),
                        Text.literal("Acquire a Happy Ghast Heart Upgrade."),
                        Identifier.of(HappyGhastMod.MOD_ID, "gui/advancements/backgrounds/happy_ghast_advancements"),
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("ghast_strength_upgrade_criterion", Criteria.INVENTORY_CHANGED.create(new InventoryChangedCriterion.Conditions(Optional.empty(), InventoryChangedCriterion.Conditions.Slots.ANY, Collections.singletonList(ItemPredicate.Builder.create().items(itemLookup, HappyGhastItems.HAPPY_GHAST_HEART_UPGRADE).build()))))
                .build(consumer, HappyGhastMod.MOD_ID + ":ghast_heart_upgrade");

        AdvancementEntry ghastFireballUpgrade = Advancement.Builder.create()
                .parent(ghastHeartUpgrade)
                .display(
                        HappyGhastItems.HAPPY_GHAST_FIREBALL_UPGRADE,
                        Text.literal("Happy Fireballs++"),
                        Text.literal("Acquire a Happy Ghast Fireball Upgrade."),
                        Identifier.of(HappyGhastMod.MOD_ID, "gui/advancements/backgrounds/happy_ghast_advancements"),
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("ghast_strength_upgrade_criterion", Criteria.INVENTORY_CHANGED.create(new InventoryChangedCriterion.Conditions(Optional.empty(), InventoryChangedCriterion.Conditions.Slots.ANY, Collections.singletonList(ItemPredicate.Builder.create().items(itemLookup, HappyGhastItems.HAPPY_GHAST_FIREBALL_UPGRADE).build()))))
                .build(consumer, HappyGhastMod.MOD_ID + ":ghast_fireball_upgrade");

        AdvancementEntry happyGhastNether = Advancement.Builder.create()
                .parent(spawnHappyGhast)
                .display(
                        HappyGhastItems.HAPPY_GHAST_WORRIED_ICON,
                        Text.literal("Not-so-happy Trauma"),
                        Text.literal("Bring a Happy Ghast back to the Nether."),
                        Identifier.of(HappyGhastMod.MOD_ID, "gui/advancements/backgrounds/happy_ghast_advancements"),
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .criterion("happy_ghast_nether_criterion", HappyGhastCriteria.HAPPY_GHAST_NETHER.create(new HappyGhastNetherCriterion.Conditions(Optional.empty())))
                .build(consumer, HappyGhastMod.MOD_ID + ":happy_ghast_nether");

        AdvancementEntry absoluteBetrayal = Advancement.Builder.create()
                .parent(happyGhastNether)
                .display(
                        HappyGhastItems.HAPPY_GHAST_NETHER_ICON,
                        Text.literal("Absolute Betrayal."),
                        Text.literal("Abandon a Happy Ghast in the Nether."),
                        Identifier.of(HappyGhastMod.MOD_ID, "gui/advancements/backgrounds/happy_ghast_advancements"),
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .criterion("absolute_betrayal_criterion", HappyGhastCriteria.ABSOLUTE_BETRAYAL.create(new AbsoluteBetrayalCriterion.Conditions(Optional.empty())))
                .build(consumer, HappyGhastMod.MOD_ID + ":absolute_betrayal");

        AdvancementEntry happyGhastEnd = Advancement.Builder.create()
                .parent(spawnHappyGhast)
                .display(
                        HappyGhastItems.HAPPY_GHAST_END_ICON,
                        Text.literal("To The End and Back"),
                        Text.literal("Bring a Happy Ghast to The End."),
                        Identifier.of(HappyGhastMod.MOD_ID, "gui/advancements/backgrounds/happy_ghast_advancements"),
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .criterion("happy_ghast_end_criterion", HappyGhastCriteria.HAPPY_GHAST_END.create(new HappyGhastEndCriterion.Conditions(Optional.empty())))
                .build(consumer, HappyGhastMod.MOD_ID + ":happy_ghast_end");

        AdvancementEntry freeGhast = Advancement.Builder.create()
                .parent(spawnHappyGhast)
                .display(
                        HappyGhastItems.HAPPY_GHAST_FREED_ICON,
                        Text.literal("Easy Alliance"),
                        Text.literal("Free a ghast from the Nether, and its suffering."),
                        Identifier.of(HappyGhastMod.MOD_ID, "gui/advancements/backgrounds/happy_ghast_advancements"),
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .criterion("free_ghast_criterion", HappyGhastCriteria.FREE_GHAST.create(new FreeGhastCriterion.Conditions(Optional.empty())))
                .build(consumer, HappyGhastMod.MOD_ID + ":free_ghast");

        AdvancementEntry friendlyFire = Advancement.Builder.create()
                .parent(spawnHappyGhast)
                .display(
                        HappyGhastItems.FRIENDLY_FIRE_ICON,
                        Text.literal("Friendly Fire"),
                        Text.literal("Kill a Ghast with a Happy Ghast's fireball"),
                        Identifier.of(HappyGhastMod.MOD_ID, "gui/advancements/backgrounds/happy_ghast_advancements"),
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                )
                .criterion("friendly_fire_criterion", HappyGhastCriteria.FRIENDLY_FIRE.create(new FriendlyFireCriterion.Conditions(Optional.empty())))
                .build(consumer, HappyGhastMod.MOD_ID + ":friendly_fire");
    }
}
