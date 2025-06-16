package org.apiary.spawningindustry.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.apiary.spawningindustry.blocks.SIBlocks;
import org.apiary.spawningindustry.main.SIConstants;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SIBlockTagProvider extends BlockTagsProvider {
    public SIBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SIConstants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(SIBlocks.EXAMPLE_BLOCK.get());
    }
}
