package org.apiary.spawningindustry.block.kinetic;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apiary.spawningindustry.entity.block.SIBlockEntities;
import org.apiary.spawningindustry.entity.block.kinetic.AndesiteMechanistSpawnerBlockEntity;

public class AndesiteMechanistSpawnerBlock extends MechanistSpawnerBlock<AndesiteMechanistSpawnerBlockEntity> {

    public AndesiteMechanistSpawnerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public Class<AndesiteMechanistSpawnerBlockEntity> getBlockEntityClass() {
        return AndesiteMechanistSpawnerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends AndesiteMechanistSpawnerBlockEntity> getBlockEntityType() {
        return SIBlockEntities.ANDESITE_MECHANIST_SPAWNER_BE.get();
    }
}
