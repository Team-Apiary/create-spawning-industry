package org.apiary.spawningindustry.item.custom;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.apiary.spawningindustry.component.SIDataComponents;
import org.apiary.spawningindustry.main.SIConstants;

import java.util.List;

@EventBusSubscriber(modid = SIConstants.MODID)
public class SoulboundNexusItem extends Item {
    public SoulboundNexusItem(Properties properties) {
        super(properties);
    }

    @SubscribeEvent
    public static void onEntityHit(LivingDeathEvent event) {
        // Ensure the source of damage is a player (or the appropriate attacker)
        if (event.getSource().getEntity() instanceof Player player) {
            // Retrieve the item in hand—for instance, the player's main hand item.
            ItemStack weaponStack = player.getMainHandItem();

            // Obtain the target entity's type as a ResourceLocation.
            // (Use your preferred method to get the entity's identifier.
            //  ForgeRegistries.ENTITY_TYPES.getKey(...) is common in many setups.)
            ResourceLocation entityTypeId = BuiltInRegistries.ENTITY_TYPE.getKey(event.getEntity().getType());

            // Save the entity type on the weapon's data component.
            weaponStack.set(SIDataComponents.ENTITY_TYPE, entityTypeId);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        // Retrieve the saved entity type (a ResourceLocation) from your component.
        ResourceLocation savedEntity = stack.get(SIDataComponents.ENTITY_TYPE);
        if (savedEntity != null) {
            // Look up the EntityType using the built-in registry.
            EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(savedEntity);
            // Get the translation key (usually something like "entity.minecraft.zombie")
            // and create a translatable component so that it pulls the proper language string.
            tooltipComponents.add(Component.translatable(entityType.getDescriptionId()));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
