package org.apiary.spawningindustry.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.apiary.spawningindustry.blocks.SIBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class SIBlockLootTableProvider extends BlockLootSubProvider {
    protected SIBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {

        //Haunted Iron Blocks
        dropSelf(SIBlocks.HAUNTED_IRON_BLOCK.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return SIBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
