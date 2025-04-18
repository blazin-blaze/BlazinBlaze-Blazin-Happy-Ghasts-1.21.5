package net.blazinblaze.happyghastmod.datagen;

import net.blazinblaze.happyghastmod.item.HappyGhastItems;
import net.blazinblaze.happyghastmod.loot.HappyGhastLootTables;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class HappyGhastLootTableProvider extends SimpleFabricLootTableProvider {
    public HappyGhastLootTableProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup, LootContextTypes.ADVANCEMENT_REWARD);
    }

    @Override
    public void accept(BiConsumer<RegistryKey<LootTable>, LootTable.Builder> lootTableBiConsumer) {
        lootTableBiConsumer.accept(HappyGhastLootTables.GOLDEN_SNOWBALL_LOOT_TABLE, LootTable.builder()
                .pool(LootPool.builder() // One pool
                        .rolls(ConstantLootNumberProvider.create(1.0f))
                        .with(ItemEntry.builder(HappyGhastItems.SNOWBALL_GOLDEN_ITEM) // With an entry that has diamond(s)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f)))) // One diamond
                ));
    }
}
