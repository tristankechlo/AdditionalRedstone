package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.commands.ProjectLinks;
import com.tristankechlo.additionalredstone.init.ModItems;
import com.tristankechlo.additionalredstone.platform.ForgePacketHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AdditionalRedstone.MOD_ID)
public class ForgeAdditionalRedstone {

    public ForgeAdditionalRedstone() {
        //register commands
        AdditionalRedstone.init();

        //register packets
        ForgePacketHandler.registerPackets();

        @SuppressWarnings("removal") // ignore here, removed in 1.21.1+
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onCreativeModeTabRegister);

        //register listener
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
    }

    private void registerCommands(RegisterCommandsEvent event) {
        ProjectLinks.registerAsCommand(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }


    /* add items to their creative tab */
    private void onCreativeModeTabRegister(CreativeModeTabEvent.Register event) {
        @SuppressWarnings("removal") // ignore here, removed in 1.21.1+
        ResourceLocation mainTab = new ResourceLocation(AdditionalRedstone.MOD_ID, "main");
        event.registerCreativeModeTab(mainTab, builder ->
                builder.title(Component.translatable("itemGroup.additionalredstone.main"))
                        .icon(() -> ModItems.CIRCUIT_MAKER_BLOCK_ITEM.get().getDefaultInstance())
                        .displayItems((params, output) -> AdditionalRedstone.fillItemGroup(output::accept))
                        .build()
        );
    }

}
