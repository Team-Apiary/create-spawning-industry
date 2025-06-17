
package org.apiary.spawningindustry.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apiary.spawningindustry.main.SIConstants;

@EventBusSubscriber(modid = SIConstants.MODID, bus = EventBusSubscriber.Bus.MOD)
public class SIConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue GENERATE_XP = BUILDER
            .comment("Enable XP Output from Mechanist Spawners? (Default: True)")
            .define("generateXP", true);

    private static final ModConfigSpec.IntValue BRASS_SPAWNER_STRESS = BUILDER
            .comment("Brass Mechanist Spawner: Stress on mechanical system. (Higher = Harder to run)")
            .defineInRange("brassSpawnerStress", 256, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue BRASS_SPAWNER_WORK = BUILDER
            .comment("Brass Mechanist Spawner: Work needed for one set of items to be generated. (Higher = Slower)")
            .defineInRange("brassSpawnerWork", 1024, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue ANDESITE_SPAWNER_STRESS = BUILDER
            .comment("Andesite Mechanist Spawner: Stress on mechanical system. (Higher = Harder to run)")
            .defineInRange("andesiteSpawnerStress", 64, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue ANDESITE_SPAWNER_WORK = BUILDER
            .comment("Andesite Mechanist Spawner: Work needed for one set of items to be generated. (Higher = Slower)")
            .defineInRange("andesiteSpawnerWork", 8192, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean generateXP;
    public static int brassSpawnerStress;
    public static int brassSpawnerWork;
    public static int andesiteSpawnerStress;
    public static int andesiteSpawnerWork;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        try {
            generateXP = GENERATE_XP.get();
            brassSpawnerStress = BRASS_SPAWNER_STRESS.get();
            brassSpawnerWork = BRASS_SPAWNER_WORK.get();
            andesiteSpawnerStress = ANDESITE_SPAWNER_STRESS.get();
            andesiteSpawnerWork = ANDESITE_SPAWNER_WORK.get();
        } catch (Exception e) {
            SIConstants.LOGGER.error("Failed to load config: {}. Please check your config file as it is invalid.", e.getMessage());
        }
    }
}
