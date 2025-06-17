package org.apiary.spawningindustry.block.kinetic;

import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apiary.spawningindustry.entity.block.SIBlockEntities;
import org.apiary.spawningindustry.entity.block.kinetic.BrassMechanistSpawnerBlockEntity;

public class BrassMechanistSpawnerBlock extends MechanistSpawnerBlock<BrassMechanistSpawnerBlockEntity> {

    public BrassMechanistSpawnerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<BrassMechanistSpawnerBlockEntity> getBlockEntityClass() {
        return BrassMechanistSpawnerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BrassMechanistSpawnerBlockEntity> getBlockEntityType() {
        return SIBlockEntities.BRASS_MECHANIST_SPAWNER_BE.get();
    }
}
