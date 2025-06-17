package org.apiary.spawningindustry.main;

import com.simibubi.create.foundation.data.CreateRegistrate;
import org.apiary.spawningindustry.ui.SICreativeTabs;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public class SIConstants {
    public static final String MODID = "spawningindustry";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID).defaultCreativeTab(SICreativeTabs.SPAWNING_INDUSTRY_TAB.getKey());
}