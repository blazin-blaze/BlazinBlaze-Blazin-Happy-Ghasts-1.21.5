package net.blazinblaze.happyghastmod.item;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class HappyGhastEquipmentAssets {
    public static final Map<DyeColor, RegistryKey<EquipmentAsset>> HARNESSES = Util.mapEnum(
            DyeColor.class,
            dyeColor -> createId("harness_" + dyeColor.getId())
    );

    static @NotNull RegistryKey<EquipmentAsset> createId(String string) {
        return RegistryKey.of(
                RegistryKey.ofRegistry(Identifier.ofVanilla("equipment_asset")),
                Identifier.of(HappyGhastMod.MOD_ID, string)
        );
    }
}
