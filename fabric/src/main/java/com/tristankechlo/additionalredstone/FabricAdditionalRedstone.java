package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.commands.ProjectLinks;
import com.tristankechlo.additionalredstone.init.FabricItemGroup;
import com.tristankechlo.additionalredstone.platform.FabricPacketHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;

public class FabricAdditionalRedstone implements ModInitializer {

    @Override
    public void onInitialize() {
        //register content
        AdditionalRedstone.init();
        FabricItemGroup.register();

        //register packets
        FabricPacketHandler.registerPackets();

        //register mod command
        CommandRegistrationCallback.EVENT.register(ProjectLinks::registerAsCommand);
    }

}
