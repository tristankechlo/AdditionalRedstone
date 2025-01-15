package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.commands.ModCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class AdditionalRedstone {

    public AdditionalRedstone() {
        //register commands
        Constants.registerContent();

        //register packets
        ForgePacketHandler.registerPackets();

        //register listener
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    /* register commands */
    private void registerCommands(final RegisterCommandsEvent event) {
        ModCommand.register(event.getDispatcher());
    }

}
