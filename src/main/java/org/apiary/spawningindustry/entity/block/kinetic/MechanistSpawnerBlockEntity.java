package org.apiary.spawningindustry.entity.block.kinetic;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MechanistSpawnerBlockEntity extends KineticBlockEntity {

    protected final ItemStackHandler itemHandler = createOutputHandler();
    private static final int INVENTORY_SIZE = 3;
    private static final int BASE_WORK_PER_ITEM = 2048;
    private int progress = 0;

    protected int getWorkRequired() {
        return BASE_WORK_PER_ITEM;
    }
    public MechanistSpawnerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @NotNull
    private ItemStackHandler createOutputHandler() {
        return new ItemStackHandler(INVENTORY_SIZE) {
            @Override
            protected void onContentsChanged(int slot) {
                // Notify the block entity that its inventory has changed.
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                // Do not allow any items to be inserted.
                return false;
            }

            @NotNull
            @Override
            public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
                // Reject insertion attempts entirely.
                return stack;
            }

            @Override
            public int getSlotLimit(int slot) {
                return INVENTORY_SIZE;
            }
        };
    }

    @Nullable
    public IItemHandler getItemHandlerCapability(@Nullable Direction side) {
        if (side != null && side.getAxis() == Axis.Y) {
            // Deny access from top and bottom.
            return null;
        }
        return itemHandler;
    }

    @Override
    public void tick() {
        super.tick();
        // Ensure we're on the server side.
        if (level == null || level.isClientSide) {
            return;
        }

        if (!itemHandler.getStackInSlot(0).isEmpty()) {
            // Reset progress (or simply pause accumulation) if something is present.
            progress = 0;
            return;  // Do nothing else until the item is removed.
        }

        // Get absolute kinetic speed from the rotational input and reset progress if 0.
        float currentSpeed = Math.abs(getSpeed());
        if (currentSpeed == 0) {
            if (progress != 0) {
                progress = 0;
                setChanged();
            }
            return;
        }
        // Accumulate kinetic progress based on the current speed.
        progress += (int) currentSpeed;
        setChanged();

        // Once sufficient work has been done, generate drops.
        if (progress >= getWorkRequired()) {
            produceMobDrops();
            progress = 0;
        }
    }

    //TODO: Add actual drop logic rather than just rotten flesh
    private void produceMobDrops() {
        ItemStack produced = new ItemStack(Items.ROTTEN_FLESH, 1);
        itemHandler.setStackInSlot(0, produced);

        if (level != null && !level.isClientSide) {
            level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
        }
        setChanged();
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        // Save the accumulated progress.
        compound.putInt("Progress", progress);
        // Save the inventory on the server side.
        if (!clientPacket) {
            compound.put("Inventory", itemHandler.serializeNBT(registries));
        }
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        progress = compound.getInt("Progress");
        // Restore the inventory state if applicable.
        if (!clientPacket && compound.contains("Inventory")) {
            itemHandler.deserializeNBT(registries, compound.getCompound("Inventory"));
        }
        super.read(compound, registries, clientPacket);
    }
}
