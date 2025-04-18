package net.blazinblaze.happyghastmod;

import net.blazinblaze.happyghastmod.attachment.HappyGhastClientAttachments;
import net.blazinblaze.happyghastmod.entity.HappyGhastEntities;
import net.blazinblaze.happyghastmod.entity.model.HappyGhastModelLayers;
import net.blazinblaze.happyghastmod.entity.renderer.GhastlingRenderer;
import net.blazinblaze.happyghastmod.entity.renderer.HappyGhastFireballRenderer;
import net.blazinblaze.happyghastmod.entity.renderer.HappyGhastRenderer;
import net.blazinblaze.happyghastmod.screen.HappyGhastUpgradeScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class HappyGhastModClient implements ClientModInitializer {

	//public static final ScreenHandlerType<HappyGhastUpgradeScreenHandler> GHAST_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(HappyGhastMod.MOD_ID, "happy_ghast_screen_handler"), new ScreenHandlerType<>(HappyGhastUpgradeScreenHandler::new, FeatureSet.empty()));

	@Override
	public void onInitializeClient() {
		HandledScreens.register(HappyGhastMod.GHAST_SCREEN_HANDLER, HappyGhastUpgradeScreen::new);
		HappyGhastModelLayers.registerModelLayers();
		EntityRendererRegistry.register(HappyGhastEntities.HAPPY_GHAST, HappyGhastRenderer::new);
		EntityRendererRegistry.register(HappyGhastEntities.GHASTLING, GhastlingRenderer::new);
		EntityRendererRegistry.register(HappyGhastEntities.HAPPY_GHAST_FIREBALL_ENTITY, HappyGhastFireballRenderer::new);
		HappyGhastClientAttachments.init();
	}
}