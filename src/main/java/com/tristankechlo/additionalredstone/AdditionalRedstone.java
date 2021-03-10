package com.tristankechlo.additionalredstone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod(AdditionalRedstone.MOD_ID)
public class AdditionalRedstone {

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "additionalredstone";

	public AdditionalRedstone() {
//    	ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ImprovedVanillaConfig.spec);
//
//		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

//		ModItems.ITEMS.register(modEventBus);
//		ModBlocks.BLOCKS.register(modEventBus);
//		ModSounds.SOUNDS.register(modEventBus);
//		ModParticle.PARTICLES.register(modEventBus);
//		ModEntityTypes.ENTITY_TYPES.register(modEventBus);

		MinecraftForge.EVENT_BUS.register(this);
	}
}