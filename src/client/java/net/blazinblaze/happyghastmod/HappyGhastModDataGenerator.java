package net.blazinblaze.happyghastmod;

import net.blazinblaze.happyghastmod.datagen.HappyGhastAdvancementProvider;
//import net.blazinblaze.happyghastmod.datagen.HappyGhastEquipmentAssetProvider;
import net.blazinblaze.happyghastmod.datagen.HappyGhastLootTableProvider;
import net.blazinblaze.happyghastmod.datagen.HappyGhastRecipeProvider;
import net.blazinblaze.happyghastmod.datagen.HappyGhastTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import org.jetbrains.annotations.NotNull;

public class HappyGhastModDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(@NotNull FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(HappyGhastAdvancementProvider::new);
		pack.addProvider(HappyGhastLootTableProvider::new);
		pack.addProvider(HappyGhastRecipeProvider::new);
		pack.addProvider(HappyGhastTagProvider::new);
	}

	@Override
	public void buildRegistry(@NotNull RegistryBuilder registryBuilder) {
	}

	@Override
	public @NotNull String getEffectiveModId() {
		return HappyGhastMod.MOD_ID;
	}
}
