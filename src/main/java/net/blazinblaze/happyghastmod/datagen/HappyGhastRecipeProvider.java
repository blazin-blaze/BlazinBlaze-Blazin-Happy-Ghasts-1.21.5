package net.blazinblaze.happyghastmod.datagen;

import net.blazinblaze.happyghastmod.block.HappyGhastBlocks;
import net.blazinblaze.happyghastmod.entity.HappyGhastEntities;
import net.blazinblaze.happyghastmod.item.HappyGhastItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class HappyGhastRecipeProvider extends FabricRecipeProvider {
    public HappyGhastRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                RegistryWrapper.Impl<Item> itemLookup = registries.getOrThrow(RegistryKeys.ITEM);
                createShaped(RecipeCategory.MISC, HappyGhastBlocks.DRIED_GHAST.asItem(), 1)
                        .pattern("tbt")
                        .pattern("bsb")
                        .pattern("tbt")
                        .input('t', Ingredient.ofItem(Items.GHAST_TEAR)) // 'l' means "any log"
                        .input('s', Ingredient.ofItem(HappyGhastItems.GHAST_SOUL))
                        .input('b', Ingredient.ofItem(Blocks.BONE_BLOCK.asItem()))
                        .group("dried_ghast") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastBlocks.DRIED_GHAST.asItem()), conditionsFromItem(HappyGhastBlocks.DRIED_GHAST.asItem()))
                        .offerTo(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return "HappyGhastRecipeProvider";
    }
}
