package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.commands.ProjectLinks;
import com.tristankechlo.additionalredstone.platform.NeoForgePacketHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod(AdditionalRedstone.MOD_ID)
public class NeoForgeAdditionalRedstone {

    public NeoForgeAdditionalRedstone(IEventBus modEventBus) {
        //register commands
        AdditionalRedstone.registerContent();

        //register packets
        modEventBus.addListener(NeoForgePacketHandler::registerPackets);

        //register listener
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        ProjectLinks.registerAsCommand(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }

}
