package org.apiary.spawningindustry.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apiary.spawningindustry.blocks.SIBlocks;
import org.apiary.spawningindustry.main.SIConstants;

public class SIItems {
    // Deferred Register for items.
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SIConstants.MODID);

    //Items
    //Haunted Iron Items
    public static final DeferredItem<Item> HAUNTED_IRON_INGOT =
            ITEMS.registerSimpleItem("haunted_iron_ingot", new Item.Properties());
    public static final DeferredItem<Item> HAUNTED_IRON_NUGGET =
            ITEMS.registerSimpleItem("haunted_iron_nugget", new Item.Properties());
    public static final DeferredItem<Item> HAUNTED_IRON_SHEET =
            ITEMS.registerSimpleItem("haunted_iron_sheet", new Item.Properties());

    //Block Items
    //Haunted Iron Block Items
    public static final DeferredItem<BlockItem> HAUNTED_IRON_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("haunted_iron_block", SIBlocks.HAUNTED_IRON_BLOCK);
}