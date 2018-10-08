package com.matt.forgehax.mods;

import com.google.common.eventbus.Subscribe;
import com.matt.forgehax.asm.events.WorldCheckLightForEvent;
import com.matt.forgehax.util.mod.Category;
import com.matt.forgehax.util.mod.ToggleMod;
import com.matt.forgehax.util.mod.loader.RegisterMod;
import net.minecraft.world.EnumSkyBlock;

/**
 * Created on 2/10/2018 by fr1kin
 */
@RegisterMod
public class NoSkylightUpdates extends ToggleMod {
    public NoSkylightUpdates() {
        super(Category.RENDER, "NoSkylightUpdates", false, "Prevents skylight updates");
    }

    @Subscribe
    public void onLightingUpdate(WorldCheckLightForEvent event) {
        if(event.getEnumSkyBlock() == EnumSkyBlock.SKY)
            event.setCanceled(true);
    }
}
