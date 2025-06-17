package org.apiary.spawningindustry.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apiary.spawningindustry.block.kinetic.AndesiteMechanistSpawnerBlock;
import org.apiary.spawningindustry.block.kinetic.BrassMechanistSpawnerBlock;
import org.apiary.spawningindustry.main.SIConstants;

public class SIBlocks {
    // Deferred Register for blocks.
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SIConstants.MODID);

    //Haunted Iron Blocks.
    public static final DeferredBlock<Block> HAUNTED_IRON_BLOCK =
            BLOCKS.registerSimpleBlock("haunted_iron_block", BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).mapColor(MapColor.TERRACOTTA_CYAN));

    // Andesite Mechanist Spawner Block.
    public static final DeferredHolder<Block, AndesiteMechanistSpawnerBlock> ANDESITE_MECHANIST_SPAWNER_BLOCK =
            BLOCKS.register("andesite_mechanist_spawner", () -> new AndesiteMechanistSpawnerBlock(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.STONE)
                            .strength(1.5f, 6.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()));

    // Brass Mechanist Spawner Block.
    public static final DeferredHolder<Block, BrassMechanistSpawnerBlock> BRASS_MECHANIST_SPAWNER_BLOCK =
            BLOCKS.register("brass_mechanist_spawner", () -> new BrassMechanistSpawnerBlock(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.METAL)
                            .strength(1.5f, 6.0f)
                            .requiresCorrectToolForDrops()
                            .noOcclusion()));
}