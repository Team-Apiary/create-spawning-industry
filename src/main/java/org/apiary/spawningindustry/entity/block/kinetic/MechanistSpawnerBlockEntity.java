package org.apiary.spawningindustry.entity.block.kinetic;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.apiary.spawningindustry.main.SIConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class MechanistSpawnerBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {

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
            if (boundEntityType != null) {
                produceMobDrops();
                progress = 0;
            }
        }
    }

    private void produceMobDrops() {
        List<ItemStack> drops = identifyDrops((ServerLevel) this.getLevel(), boundEntityType, this.getBlockPos());
        EntityType<?> mobType = BuiltInRegistries.ENTITY_TYPE.get(boundEntityType);
        drops = patchDrops(drops, mobType);
        drops = handleXP(drops);
        pendingDrops.addAll(drops);

        SIConstants.LOGGER.info("Loot table roll returned {} items:", drops.size());
        for (ItemStack stack : drops) {
            SIConstants.LOGGER.info(" -> {}", stack);
        }

        if (itemHandler.getStackInSlot(0).isEmpty() && !pendingDrops.isEmpty()) {
            while (!pendingDrops.isEmpty() && pendingDrops.peek().isEmpty()) {
                pendingDrops.poll();
            }
            if (!pendingDrops.isEmpty()) {
                ItemStack nextDrop = pendingDrops.poll();
                itemHandler.setStackInSlot(0, nextDrop);
            }
        }

        if (level != null && !level.isClientSide) {
            level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
        }
        setChanged();
    }

    private static List<ItemStack> identifyDrops(ServerLevel level, ResourceLocation entityTypeRL, BlockPos spawnerPos) {

        // Identify what the drops should be based on the entity's type and use a minimal player dummy entity to trigger the random loot table roll.
        EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(entityTypeRL);
        ResourceKey<LootTable> lootTableKey = entityType.getDefaultLootTable();
        LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(lootTableKey);

        Entity dummyEntity = entityType.create(level);
        if (dummyEntity == null) {
            throw new IllegalStateException("Unable to create dummy entity for loot context.");
        }
        dummyEntity.setPos(spawnerPos.getX(), spawnerPos.getY(), spawnerPos.getZ());

        ResourceLocation playerRL = ResourceLocation.tryParse("minecraft:player_attack");
        if (playerRL == null) {
            throw new IllegalStateException("Unable to parse player damage type resource location.");
        }
        Holder<DamageType> playerHolder = level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolder(ResourceKey.create(Registries.DAMAGE_TYPE, playerRL))
                .orElseThrow(() -> new IllegalStateException("Player DamageType not found"));

        DamageSource playerDamageSource = new DamageSource(playerHolder);

        ItemStack dummyItem = new ItemStack(Items.DIAMOND_SWORD);

        ServerPlayer fakePlayer = FakePlayerFactory.getMinecraft(level);

        LootParams lootParams = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(spawnerPos))
                .withParameter(LootContextParams.THIS_ENTITY, dummyEntity)
                .withParameter(LootContextParams.DAMAGE_SOURCE, playerDamageSource)
                .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, fakePlayer)
                .withParameter(LootContextParams.TOOL, dummyItem)
                .create(LootContextParamSets.ENTITY);

        return lootTable.getRandomItems(lootParams);
    }

    private List<ItemStack> patchDrops(List<ItemStack> originalDrops, EntityType<?> mobType) {
        // Add patches to the drops list for certain entities that need it.
        List<ItemStack> patchedDrops = new ArrayList<>(originalDrops);

        if (mobType.equals(EntityType.SHEEP)) {
            int woolAmount = new Random().nextInt(3);
            patchedDrops.add(new ItemStack(Items.WHITE_WOOL, woolAmount));
        }

        return patchedDrops;
    }

    //This method exists to be overridden by the two different spawners.
    protected List<ItemStack> handleXP(List<ItemStack> originalDrops) {
        return originalDrops;
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
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        // Display Header.
        CreateLang.translate("gui.goggles.spawner_stats").forGoggles(tooltip);

        // Display Soulbound Nexus status and Bound Soul information.
        if (boundEntityType != null) {
            CreateLang.builder()
                    .add(Component.literal("Soulbound Nexus: ").withStyle(ChatFormatting.GRAY))
                    .add(Component.literal("Inserted").withStyle(ChatFormatting.GREEN))
                    .forGoggles(tooltip, 1);
            EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(boundEntityType);
            CreateLang.builder()
                    .add(Component.literal("Bound Soul: ").withStyle(ChatFormatting.GRAY))
                    .add(Component.translatable(entityType.getDescriptionId()).withStyle(ChatFormatting.AQUA))
                    .forGoggles(tooltip, 1);
        } else {
            CreateLang.builder()
                    .add(Component.literal("Soulbound Nexus: ").withStyle(ChatFormatting.GRAY))
                    .add(Component.literal("Not Inserted").withStyle(ChatFormatting.RED))
                    .forGoggles(tooltip, 1);
        }

        // Blank line.
        tooltip.add(Component.empty());

        // Display Kinetic Stats.
        if (IRotate.StressImpact.isEnabled()) {
            float stressAtBase = calculateStressApplied();
            if (!Mth.equal(stressAtBase, 0)) {
                addStressImpactStats(tooltip, stressAtBase);
            }
        }

        return true;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putInt("Progress", progress);

        if (!clientPacket) {
            compound.put("Inventory", itemHandler.serializeNBT(registries));
        }

        if (this.boundEntityType != null) {
            compound.putString("EntityType", this.boundEntityType.toString());
        } else {
            compound.remove("EntityType");
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
        } else {
            this.boundEntityType = null;
        }
        super.read(compound, registries, clientPacket);
    }
}
