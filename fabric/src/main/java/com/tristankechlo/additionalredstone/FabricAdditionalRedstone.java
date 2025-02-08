package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.commands.ProjectLinks;
import com.tristankechlo.additionalredstone.init.ModItems;
import com.tristankechlo.additionalredstone.platform.FabricPacketHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.resources.ResourceLocation;

public class FabricAdditionalRedstone implements ModInitializer {

    @Override
    public void onInitialize() {
        //register content
        AdditionalRedstone.init();

        // register creative tab
        FabricItemGroupBuilder.create(new ResourceLocation(AdditionalRedstone.MOD_ID, "main"))
                .icon(() -> ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get().getDefaultInstance())
                .appendItems(AdditionalRedstone::fillItemGroup)
                .build();

        //register packets
        FabricPacketHandler.registerPackets();

        //register mod command
        CommandRegistrationCallback.EVENT.register(ProjectLinks::registerAsCommand);
    }

}
