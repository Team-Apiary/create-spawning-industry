package org.apiary.ui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apiary.items.SIItems;
import org.apiary.main.SIConstants;

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
                            .icon(() -> SIItems.EXAMPLE_ITEM.get().getDefaultInstance())
                            .displayItems((parameters, output) -> {
                                output.accept(SIItems.EXAMPLE_ITEM.get());
                                output.accept(SIItems.EXAMPLE_BLOCK_ITEM.get());
                            })
                            .build()
            );
}