package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.commands.ProjectLinks;
import com.tristankechlo.additionalredstone.init.ForgeItemGroup;
import com.tristankechlo.additionalredstone.platform.ForgePacketHandler;
import net.minecraft.commands.Commands;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(AdditionalRedstone.MOD_ID)
public class ForgeAdditionalRedstone {

    // registers the creative tab
    public static final CreativeModeTab ITEM_GROUP = new ForgeItemGroup();

    public ForgeAdditionalRedstone() {
        //register commands
        AdditionalRedstone.init();

        //register packets
        ForgePacketHandler.registerPackets();

        //register listener
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        ProjectLinks.registerAsCommand(event.getDispatcher(), event.getEnvironment() == Commands.CommandSelection.DEDICATED);
    }

}
