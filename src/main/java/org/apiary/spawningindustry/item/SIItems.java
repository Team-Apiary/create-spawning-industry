package org.apiary.spawningindustry.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apiary.spawningindustry.block.SIBlocks;
import org.apiary.spawningindustry.block.kinetic.AndesiteMechanistSpawnerBlock;
import org.apiary.spawningindustry.item.custom.SoulboundNexusItem;
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


    public static final DeferredHolder<Item, SoulboundNexusItem> SOULBOUND_NEXUS =
            ITEMS.register("soulbound_nexus",  () -> new SoulboundNexusItem(new Item.Properties()));


    //Block Items
    //Haunted Iron Block Items
    public static final DeferredItem<BlockItem> HAUNTED_IRON_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("haunted_iron_block", SIBlocks.HAUNTED_IRON_BLOCK);

    // Mechanist Spawner Block Items
    public static final DeferredItem<BlockItem> ANDESITE_MECHANIST_SPAWNER_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("andesite_mechanist_spawner", SIBlocks.ANDESITE_MECHANIST_SPAWNER_BLOCK);
    public static final DeferredItem<BlockItem> BRASS_MECHANIST_SPAWNER_BLOCK_ITEM =
            ITEMS.registerSimpleBlockItem("brass_mechanist_spawner", SIBlocks.BRASS_MECHANIST_SPAWNER_BLOCK);
}