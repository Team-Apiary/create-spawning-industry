package org.apiary.spawningindustry.datagen;

import com.simibubi.create.content.kinetics.fan.processing.HauntingRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.apiary.spawningindustry.items.SIItems;
import org.apiary.spawningindustry.main.SIConstants;

import java.util.concurrent.CompletableFuture;

public class SIRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public SIRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    //Call each type of Recipe to Register
    @Override
    protected void buildRecipes(RecipeOutput output) {
        compactingRecipes(output);
        shapelessRecipies(output);
        hauntingRecipes(output);
        pressingRecipes(output);
    }

    //Recipe Registration

    protected void compactingRecipes(RecipeOutput output){
        addLargeCompactingRecipe(output, "haunted_iron_block", SIItems.HAUNTED_IRON_BLOCK_ITEM.get(), SIItems.HAUNTED_IRON_INGOT.get());
        addLargeCompactingRecipe(output, "haunted_iron_ingot", SIItems.HAUNTED_IRON_INGOT.get(), SIItems.HAUNTED_IRON_NUGGET.get());

    }

    protected void shapelessRecipies(RecipeOutput output){
        addShapelessRecipe(output, "haunted_iron_ingots_from_block", SIItems.HAUNTED_IRON_INGOT.get(), SIItems.HAUNTED_IRON_BLOCK_ITEM.get(), 9);
        addShapelessRecipe(output, "haunted_iron_nuggets_from_ingot", SIItems.HAUNTED_IRON_NUGGET.get(), SIItems.HAUNTED_IRON_INGOT.get(), 9);
    }

    protected void hauntingRecipes(RecipeOutput output){
        addHauntingRecipe(output, "haunted_iron_ingot", Ingredient.of(Items.IRON_INGOT), SIItems.HAUNTED_IRON_INGOT.get(), 1.0f);
        addHauntingRecipe(output, "haunted_iron_nugget", Ingredient.of(Items.IRON_NUGGET), SIItems.HAUNTED_IRON_NUGGET.get(), 1.0f);
        addHauntingRecipe(output, "haunted_iron_block", Ingredient.of(Items.IRON_BLOCK), SIItems.HAUNTED_IRON_BLOCK_ITEM.get(), 1.0f);
        addHauntingRecipe(output, "haunted_iron_sheet", Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "iron_sheet"))), SIItems.HAUNTED_IRON_SHEET.get(), 1.0f);
    }

    protected void pressingRecipes(RecipeOutput output){
        addPressingRecipe(output, "haunted_iron_sheet", Ingredient.of(SIItems.HAUNTED_IRON_INGOT), SIItems.HAUNTED_IRON_SHEET.get(), 1.0f);
    }

    //Helper Methods

    private void addHauntingRecipe(RecipeOutput output, String recipeName, Ingredient input, Item outputItem, float chance) {
        new StandardProcessingRecipe.Builder<>(HauntingRecipe::new,
                ResourceLocation.fromNamespaceAndPath(SIConstants.MODID, recipeName))
                .withItemIngredients(input)
                .withItemOutputs(new ProcessingOutput(new ItemStack(outputItem), chance))
                .build(output);
    }

    private void addPressingRecipe(RecipeOutput output, String recipeName, Ingredient input, Item outputItem, float chance) {
        new StandardProcessingRecipe.Builder<>(PressingRecipe::new,
                ResourceLocation.fromNamespaceAndPath(SIConstants.MODID, recipeName))
                .withItemIngredients(input)
                .withItemOutputs(new ProcessingOutput(new ItemStack(outputItem), chance))
                .build(output);
    }

    private void addLargeCompactingRecipe(RecipeOutput output, String recipeName, ItemLike result, ItemLike ingredient) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ingredient)
                .unlockedBy("has_item", has(ingredient))
                .save(output, ResourceLocation.fromNamespaceAndPath(SIConstants.MODID, recipeName));
    }

    private void addShapelessRecipe(RecipeOutput output, String recipeName, ItemLike result, ItemLike ingredient, int resultCount) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, new ItemStack(result, resultCount))
                .requires(ingredient)
                .unlockedBy("has_item", has(ingredient))
                .save(output, ResourceLocation.fromNamespaceAndPath(SIConstants.MODID, recipeName));
    }
}
