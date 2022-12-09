package com.tristankechlo.additionalredstone;

import com.tristankechlo.additionalredstone.commands.ModCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.tristankechlo.additionalredstone.client.ClientSetup;
import com.tristankechlo.additionalredstone.init.ModBlocks;
import com.tristankechlo.additionalredstone.init.ModContainer;
import com.tristankechlo.additionalredstone.init.ModItems;
import com.tristankechlo.additionalredstone.init.ModTileEntities;
import com.tristankechlo.additionalredstone.network.PacketHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AdditionalRedstone.MOD_ID)
public class AdditionalRedstone {

	public static final String MOD_NAME = "AdditionalRedstone";
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
	public static final String MOD_ID = "additionalredstone";
	public static final String GITHUB_URL = "https://github.com/tristankechlo/AdditionalRedstone";
	public static final String GITHUB_ISSUE_URL = GITHUB_URL + "/issues";
	public static final String GITHUB_WIKI_URL = GITHUB_URL + "/wiki";
	public static final String DISCORD_URL = "https://discord.gg/bhUaWhq";
	public static final String CURSEFORGE_URL = "https://curseforge.com/minecraft/mc-mods/additional-redstone";
	public static final String MODRINTH_URL = "https://modrinth.com/mod/additional-redstone";

	public AdditionalRedstone() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		PacketHandler.registerPackets();

		ModItems.ITEMS.register(modEventBus);
		ModBlocks.BLOCKS.register(modEventBus);
		ModTileEntities.TILE_ENTITIES.register(modEventBus);
		ModContainer.CONTAINER_TYPES.register(modEventBus);

		modEventBus.addListener(ClientSetup::init);

		MinecraftForge.EVENT_BUS.addListener(this::commandRegister);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void commandRegister(RegisterCommandsEvent event) {
		ModCommand.register(event.getDispatcher());
	}

}