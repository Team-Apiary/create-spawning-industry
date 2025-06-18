package org.apiary.spawningindustry.item.custom;

import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.apiary.spawningindustry.block.kinetic.MechanistSpawnerBlock;
import org.apiary.spawningindustry.component.SIDataComponents;
import org.apiary.spawningindustry.entity.block.kinetic.MechanistSpawnerBlockEntity;
import org.apiary.spawningindustry.main.SIConstants;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@EventBusSubscriber(modid = SIConstants.MODID)
public class SoulboundNexusItem extends Item {
    public SoulboundNexusItem(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void onEntityHit(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player || event.getSource().getEntity() instanceof DeployerFakePlayer) {
            // Save the entity type on the items's data component.
            ItemStack weaponStack = event.getEntity().getMainHandItem();
            ResourceLocation entityTypeId = BuiltInRegistries.ENTITY_TYPE.getKey(event.getEntity().getType());
            weaponStack.set(SIDataComponents.ENTITY_TYPE, entityTypeId);
        }
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        // Check if the clicked block is a Mechanist spawner, if so update the Bound Entity Type.
        if (state.getBlock() instanceof MechanistSpawnerBlock<?>) {
            if (!level.isClientSide()) {
                var blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof MechanistSpawnerBlockEntity spawnerEntity) {
                    ItemStack stack = context.getItemInHand();
                    ResourceLocation entityTypeId = stack.get(SIDataComponents.ENTITY_TYPE);
                    if (entityTypeId != null) {
                        spawnerEntity.setEntityType(entityTypeId);
                        spawnerEntity.clearPendingDrops();
                        spawnerEntity.setChanged();
                        stack.shrink(1);
                        level.sendBlockUpdated(pos, state, state, 3);
                        SIConstants.LOGGER.info("Set " + entityTypeId + " to the spawner at " + spawnerEntity.getBlockPos() + " new value is " + spawnerEntity.getEntityType());
                        return InteractionResult.SUCCESS;
                    } else {
                        if (context.getPlayer() != null) {
                            context.getPlayer().displayClientMessage(Component.literal("This item has no entity type stored!"), true);
                        }
                        return InteractionResult.FAIL;
                    }
                }
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        ResourceLocation savedEntity = stack.get(SIDataComponents.ENTITY_TYPE);
        if (savedEntity != null) {
            EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(savedEntity);
            tooltipComponents.add(Component.translatable("tooltip.bound_to_soul", Component.translatable(entityType.getDescriptionId())));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
