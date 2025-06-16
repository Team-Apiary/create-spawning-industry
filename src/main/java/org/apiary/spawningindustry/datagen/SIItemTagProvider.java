package org.apiary.spawningindustry.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.apiary.spawningindustry.items.SIItems;
import org.apiary.spawningindustry.main.SIConstants;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SIItemTagProvider extends ItemTagsProvider {
    public SIItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, SIConstants.MODID, existingFileHelper);
    }

    public static final TagKey<Item> INGOTS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "ingots"));
    public static final TagKey<Item> NUGGETS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "nuggets"));
    public static final TagKey<Item> PLATES = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "plates"));
    public static final TagKey<Item> BEACON_PAYMENT_ITEMS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("minecraft", "beacon_payment_items"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(INGOTS)
                .add(SIItems.HAUNTED_IRON_INGOT.get());
        tag(NUGGETS)
                .add(SIItems.HAUNTED_IRON_NUGGET.get());
        tag(PLATES)
                .add(SIItems.HAUNTED_IRON_SHEET.get());
        tag(BEACON_PAYMENT_ITEMS)
                .add(SIItems.HAUNTED_IRON_INGOT.get());
    }
}
