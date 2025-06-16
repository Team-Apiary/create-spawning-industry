package org.apiary.spawningindustry.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
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

    public static final TagKey<Block> STORAGE_BLOCKS = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "storage_blocks"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(SIBlocks.HAUNTED_IRON_BLOCK.get());
        tag(STORAGE_BLOCKS)
                .add(SIBlocks.HAUNTED_IRON_BLOCK.get());
        tag(BlockTags.BEACON_BASE_BLOCKS)
                .add(SIBlocks.HAUNTED_IRON_BLOCK.get());
    }
}
