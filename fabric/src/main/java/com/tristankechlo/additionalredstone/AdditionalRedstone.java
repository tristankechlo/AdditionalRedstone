package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.commands.ModCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class AdditionalRedstone implements ModInitializer {

    @Override
    public void onInitialize() {
        //register content
        Constants.registerContent();

        //register packets
        FabricPacketHandler.registerPackets();

        //register mod command
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ModCommand.register(dispatcher);
        });
    }

}
