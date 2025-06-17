package org.apiary.spawningindustry.ui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apiary.spawningindustry.item.SIItems;
import org.apiary.spawningindustry.main.SIConstants;

public class SICreativeTabs {
    // Deferred Register for creative tabs.
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(net.minecraft.core.registries.Registries.CREATIVE_MODE_TAB, SIConstants.MODID);

    // Create a creative tab that shows the example item.
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SPAWNING_INDUSTRY_TAB =
            CREATIVE_MODE_TABS.register("spawning_industry", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.spawningindustry"))
                            .withTabsBefore(CreativeModeTabs.COMBAT)
                            .icon(() -> SIItems.HAUNTED_IRON_INGOT.get().getDefaultInstance())
                            .displayItems((parameters, output) -> {
                                output.accept(SIItems.HAUNTED_IRON_INGOT.get());
                                output.accept(SIItems.HAUNTED_IRON_NUGGET.get());
                                output.accept(SIItems.HAUNTED_IRON_SHEET.get());
                                output.accept(SIItems.HAUNTED_IRON_BLOCK_ITEM.get());
                                output.accept(SIItems.BRASS_MECHANIST_SPAWNER_BLOCK_ITEM.get());
                                output.accept(SIItems.ANDESITE_MECHANIST_SPAWNER_BLOCK_ITEM.get());
                            })
                            .build()
            );
}