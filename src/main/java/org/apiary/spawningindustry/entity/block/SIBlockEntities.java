package org.apiary.spawningindustry.entity.block;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.apiary.spawningindustry.entity.block.kinetic.AndesiteMechanistSpawnerBlockEntity;
import org.apiary.spawningindustry.entity.block.kinetic.BrassMechanistSpawnerBlockEntity;
import org.apiary.spawningindustry.entity.block.kinetic.MechanistSpawnerBlockEntity;
import org.apiary.spawningindustry.main.SpawningIndustry;
import org.apiary.spawningindustry.block.SIBlocks;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class SIBlockEntities {

    private static final CreateRegistrate REGISTRATE = SpawningIndustry.registrate();

    // Register the block entity for the Andesite Mechanist Spawner.
    public static final BlockEntityEntry<AndesiteMechanistSpawnerBlockEntity> ANDESITE_MECHANIST_SPAWNER_BE = REGISTRATE
            .blockEntity("andesite_mechanist_spawner", AndesiteMechanistSpawnerBlockEntity::new)
            .validBlocks(SIBlocks.ANDESITE_MECHANIST_SPAWNER_BLOCK::get)
            .register();

    // Register the block entity for the Brass Mechanist Spawner.
    public static final BlockEntityEntry<BrassMechanistSpawnerBlockEntity> BRASS_MECHANIST_SPAWNER_BE = REGISTRATE
            .blockEntity("brass_mechanist_spawner", BrassMechanistSpawnerBlockEntity::new)
            .validBlocks(SIBlocks.BRASS_MECHANIST_SPAWNER_BLOCK::get)
            .register();

    public static void load() {
        // This method forces the class to load and the static fields to be initialized.
    }

    public static void registerCapabilities(final RegisterCapabilitiesEvent event) {
        // Register item handler for the Andesite Mechanist Spawner.
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                SIBlockEntities.ANDESITE_MECHANIST_SPAWNER_BE.get(),
                MechanistSpawnerBlockEntity::getItemHandlerCapability
        );

        // Register item handler for the Brass Mechanist Spawner.
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                SIBlockEntities.BRASS_MECHANIST_SPAWNER_BE.get(),
                MechanistSpawnerBlockEntity::getItemHandlerCapability
        );
    }

}
