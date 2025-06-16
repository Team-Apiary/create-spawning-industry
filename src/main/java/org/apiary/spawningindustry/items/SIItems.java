package org.apiary.spawningindustry.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apiary.spawningindustry.blocks.SIBlocks;
import org.apiary.spawningindustry.main.SIConstants;

public class SIItems {
    // Deferred Register for items.
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SIConstants.MODID);

    // Register block item linked to our example block.
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("example_block", SIBlocks.EXAMPLE_BLOCK);

    // Register an example food item.
    public static final DeferredItem<Item> EXAMPLE_ITEM =
            ITEMS.registerSimpleItem(
                    "example_item",
                    new Item.Properties().food(
                            new FoodProperties.Builder()
                                    .alwaysEdible()
                                    .nutrition(1)
                                    .saturationModifier(2f)
                                    .build()
                    )
            );
}