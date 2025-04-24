package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.commands.ProjectLinks;
import com.tristankechlo.additionalredstone.platform.FabricPacketHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class FabricAdditionalRedstone implements ModInitializer {

    @Override
    public void onInitialize() {
        //register content
        AdditionalRedstone.registerContent();

        //register packets
        FabricPacketHandler.registerPackets();

        //register mod command
        CommandRegistrationCallback.EVENT.register(ProjectLinks::registerAsCommand);
    }

}
