package net.blazinblaze.happyghastmod.datagen;

import net.blazinblaze.happyghastmod.tag.HappyGhastTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class HappyGhastTagProvider extends FabricTagProvider<EntityType<?>> {
    public HappyGhastTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ENTITY_TYPE, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(HappyGhastTags.GHASTLING_FOLLOWS)
                .add(EntityType.PLAYER)
                .add(EntityType.ARMADILLO)
                .add(EntityType.BEE)
                .add(EntityType.CAMEL)
                .add(EntityType.CAT)
                .add(EntityType.CHICKEN)
                .add(EntityType.COW)
                .add(EntityType.DONKEY)
                .add(EntityType.FOX)
                .add(EntityType.GOAT)
                .add(EntityType.HORSE)
                .add(EntityType.SKELETON_HORSE)
                .add(EntityType.LLAMA)
                .add(EntityType.MULE)
                .add(EntityType.OCELOT)
                .add(EntityType.PANDA)
                .add(EntityType.PARROT)
                .add(EntityType.PIG)
                .add(EntityType.POLAR_BEAR)
                .add(EntityType.RABBIT)
                .add(EntityType.SHEEP)
                .add(EntityType.SNIFFER)
                .add(EntityType.STRIDER)
                .add(EntityType.VILLAGER)
                .add(EntityType.WOLF);
    }
}
