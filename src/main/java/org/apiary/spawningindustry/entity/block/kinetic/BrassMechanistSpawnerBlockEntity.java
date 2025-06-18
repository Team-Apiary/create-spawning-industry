package org.apiary.spawningindustry.entity.block.kinetic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apiary.spawningindustry.config.SIConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BrassMechanistSpawnerBlockEntity extends MechanistSpawnerBlockEntity{
    public BrassMechanistSpawnerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    protected int getWorkRequired() {
        return SIConfig.brassSpawnerWork;
    }

    @Override
    protected List<ItemStack> handleXP(List<ItemStack> originalDrops) {
        List<ItemStack> patchedDrops = new ArrayList<>(originalDrops);

        if (SIConfig.generateXP) {
            if (!SIConfig.liquidXP) {
                int xpAmount = new Random().nextInt(3);
                patchedDrops.add(new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "experience_nugget")), xpAmount));
            }
        }

        return patchedDrops;
    }
}
