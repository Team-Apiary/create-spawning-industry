package org.apiary.spawningindustry.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.apiary.spawningindustry.items.SIItems;
import org.apiary.spawningindustry.main.SIConstants;

public class SIItemModelProvider extends ItemModelProvider {
    public SIItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SIConstants.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(SIItems.EXAMPLE_ITEM.get());
    }
}
