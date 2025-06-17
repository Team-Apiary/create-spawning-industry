package org.apiary.spawningindustry.block.kinetic;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apiary.spawningindustry.entity.block.kinetic.MechanistSpawnerBlockEntity;
import org.jetbrains.annotations.NotNull;

public abstract class MechanistSpawnerBlock<T extends MechanistSpawnerBlockEntity>
        extends HorizontalKineticBlock implements IBE<T>, IRotate {

    public MechanistSpawnerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    // Accept a shaft only from the bottom.
    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN;
    }

    // The block rotates around the vertical (Y) axis.
    @Override
    public Axis getRotationAxis(BlockState state) {
        return Axis.Y;
    }

    // The spawner only requires a slow minimum speed.
    @Override
    public SpeedLevel getMinimumRequiredSpeedLevel() {
        return SpeedLevel.SLOW;
    }

    // Full cube collision shape.
    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Shapes.block();
    }

    // Full cube occlusion shape used for light culling.
    @Override
    public @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.block();
    }

    @Override
    public boolean skipRendering(@NotNull BlockState state, BlockState adjacentBlockState, @NotNull Direction side) {
        if (adjacentBlockState.is(this)) {
            return true;
        }
        return super.skipRendering(state, adjacentBlockState, side);
    }
}