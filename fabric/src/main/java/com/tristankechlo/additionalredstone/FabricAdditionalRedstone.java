package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.commands.ProjectLinks;
import com.tristankechlo.additionalredstone.init.ModItems;
import com.tristankechlo.additionalredstone.platform.FabricPacketHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.resources.ResourceLocation;

public class FabricAdditionalRedstone implements ModInitializer {

    @Override
    public void onInitialize() {
        //register content
        AdditionalRedstone.init();

        // register creative tab
        FabricItemGroup.builder(new ResourceLocation(AdditionalRedstone.MOD_ID, "main"))
                .icon(() -> ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get().getDefaultInstance())
                .displayItems((params, output) -> AdditionalRedstone.fillItemGroup(output::accept))
                .build();

        //register packets
        FabricPacketHandler.registerPackets();

        //register mod command
        CommandRegistrationCallback.EVENT.register(ProjectLinks::registerAsCommand);
    }

}
