package org.apiary.spawningindustry.create;

import com.simibubi.create.api.stress.BlockStressValues;
import org.apiary.spawningindustry.block.SIBlocks;
import org.apiary.spawningindustry.config.SIConfig;
import org.apiary.spawningindustry.main.SIConstants;

public class SIStressValues {

    public static void registerAll() {
        SIConstants.LOGGER.debug("Registering Stress Values");
        try {
            BlockStressValues.IMPACTS.register(SIBlocks.BRASS_MECHANIST_SPAWNER_BLOCK.get(), () -> SIConfig.brassSpawnerStress);
            BlockStressValues.IMPACTS.register(SIBlocks.ANDESITE_MECHANIST_SPAWNER_BLOCK.get(), () -> SIConfig.andesiteSpawnerStress);
        } catch (Exception e) {
            SIConstants.LOGGER.error("Failed to Register Stress Values", e);
        }
    }
}

