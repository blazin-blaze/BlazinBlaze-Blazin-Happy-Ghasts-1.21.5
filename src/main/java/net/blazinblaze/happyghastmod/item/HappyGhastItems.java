package net.blazinblaze.happyghastmod.item;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.blazinblaze.happyghastmod.component.HappyGhastComponents;
import net.blazinblaze.happyghastmod.datagen.HappyGhastAdvancementProvider;
import net.blazinblaze.happyghastmod.entity.HappyGhastEntities;
//import net.blazinblaze.happyghastmod.item.custom.HarnessItem;
import net.blazinblaze.happyghastmod.item.custom.GoldenSnowball;
import net.blazinblaze.happyghastmod.item.custom.HappyGhastRoyaltyUpgrade;
import net.blazinblaze.happyghastmod.item.custom.HappyGhastSoul;
import net.blazinblaze.happyghastmod.item.custom.HappyGhastUpgrade;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class HappyGhastItems {

    public static final Item BLUE_HARNESS = register("blue_harness", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, happyGhastHarness(DyeColor.BLUE)));
    public static final Item WHITE_HARNESS = register("white_harness", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, happyGhastHarness(DyeColor.WHITE)));
    public static final Item LIGHT_GRAY_HARNESS = register("light_gray_harness", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, happyGhastHarness(DyeColor.LIGHT_GRAY)));
    public static final Item GRAY_HARNESS = register("gray_harness", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, happyGhastHarness(DyeColor.GRAY)));
    public static final Item BLACK_HARNESS = register("black_harness", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, happyGhastHarness(DyeColor.BLACK)));
    public static final Item BROWN_HARNESS = register("brown_harness", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, happyGhastHarness(DyeColor.BROWN)));
    public static final Item RED_HARNESS = register("red_harness", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, happyGhastHarness(DyeColor.RED)));
    public static final Item ORANGE_HARNESS = register("orange_harness", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, happyGhastHarness(DyeColor.ORANGE)));
    public static final Item YELLOW_HARNESS = register("yellow_harness", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, happyGhastHarness(DyeColor.YELLOW)));
    public static final Item LIME_HARNESS = register("lime_harness", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, happyGhastHarness(DyeColor.LIME)));
    public static final Item GREEN_HARNESS = register("green_harness", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, happyGhastHarness(DyeColor.GREEN)));
    public static final Item CYAN_HARNESS = register("cyan_harness", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, happyGhastHarness(DyeColor.CYAN)));
    public static final Item LIGHT_BLUE_HARNESS = register("light_blue_harness", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, happyGhastHarness(DyeColor.LIGHT_BLUE)));
    public static final Item PURPLE_HARNESS = register("purple_harness", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, happyGhastHarness(DyeColor.PURPLE)));
    public static final Item MAGENTA_HARNESS = register("magenta_harness", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, happyGhastHarness(DyeColor.MAGENTA)));
    public static final Item PINK_HARNESS = register("pink_harness", Item::new, new Item.Settings().maxCount(1).component(DataComponentTypes.EQUIPPABLE, happyGhastHarness(DyeColor.PINK)));

    public static final Item HAPPY_GHAST_SPEED_UPGRADE = register("happy_ghast_speed_upgrade", HappyGhastUpgrade::new, new Item.Settings().maxCount(1).rarity(Rarity.RARE).component(HappyGhastComponents.IS_IN_UPGRADE_SLOT, false));
    public static final Item HAPPY_GHAST_STRENGTH_UPGRADE = register("happy_ghast_strength_upgrade", HappyGhastUpgrade::new, new Item.Settings().maxCount(1).rarity(Rarity.RARE).component(HappyGhastComponents.IS_IN_UPGRADE_SLOT, false));
    public static final Item HAPPY_GHAST_FIREBALL_UPGRADE = register("happy_ghast_fireball_upgrade", HappyGhastUpgrade::new, new Item.Settings().maxCount(1).rarity(Rarity.RARE).component(HappyGhastComponents.IS_IN_UPGRADE_SLOT, false));
    public static final Item HAPPY_GHAST_HEART_UPGRADE = register("happy_ghast_heart_upgrade", HappyGhastUpgrade::new, new Item.Settings().maxCount(1).rarity(Rarity.RARE).component(HappyGhastComponents.IS_IN_UPGRADE_SLOT, false));
    public static final Item HAPPY_GHAST_ROYALTY_UPGRADE = register("happy_ghast_royalty_upgrade", HappyGhastRoyaltyUpgrade::new, new Item.Settings().maxCount(1).rarity(Rarity.EPIC).component(HappyGhastComponents.IS_IN_UPGRADE_SLOT, false));

    public static final Item HAPPY_GHAST_ICON = register("happy_ghast_icon", Item::new, new Item.Settings());
    public static final Item HAPPY_GHAST_GROWN_ICON = register("happy_ghast_grown_icon", Item::new, new Item.Settings());
    public static final Item HAPPY_GHAST_FREED_ICON = register("happy_ghast_freed_icon", Item::new, new Item.Settings());
    public static final Item HAPPY_GHAST_NETHER_ICON = register("happy_ghast_nether_icon", Item::new, new Item.Settings());
    public static final Item HAPPY_GHAST_END_ICON = register("happy_ghast_end_icon", Item::new, new Item.Settings());
    public static final Item HAPPY_GHAST_WORRIED_ICON = register("happy_ghast_worried_icon", Item::new, new Item.Settings());
    public static final Item FRIENDLY_FIRE_ICON = register("friendly_fire_icon", Item::new, new Item.Settings());
    public static final Item SNOWBALL_GOLDEN_ITEM = register("snowball_golden_item", GoldenSnowball::new, new Item.Settings().fireproof().maxCount(1));

    public static final Item GHAST_SOUL = register("ghast_soul", HappyGhastSoul::new, new Item.Settings().maxCount(1).rarity(Rarity.RARE));

    public static @NotNull EquippableComponent happyGhastHarness(DyeColor dyeColor) {
        return EquippableComponent.builder(EquipmentSlot.SADDLE)
                .equipSound(SoundEvents.ENTITY_HORSE_SADDLE)
                .model(HappyGhastEquipmentAssets.HARNESSES.get(dyeColor))
                .allowedEntities(HappyGhastEntities.HAPPY_GHAST)
                .equipOnInteract(true)
                .build();
    }

    public static final Item GHASTLING_SPAWN_EGG = registerSpawnEgg((String)"ghastling_spawn_egg", (Function)
            ((settings)
                    -> new SpawnEggItem(HappyGhastEntities.GHASTLING, (Item.Settings) settings)));
    public static final Item HAPPY_GHAST_SPAWN_EGG = registerSpawnEgg((String)"happy_ghast_spawn_egg", (Function)
            ((settings)
                    -> new SpawnEggItem(HappyGhastEntities.HAPPY_GHAST, (Item.Settings) settings)));

    private static RegistryKey<Item> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(HappyGhastMod.MOD_ID, id));
    }

    public static Item registerSpawnEgg(String id, Function<Item.Settings, Item> factory) {
        return register2(keyOf(id), factory, new Item.Settings());
    }

    public static Item register2(RegistryKey<Item> key, Function<Item.Settings, Item> factory, Item.Settings settings) {
        Item item = (Item)factory.apply(settings.registryKey(key));
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return (Item)Registry.register(Registries.ITEM, key, item);
    }

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create the item key.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(HappyGhastMod.MOD_ID, name));

        // Create the item instance.
        Item item = itemFactory.apply(settings.registryKey(itemKey));

        // Register the item.
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    public static void initialize() {
    }

}
