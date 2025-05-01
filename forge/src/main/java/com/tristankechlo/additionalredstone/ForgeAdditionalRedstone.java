package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.commands.ProjectLinks;
import com.tristankechlo.additionalredstone.init.ModItems;
import com.tristankechlo.additionalredstone.platform.ForgePacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(AdditionalRedstone.MOD_ID)
public class ForgeAdditionalRedstone {

    public ForgeAdditionalRedstone() {
        //register commands
        AdditionalRedstone.registerContent();

        //register packets
        ForgePacketHandler.registerPackets();

        //register listener
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        MinecraftForge.EVENT_BUS.addListener(this::getBurnTime);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        ProjectLinks.registerAsCommand(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

    private void getBurnTime(final FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().is(ModItems.LIGHT_DETECTOR_ITEM.get())) {
            event.setBurnTime(300);
        }
    }

}
