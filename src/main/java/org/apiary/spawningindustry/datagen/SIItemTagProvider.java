package org.apiary.spawningindustry.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.apiary.spawningindustry.items.SIItems;
import org.apiary.spawningindustry.main.SIConstants;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SIItemTagProvider extends ItemTagsProvider {
    public SIItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, SIConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        /*tag(Tags.Items.FOODS_COOKIE)
                .add(SIItems.EXAMPLE_ITEM.get());*/
    }
}
