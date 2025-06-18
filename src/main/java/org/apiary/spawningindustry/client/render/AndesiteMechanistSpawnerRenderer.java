package org.apiary.spawningindustry.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;
import org.apiary.spawningindustry.entity.block.kinetic.AndesiteMechanistSpawnerBlockEntity;
import org.apiary.spawningindustry.main.SIConstants;

public class AndesiteMechanistSpawnerRenderer extends KineticBlockEntityRenderer<AndesiteMechanistSpawnerBlockEntity> {
    public AndesiteMechanistSpawnerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(AndesiteMechanistSpawnerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        Level level = be.getLevel();
        boolean flywheelSupported = level != null && VisualizationManager.supportsVisualization(level);
        if (flywheelSupported) {
            return;
        }
        SIConstants.LOGGER.debug("Flywheel is not Supported.");
    }
}
