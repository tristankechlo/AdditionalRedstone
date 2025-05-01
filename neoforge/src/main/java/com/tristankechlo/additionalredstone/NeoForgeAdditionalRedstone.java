package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.commands.ProjectLinks;
import com.tristankechlo.additionalredstone.init.ModItems;
import com.tristankechlo.additionalredstone.platform.NeoForgePacketHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

@Mod(AdditionalRedstone.MOD_ID)
public class NeoForgeAdditionalRedstone {

    public NeoForgeAdditionalRedstone(IEventBus modEventBus) {
        //register commands
        AdditionalRedstone.registerContent();

        //register packets
        modEventBus.addListener(NeoForgePacketHelper::registerPackets);

        //register listener
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        NeoForge.EVENT_BUS.addListener(this::getBurnTime);
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
