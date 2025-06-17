package org.apiary.spawningindustry.entity.block.kinetic;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.apiary.spawningindustry.main.SIConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MechanistSpawnerBlockEntity extends KineticBlockEntity {

    protected final ItemStackHandler itemHandler = createOutputHandler();
    private static final int INVENTORY_SIZE = 3;
    private static final int BASE_WORK_PER_ITEM = 2048;
    private int progress = 0;
    private ResourceLocation boundEntityType;
    private final Queue<ItemStack> pendingDrops = new LinkedList<>();

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
        if (level == null || level.isClientSide) {
            return;
        }

        // Do not make progress unless the last item got removed.
        if (!itemHandler.getStackInSlot(0).isEmpty()) {
            progress = 0;
            return;
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

    private void produceMobDrops() {
        List<ItemStack> drops = identifyDrops((ServerLevel) this.getLevel(), boundEntityType, this.getBlockPos());
        pendingDrops.addAll(drops);

        if (itemHandler.getStackInSlot(0).isEmpty() && !pendingDrops.isEmpty()) {
            while (!pendingDrops.isEmpty() && pendingDrops.peek().isEmpty()) {
                pendingDrops.poll();
            }
            if (!pendingDrops.isEmpty()) {
                ItemStack nextDrop = pendingDrops.poll();
                itemHandler.setStackInSlot(0, nextDrop);
                SIConstants.LOGGER.info("Dispensing drop: {} x{}", nextDrop.getItem().getDescriptionId(), nextDrop.getCount());
            }
        }

        if (level != null && !level.isClientSide) {
            level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
        }
        setChanged();
    }

    private static List<ItemStack> identifyDrops(ServerLevel level, ResourceLocation entityTypeRL, BlockPos spawnerPos) {

        // Identify what the drops should be based on the entity's type and use a minimal dummy entity to trigger the random loot table roll.
        EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(entityTypeRL);
        ResourceKey<LootTable> lootTableKey = entityType.getDefaultLootTable();
        LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(lootTableKey);

        Entity dummyEntity = entityType.create(level);
        if (dummyEntity == null) {
            throw new IllegalStateException("Unable to create dummy entity for loot context.");
        }
        dummyEntity.setPos(spawnerPos.getX(), spawnerPos.getY(), spawnerPos.getZ());

        ResourceLocation genericRL = ResourceLocation.tryParse("minecraft:generic");
        if (genericRL == null) {
            throw new IllegalStateException("Unable to parse generic damage type resource location.");
        }
        Holder<DamageType> genericHolder = level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolder(ResourceKey.create(Registries.DAMAGE_TYPE, genericRL))
                .orElseThrow(() -> new IllegalStateException("Generic DamageType not found"));

        DamageSource genericDamageSource = new DamageSource(genericHolder);

        LootParams lootParams = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(spawnerPos))
                .withParameter(LootContextParams.THIS_ENTITY, dummyEntity)
                .withParameter(LootContextParams.DAMAGE_SOURCE, genericDamageSource)
                .create(LootContextParamSets.ENTITY);

        return lootTable.getRandomItems(lootParams);
    }

    public void clearPendingDrops() {
        pendingDrops.clear();
    }

    public void setEntityType(ResourceLocation entityType) {
        this.boundEntityType = entityType;
    }

    public ResourceLocation getEntityType() {
        return this.boundEntityType;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putInt("Progress", progress);

        if (!clientPacket) {
            compound.put("Inventory", itemHandler.serializeNBT(registries));
        }

        if (this.boundEntityType != null) {
            compound.putString("EntityType", this.boundEntityType.toString());
        }

        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        progress = compound.getInt("Progress");

        if (!clientPacket && compound.contains("Inventory")) {
            itemHandler.deserializeNBT(registries, compound.getCompound("Inventory"));
        }

        if (compound.contains("EntityType", Tag.TAG_STRING)) {
            this.boundEntityType = ResourceLocation.tryParse(compound.getString("EntityType"));
        }

        super.read(compound, registries, clientPacket);
    }

}
