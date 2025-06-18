package org.apiary.spawningindustry.client.render;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.resources.ResourceLocation;
import org.apiary.spawningindustry.main.SIConstants;

public class SIRendering {
    public static final PartialModel MechanistSpawnerShaft = block("mechanist_spawner_shaft");

    private static PartialModel block(String path) {
        return PartialModel.of(
                ResourceLocation.fromNamespaceAndPath(SIConstants.MODID, "block/" + path)
        );
    }

    public static void load() {
    }
}
