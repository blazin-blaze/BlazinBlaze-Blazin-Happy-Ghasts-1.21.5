package net.blazinblaze.happyghastmod.tag;

import net.blazinblaze.happyghastmod.HappyGhastMod;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class HappyGhastTags {
    public static final TagKey<EntityType<?>> GHASTLING_FOLLOWS = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(HappyGhastMod.MOD_ID, "ghastling_follows"));
}
