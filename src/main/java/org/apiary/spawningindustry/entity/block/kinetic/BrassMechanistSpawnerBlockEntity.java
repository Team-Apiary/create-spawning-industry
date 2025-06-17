package org.apiary.spawningindustry.entity.block.kinetic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apiary.spawningindustry.config.SIConfig;

public class BrassMechanistSpawnerBlockEntity extends MechanistSpawnerBlockEntity{
    public BrassMechanistSpawnerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    protected int getWorkRequired() {
        return SIConfig.brassSpawnerWork;
    }
}
