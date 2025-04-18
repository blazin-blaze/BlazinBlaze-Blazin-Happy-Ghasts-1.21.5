package net.blazinblaze.happyghastmod;

import net.blazinblaze.happyghastmod.achievement.HappyGhastCriteria;
import net.blazinblaze.happyghastmod.attachments.HappyGhastAttachments;
import net.blazinblaze.happyghastmod.block.HappyGhastBlocks;
import net.blazinblaze.happyghastmod.component.HappyGhastComponents;
import net.blazinblaze.happyghastmod.entity.HappyGhastEntities;
import net.blazinblaze.happyghastmod.item.HappyGhastItems;
import net.blazinblaze.happyghastmod.loot.HappyGhastLootTableModifiers;
import net.blazinblaze.happyghastmod.screen.HappyGhastUpgradeScreenHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HappyGhastMod implements ModInitializer {
	public static final String MOD_ID = "happy-ghast-mod";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static MinecraftServer serverRef;

	public static final RegistryKey<ItemGroup> CUSTOM_ITEM_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(MOD_ID, "happy_ghast_item_group"));
	public static final ItemGroup CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(HappyGhastBlocks.HYDRATED_GHAST.asItem()))
			.displayName(Text.translatable("itemGroup.happy-ghast-mod"))
			.build();

	public static final ScreenHandlerType<HappyGhastUpgradeScreenHandler> GHAST_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(MOD_ID, "happy_ghast_screen_handler"), new ScreenHandlerType<>(HappyGhastUpgradeScreenHandler::new, FeatureSet.empty()));

	@Override
	public void onInitialize() {
		HappyGhastAttachments.init();
		HappyGhastBlocks.initialize();
		HappyGhastEntities.registerModEntites();;
		HappyGhastEntities.registerAttributes();
		HappyGhastCriteria.init();

		HappyGhastComponents.initialize();

		HappyGhastItems.initialize();

		HappyGhastLootTableModifiers.modifyLootTables();

		Registry.register(Registries.ITEM_GROUP, CUSTOM_ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);

		ItemGroupEvents.modifyEntriesEvent(CUSTOM_ITEM_GROUP_KEY).register(itemGroup -> {
			itemGroup.add(HappyGhastItems.HAPPY_GHAST_SPAWN_EGG);
			itemGroup.add(HappyGhastItems.GHASTLING_SPAWN_EGG);
			itemGroup.add(HappyGhastItems.GHAST_SOUL);
			itemGroup.add(HappyGhastItems.WHITE_HARNESS);
			itemGroup.add(HappyGhastItems.GRAY_HARNESS);
			itemGroup.add(HappyGhastItems.LIGHT_GRAY_HARNESS);
			itemGroup.add(HappyGhastItems.BLACK_HARNESS);
			itemGroup.add(HappyGhastItems.BROWN_HARNESS);
			itemGroup.add(HappyGhastItems.RED_HARNESS);
			itemGroup.add(HappyGhastItems.ORANGE_HARNESS);
			itemGroup.add(HappyGhastItems.YELLOW_HARNESS);
			itemGroup.add(HappyGhastItems.GREEN_HARNESS);
			itemGroup.add(HappyGhastItems.LIME_HARNESS);
			itemGroup.add(HappyGhastItems.BLUE_HARNESS);
			itemGroup.add(HappyGhastItems.LIGHT_BLUE_HARNESS);
			itemGroup.add(HappyGhastItems.CYAN_HARNESS);
			itemGroup.add(HappyGhastItems.PURPLE_HARNESS);
			itemGroup.add(HappyGhastItems.MAGENTA_HARNESS);
			itemGroup.add(HappyGhastItems.PINK_HARNESS);
			itemGroup.add(HappyGhastItems.HAPPY_GHAST_SPEED_UPGRADE);
			itemGroup.add(HappyGhastItems.HAPPY_GHAST_STRENGTH_UPGRADE);
			itemGroup.add(HappyGhastItems.HAPPY_GHAST_FIREBALL_UPGRADE);
			itemGroup.add(HappyGhastItems.HAPPY_GHAST_HEART_UPGRADE);
			itemGroup.add(HappyGhastItems.HAPPY_GHAST_ROYALTY_UPGRADE);
			itemGroup.add(HappyGhastItems.SNOWBALL_GOLDEN_ITEM);
			itemGroup.add(HappyGhastBlocks.DRIED_GHAST.asItem());
			itemGroup.add(HappyGhastBlocks.NEUTRAL_DRIED_GHAST.asItem());
			itemGroup.add(HappyGhastBlocks.HYDRATED_GHAST.asItem());});

		ServerLifecycleEvents.SERVER_STARTED.register((server -> {
			serverRef = server;
		}));

		ServerLifecycleEvents.SERVER_STOPPED.register((server -> {
			serverRef = null;
		}));
	}
}