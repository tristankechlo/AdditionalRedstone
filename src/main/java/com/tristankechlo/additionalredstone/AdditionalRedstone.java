package com.tristankechlo.additionalredstone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tristankechlo.additionalredstone.client.ClientSetup;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.init.ModContainer;
import com.tristankechlo.additionalredstone.init.ModItems;
import com.tristankechlo.additionalredstone.init.ModBlockEntities;
import com.tristankechlo.additionalredstone.network.PacketHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AdditionalRedstone.MOD_ID)
public class AdditionalRedstone {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "additionalredstone";

	public AdditionalRedstone() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		PacketHandler.registerPackets();

		ModItems.ITEMS.register(modEventBus);
		ModBlocks.BLOCKS.register(modEventBus);
		ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
		ModContainer.CONTAINER_TYPES.register(modEventBus);

		modEventBus.addListener(ClientSetup::init);

		MinecraftForge.EVENT_BUS.register(this);
	}

}