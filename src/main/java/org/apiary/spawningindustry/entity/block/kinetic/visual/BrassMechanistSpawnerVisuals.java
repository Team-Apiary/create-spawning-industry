package org.apiary.spawningindustry.entity.block.kinetic.visual;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.instance.Instancer;
import dev.engine_room.flywheel.api.instance.InstancerProvider;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visual.LightUpdatedVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import org.apiary.spawningindustry.client.render.SIRendering;
import org.apiary.spawningindustry.entity.block.kinetic.BrassMechanistSpawnerBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BrassMechanistSpawnerVisuals extends KineticBlockEntityVisual<BrassMechanistSpawnerBlockEntity> implements SimpleDynamicVisual, LightUpdatedVisual {

    public BrassMechanistSpawnerVisuals(VisualizationContext context, BrassMechanistSpawnerBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        initializeVisuals(partialTick);
    }

    protected RotatingInstance shaftInputInstance;

    private void initializeVisuals(float partialTick) {
        try {
            if (SIRendering.MechanistSpawnerShaft == null) {
                return;
            }
            var rotorModelGetter = Models.partial(SIRendering.MechanistSpawnerShaft);

            InstancerProvider ip = instancerProvider();
            if (ip == null) {
                return;
            }
            Instancer<RotatingInstance> instancer = ip.instancer(AllInstanceTypes.ROTATING, rotorModelGetter);
            if (instancer == null) {
                return;
            }
            shaftInputInstance = instancer.createInstance();
            if (shaftInputInstance == null) {
                return;
            }
            shaftInputInstance.setup(blockEntity)
                    .setPosition(getVisualPosition())
                    .setChanged();
            updateLight(partialTick);
        } catch (Exception e) {
            if (shaftInputInstance != null) {
                shaftInputInstance.delete();
                shaftInputInstance = null;
            }
        }
    }

    @Override
    public void beginFrame(DynamicVisual.Context context) {
        if (shaftInputInstance == null) return;
        shaftInputInstance.setup(blockEntity)
                .setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        if (shaftInputInstance != null) {
            relight(getVisualPosition(), shaftInputInstance);
        }
    }

    @Override
    protected void _delete() {
        if (shaftInputInstance != null) {
            shaftInputInstance.delete();
            shaftInputInstance = null;
        }
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        if (shaftInputInstance != null) {
            consumer.accept(shaftInputInstance);
        }
    }
}