package org.apiary.spawningindustry.block.kinetic;

import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apiary.spawningindustry.component.SIDataComponents;
import org.apiary.spawningindustry.entity.block.kinetic.MechanistSpawnerBlockEntity;
import org.apiary.spawningindustry.item.SIItems;
import org.jetbrains.annotations.NotNull;

public abstract class MechanistSpawnerBlock<T extends MechanistSpawnerBlockEntity> extends HorizontalKineticBlock implements IBE<T>, IRotate {

    public MechanistSpawnerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    //Give the player a Soulbound Nexus when they crouch-right-click the spawner, remove the bound soul and transfer it to the given nexus
    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (!player.isCrouching()) {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof MechanistSpawnerBlockEntity spawner)) {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }

        ResourceLocation boundEntity = spawner.getEntityType();
        if (boundEntity == null) {
            return super.useWithoutItem(state, level, pos, player, hitResult);
        }

        ItemStack nexusStack = new ItemStack(SIItems.SOULBOUND_NEXUS.get());
        nexusStack.set(SIDataComponents.ENTITY_TYPE, boundEntity);

        spawner.setEntityType(null);
        spawner.setChanged();
        level.sendBlockUpdated(pos, state, state, 3);

        if (!player.getInventory().add(nexusStack)) {
            ItemEntity dropped = new ItemEntity(level,
                    pos.getX() + 0.5,
                    pos.getY() + 1.0,
                    pos.getZ() + 0.5,
                    nexusStack);
            level.addFreshEntity(dropped);
        }

        return InteractionResult.SUCCESS;
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