package net.blazinblaze.happyghastmod.loot;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class HappyGhastLootTables {
    public static RegistryKey<LootTable> GOLDEN_SNOWBALL_LOOT_TABLE = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(HappyGhastMod.MOD_ID, "advancement_rewards/golden_snowball_loot_table"));
    public static RegistryKey<LootTable> HAPPY_ROYALTY_LOOT_TABLE = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(HappyGhastMod.MOD_ID, "advancement_rewards/happy_royalty_loot_table"));
}
