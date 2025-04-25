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
                createShaped(RecipeCategory.MISC, HappyGhastItems.BLUE_HARNESS, 1)
                        .pattern("lll")
                        .pattern("gwg")
                        .input('l', Ingredient.ofItem(Items.LEATHER)) // 'l' means "any log"
                        .input('w', Ingredient.ofItem(Blocks.BLUE_WOOL.asItem()))
                        .input('g', Ingredient.ofItem(Blocks.GLASS.asItem()))
                        .group("blue_harness") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastItems.BLUE_HARNESS), conditionsFromItem(HappyGhastItems.BLUE_HARNESS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.MISC, HappyGhastItems.WHITE_HARNESS, 1)
                        .pattern("lll")
                        .pattern("gwg")
                        .input('l', Ingredient.ofItem(Items.LEATHER)) // 'l' means "any log"
                        .input('w', Ingredient.ofItem(Blocks.WHITE_WOOL.asItem()))
                        .input('g', Ingredient.ofItem(Blocks.GLASS.asItem()))
                        .group("white_harness") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastItems.WHITE_HARNESS), conditionsFromItem(HappyGhastItems.WHITE_HARNESS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.MISC, HappyGhastItems.LIGHT_GRAY_HARNESS, 1)
                        .pattern("lll")
                        .pattern("gwg")
                        .input('l', Ingredient.ofItem(Items.LEATHER)) // 'l' means "any log"
                        .input('w', Ingredient.ofItem(Blocks.LIGHT_GRAY_WOOL.asItem()))
                        .input('g', Ingredient.ofItem(Blocks.GLASS.asItem()))
                        .group("light_gray_harness") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastItems.LIGHT_GRAY_HARNESS), conditionsFromItem(HappyGhastItems.LIGHT_GRAY_HARNESS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.MISC, HappyGhastItems.GRAY_HARNESS, 1)
                        .pattern("lll")
                        .pattern("gwg")
                        .input('l', Ingredient.ofItem(Items.LEATHER)) // 'l' means "any log"
                        .input('w', Ingredient.ofItem(Blocks.GRAY_WOOL.asItem()))
                        .input('g', Ingredient.ofItem(Blocks.GLASS.asItem()))
                        .group("gray_harness") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastItems.GRAY_HARNESS), conditionsFromItem(HappyGhastItems.GRAY_HARNESS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.MISC, HappyGhastItems.BLACK_HARNESS, 1)
                        .pattern("lll")
                        .pattern("gwg")
                        .input('l', Ingredient.ofItem(Items.LEATHER)) // 'l' means "any log"
                        .input('w', Ingredient.ofItem(Blocks.BLACK_WOOL.asItem()))
                        .input('g', Ingredient.ofItem(Blocks.GLASS.asItem()))
                        .group("black_harness") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastItems.BLACK_HARNESS), conditionsFromItem(HappyGhastItems.BLACK_HARNESS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.MISC, HappyGhastItems.BROWN_HARNESS, 1)
                        .pattern("lll")
                        .pattern("gwg")
                        .input('l', Ingredient.ofItem(Items.LEATHER)) // 'l' means "any log"
                        .input('w', Ingredient.ofItem(Blocks.BROWN_WOOL.asItem()))
                        .input('g', Ingredient.ofItem(Blocks.GLASS.asItem()))
                        .group("brown_harness") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastItems.BROWN_HARNESS), conditionsFromItem(HappyGhastItems.BROWN_HARNESS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.MISC, HappyGhastItems.RED_HARNESS, 1)
                        .pattern("lll")
                        .pattern("gwg")
                        .input('l', Ingredient.ofItem(Items.LEATHER)) // 'l' means "any log"
                        .input('w', Ingredient.ofItem(Blocks.RED_WOOL.asItem()))
                        .input('g', Ingredient.ofItem(Blocks.GLASS.asItem()))
                        .group("black_harness") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastItems.RED_HARNESS), conditionsFromItem(HappyGhastItems.RED_HARNESS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.MISC, HappyGhastItems.ORANGE_HARNESS, 1)
                        .pattern("lll")
                        .pattern("gwg")
                        .input('l', Ingredient.ofItem(Items.LEATHER)) // 'l' means "any log"
                        .input('w', Ingredient.ofItem(Blocks.ORANGE_WOOL.asItem()))
                        .input('g', Ingredient.ofItem(Blocks.GLASS.asItem()))
                        .group("orange_harness") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastItems.ORANGE_HARNESS), conditionsFromItem(HappyGhastItems.ORANGE_HARNESS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.MISC, HappyGhastItems.YELLOW_HARNESS, 1)
                        .pattern("lll")
                        .pattern("gwg")
                        .input('l', Ingredient.ofItem(Items.LEATHER)) // 'l' means "any log"
                        .input('w', Ingredient.ofItem(Blocks.YELLOW_WOOL.asItem()))
                        .input('g', Ingredient.ofItem(Blocks.GLASS.asItem()))
                        .group("yellow_harness") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastItems.YELLOW_HARNESS), conditionsFromItem(HappyGhastItems.YELLOW_HARNESS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.MISC, HappyGhastItems.LIME_HARNESS, 1)
                        .pattern("lll")
                        .pattern("gwg")
                        .input('l', Ingredient.ofItem(Items.LEATHER)) // 'l' means "any log"
                        .input('w', Ingredient.ofItem(Blocks.LIME_WOOL.asItem()))
                        .input('g', Ingredient.ofItem(Blocks.GLASS.asItem()))
                        .group("lime_harness") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastItems.LIME_HARNESS), conditionsFromItem(HappyGhastItems.LIME_HARNESS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.MISC, HappyGhastItems.GREEN_HARNESS, 1)
                        .pattern("lll")
                        .pattern("gwg")
                        .input('l', Ingredient.ofItem(Items.LEATHER)) // 'l' means "any log"
                        .input('w', Ingredient.ofItem(Blocks.GREEN_WOOL.asItem()))
                        .input('g', Ingredient.ofItem(Blocks.GLASS.asItem()))
                        .group("green_harness") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastItems.GREEN_HARNESS), conditionsFromItem(HappyGhastItems.GREEN_HARNESS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.MISC, HappyGhastItems.CYAN_HARNESS, 1)
                        .pattern("lll")
                        .pattern("gwg")
                        .input('l', Ingredient.ofItem(Items.LEATHER)) // 'l' means "any log"
                        .input('w', Ingredient.ofItem(Blocks.CYAN_WOOL.asItem()))
                        .input('g', Ingredient.ofItem(Blocks.GLASS.asItem()))
                        .group("cyan_harness") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastItems.CYAN_HARNESS), conditionsFromItem(HappyGhastItems.CYAN_HARNESS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.MISC, HappyGhastItems.LIGHT_BLUE_HARNESS, 1)
                        .pattern("lll")
                        .pattern("gwg")
                        .input('l', Ingredient.ofItem(Items.LEATHER)) // 'l' means "any log"
                        .input('w', Ingredient.ofItem(Blocks.LIGHT_BLUE_WOOL.asItem()))
                        .input('g', Ingredient.ofItem(Blocks.GLASS.asItem()))
                        .group("light_blue_harness") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastItems.LIGHT_BLUE_HARNESS), conditionsFromItem(HappyGhastItems.LIGHT_BLUE_HARNESS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.MISC, HappyGhastItems.PURPLE_HARNESS, 1)
                        .pattern("lll")
                        .pattern("gwg")
                        .input('l', Ingredient.ofItem(Items.LEATHER)) // 'l' means "any log"
                        .input('w', Ingredient.ofItem(Blocks.PURPLE_WOOL.asItem()))
                        .input('g', Ingredient.ofItem(Blocks.GLASS.asItem()))
                        .group("purple_harness") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastItems.PURPLE_HARNESS), conditionsFromItem(HappyGhastItems.PURPLE_HARNESS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.MISC, HappyGhastItems.MAGENTA_HARNESS, 1)
                        .pattern("lll")
                        .pattern("gwg")
                        .input('l', Ingredient.ofItem(Items.LEATHER)) // 'l' means "any log"
                        .input('w', Ingredient.ofItem(Blocks.MAGENTA_WOOL.asItem()))
                        .input('g', Ingredient.ofItem(Blocks.GLASS.asItem()))
                        .group("magenta_harness") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastItems.MAGENTA_HARNESS), conditionsFromItem(HappyGhastItems.MAGENTA_HARNESS))
                        .offerTo(exporter);
                createShaped(RecipeCategory.MISC, HappyGhastItems.PINK_HARNESS, 1)
                        .pattern("lll")
                        .pattern("gwg")
                        .input('l', Ingredient.ofItem(Items.LEATHER)) // 'l' means "any log"
                        .input('w', Ingredient.ofItem(Blocks.PINK_WOOL.asItem()))
                        .input('g', Ingredient.ofItem(Blocks.GLASS.asItem()))
                        .group("pink_harness") // Put it in a group called "multi_bench" - groups are shown in one slot in the recipe book
                        .criterion(hasItem(HappyGhastItems.PINK_HARNESS), conditionsFromItem(HappyGhastItems.PINK_HARNESS))
                        .offerTo(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return "HappyGhastRecipeProvider";
    }
}
