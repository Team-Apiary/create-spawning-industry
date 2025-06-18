package org.apiary.spawningindustry.main;

import com.simibubi.create.foundation.data.CreateRegistrate;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.apiary.spawningindustry.block.SIBlocks;
import org.apiary.spawningindustry.client.render.BrassMechanistSpawnerRenderer;
import org.apiary.spawningindustry.client.render.SIRendering;
import org.apiary.spawningindustry.component.SIDataComponents;
import org.apiary.spawningindustry.config.SIConfig;
import org.apiary.spawningindustry.create.SIStressValues;
import org.apiary.spawningindustry.entity.block.SIBlockEntities;
import org.apiary.spawningindustry.entity.block.kinetic.BrassMechanistSpawnerBlockEntity;
import org.apiary.spawningindustry.entity.block.kinetic.visual.BrassMechanistSpawnerVisuals;
import org.apiary.spawningindustry.item.SIItems;
import org.apiary.spawningindustry.ui.SICreativeTabs;

import static org.apiary.spawningindustry.main.SIConstants.REGISTRATE;

@Mod(SIConstants.MODID)
public class SpawningIndustry {

    public SpawningIndustry(IEventBus modEventBus, ModContainer modContainer) {

        REGISTRATE.registerEventListeners(modEventBus);

        // Register our deferred registers
        SIBlocks.BLOCKS.register(modEventBus);
        SIItems.ITEMS.register(modEventBus);
        SICreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        SIBlockEntities.load();
        SIDataComponents.register(modEventBus);
        SIRendering.load();

        // Add common setup listener.
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(SIBlockEntities::registerCapabilities);

        // Subscribe to additional events on the global NeoForge event bus.
        NeoForge.EVENT_BUS.register(this);

        // Register mod configuration.
        modContainer.registerConfig(ModConfig.Type.COMMON, SIConfig.SPEC);
    }

    // Called during common setup phase.
    private void commonSetup(final FMLCommonSetupEvent event) {
        SIConstants.LOGGER.info("Create: Spawning Industry Initializing");
        SIStressValues.registerAll();
    }

    // Server starting event subscription.
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // Client-side events.
    @EventBusSubscriber(
            modid = SIConstants.MODID,
            bus = EventBusSubscriber.Bus.MOD,
            value = Dist.CLIENT
    )
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                RenderType translucentType = RenderType.translucent();
                ItemBlockRenderTypes.setRenderLayer(SIBlocks.BRASS_MECHANIST_SPAWNER_BLOCK.get(), translucentType);

                //PonderIndex.addPlugin(new ModPonderPlugin());

                BlockEntityType<BrassMechanistSpawnerBlockEntity> beType = SIBlockEntities.BRASS_MECHANIST_SPAWNER_BE.get();
                SimpleBlockEntityVisualizer.builder(beType).factory(BrassMechanistSpawnerVisuals::new).skipVanillaRender(blockEntity -> true).apply();

                RenderType brassMechanistSpawnerRenderType = RenderType.translucent();
                ItemBlockRenderTypes.setRenderLayer(SIBlocks.BRASS_MECHANIST_SPAWNER_BLOCK.get(), brassMechanistSpawnerRenderType);
                BlockEntityRenderers.register(SIBlockEntities.BRASS_MECHANIST_SPAWNER_BE.get(), BrassMechanistSpawnerRenderer::new);
            });
        }
    }

    public static CreateRegistrate registrate() {
        return REGISTRATE;
    }
}
