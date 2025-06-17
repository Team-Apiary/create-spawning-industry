package org.apiary.spawningindustry.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.apiary.spawningindustry.item.SIItems;
import org.apiary.spawningindustry.main.SIConstants;

public class SIItemModelProvider extends ItemModelProvider {
    public SIItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SIConstants.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        //Haunted Iron Items
        basicItem(SIItems.HAUNTED_IRON_INGOT.get());
        basicItem(SIItems.HAUNTED_IRON_NUGGET.get());
        basicItem(SIItems.HAUNTED_IRON_SHEET.get());
        basicItem(SIItems.SOULBOUND_NEXUS.get());
    }
}
