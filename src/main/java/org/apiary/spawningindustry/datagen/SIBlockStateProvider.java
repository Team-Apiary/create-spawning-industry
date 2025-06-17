package org.apiary.spawningindustry.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.apiary.spawningindustry.block.SIBlocks;
import org.apiary.spawningindustry.main.SIConstants;

public class SIBlockStateProvider extends BlockStateProvider {
    public SIBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, SIConstants.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        //Haunted Iron Blocks
        blockWithItem(SIBlocks.HAUNTED_IRON_BLOCK);
        blockWithItem((DeferredBlock<?>) SIBlocks.ANDESITE_MECHANIST_SPAWNER_BLOCK);
        blockWithItem((DeferredBlock<?>) SIBlocks.BRASS_MECHANIST_SPAWNER_BLOCK);
    }

    private  void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
