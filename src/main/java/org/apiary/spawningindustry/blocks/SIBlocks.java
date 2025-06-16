package org.apiary.spawningindustry.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apiary.spawningindustry.main.SIConstants;

public class SIBlocks {
    // Deferred Register for blocks under the mod's namespace.
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SIConstants.MODID);

    // Register an example block.
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock(
            "example_block",
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
    );

    //Haunted Iron Blocks
    public static final DeferredBlock<Block> HAUNTED_IRON_BLOCK =
            BLOCKS.registerSimpleBlock("haunted_iron_block", BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_CYAN));
}